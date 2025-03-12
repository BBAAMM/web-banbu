package com.banbu.kim.service;

import com.banbu.kim.dto.AptTradeResponse;

public interface AptTradeService {
    AptTradeResponse getAptTradeData(int pageNo, int numOfRows, String lawdCd, String dealYmd);

    int saveAptTradeData(String lawdCd, String dealYmd);

    int saveAllRegionsAptTradeData(String dealYmd);
}