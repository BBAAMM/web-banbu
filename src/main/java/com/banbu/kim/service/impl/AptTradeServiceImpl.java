package com.banbu.kim.service.impl;

import com.banbu.kim.dto.AptTradeResponse;
import com.banbu.kim.entity.AptSales;
import com.banbu.kim.entity.RegionCode;
import com.banbu.kim.feign.AptTradeFeignClient;
import com.banbu.kim.repository.AptSalesRepository;
import com.banbu.kim.repository.RegionCodeRepository;
import com.banbu.kim.service.AptTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AptTradeServiceImpl implements AptTradeService {
    private final AptTradeFeignClient aptTradeFeignClient;
    private final AptSalesRepository aptSalesRepository;
    private final RegionCodeRepository regionCodeRepository;
    private static final int BATCH_SIZE = 2000;

    @Value("${apt.trade.service.key}")
    private String serviceKey;

    @Override
    public AptTradeResponse getAptTradeData(int pageNo, int numOfRows, String lawdCd, String dealYmd) {
        return aptTradeFeignClient.getAptTradeData(serviceKey, pageNo, numOfRows, lawdCd, dealYmd);
    }

    @Override
    @Transactional
    public int saveAptTradeData(String lawdCd, String dealYmd) {
        log.info("Starting to save apartment sales data - District Code: {}, Transaction Date: {}", lawdCd, dealYmd);
        return processAndSaveTradeData(lawdCd, dealYmd);
    }

    @Override
    @Transactional
    public int saveAllRegionsAptTradeData(String dealYmd) {
        log.info("Starting to save apartment sales data for all regions - Transaction Date: {}", dealYmd);
        AtomicInteger totalSaved = new AtomicInteger(0);

        List<RegionCode> regionCodes = regionCodeRepository.findAll();
        log.info("Found {} regions to process", regionCodes.size());

        regionCodes.forEach(regionCode -> {
            String lawdCd = String.format("%d", regionCode.getId().getGuCode());
            // 구 코드가 5자리가 되도록 앞에 0을 채움
            while (lawdCd.length() < 5) {
                lawdCd = "0" + lawdCd;
            }

            try {
                int saved = processAndSaveTradeData(lawdCd, dealYmd);
                totalSaved.addAndGet(saved);
                log.info("Processed region {} ({}): saved {} records",
                        regionCode.getGu(), lawdCd, saved);
            } catch (Exception e) {
                log.error("Error processing region {} ({}): {}",
                        regionCode.getGu(), lawdCd, e.getMessage());
            }
        });

        log.info("Completed saving apartment sales data for all regions. Total saved: {}",
                totalSaved.get());
        return totalSaved.get();
    }

    private int processAndSaveTradeData(String lawdCd, String dealYmd) {
        int pageNo = 1;
        int numOfRows = 1000;
        boolean hasMoreData = true;
        AtomicInteger totalSaved = new AtomicInteger(0);
        List<AptSales> batchList = new ArrayList<>();

        while (hasMoreData) {
            log.debug("Fetching page {} with {} rows per page for region {}",
                    pageNo, numOfRows, lawdCd);
            AptTradeResponse response = aptTradeFeignClient.getAptTradeData(
                    serviceKey, pageNo, numOfRows, lawdCd, dealYmd);

            if (response.getBody().getItems() != null &&
                    response.getBody().getItems().getItem() != null) {

                List<AptTradeResponse.Item> items = response.getBody().getItems().getItem();

                // Convert items to AptSales entities
                items.forEach(item -> {
                    try {
                        batchList.add(AptSales.convertToAptSales(item, lawdCd));

                        // When batch size is reached, save the batch
                        if (batchList.size() >= BATCH_SIZE) {
                            aptSalesRepository.saveAll(batchList);
                            totalSaved.addAndGet(batchList.size());
                            batchList.clear();
                        }
                    } catch (Exception e) {
                        log.error("Error converting item: {}", e.getMessage());
                    }
                });

                // Check for more pages
                int totalCount = response.getBody().getTotalCount();
                hasMoreData = pageNo * numOfRows < totalCount;
                pageNo++;
            } else {
                hasMoreData = false;
            }
        }

        // Save any remaining items in the batch
        if (!batchList.isEmpty()) {
            aptSalesRepository.saveAll(batchList);
            totalSaved.addAndGet(batchList.size());
            batchList.clear();
        }

        return totalSaved.get();
    }
}