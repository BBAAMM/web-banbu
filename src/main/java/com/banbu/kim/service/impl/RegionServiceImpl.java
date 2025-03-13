package com.banbu.kim.service.impl;

import com.banbu.kim.entity.RegionCode;
import com.banbu.kim.entity.RegionDong;
import com.banbu.kim.repository.RegionCodeRepository;
import com.banbu.kim.repository.RegionDongRepository;
import com.banbu.kim.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionServiceImpl implements RegionService {

    private final RegionCodeRepository regionCodeRepository;
    private final RegionDongRepository regionDongRepository;

    @Override
    public List<RegionCode> getAllRegionCodes() {
        return regionCodeRepository.findAll();
    }

    @Override
    public List<RegionCode> getRegionCodesByCity(String city) {
        return regionCodeRepository.findByCity(city);
    }

    @Override
    public RegionCode getRegionCodeByGuCode(Integer guCode) {
        return regionCodeRepository.findById_GuCode(guCode)
                .orElseThrow(() -> {
                    log.error("구 코드를 찾을 수 없습니다: {}", guCode);
                    return new IllegalArgumentException("해당 구 코드가 존재하지 않습니다: " + guCode);
                });
    }

    @Override
    @Transactional
    public RegionCode saveRegionCode(RegionCode regionCode) {
        log.info("지역 코드 저장: city={}, gu={}, cityCode={}",
                regionCode.getCity(), regionCode.getGu(), regionCode.getId().getCityCode());
        return regionCodeRepository.save(regionCode);
    }

    @Override
    @Transactional
    public void deleteRegionCode(Integer cityCode) {
        log.info("지역 코드 삭제: cityCode={}", cityCode);
        List<RegionCode> regionCodes = regionCodeRepository.findById_CityCode(cityCode);
        regionCodes.forEach(regionCode -> regionCodeRepository.deleteById_CityCodeAndId_GuCode(
                regionCode.getId().getCityCode(),
                regionCode.getId().getGuCode()));
    }

    @Override
    public List<RegionDong> getAllRegionDongs() {
        return regionDongRepository.findAll();
    }

    @Override
    public List<RegionDong> getRegionDongsByGuCode(Integer guCode) {
        return regionDongRepository.findById_GuCode(guCode);
    }

    @Override
    public List<RegionDong> getRegionDongsByCityAndGu(String city, String gu) {
        return regionDongRepository.findByCityAndGu(city, gu);
    }

    @Override
    public RegionDong getRegionDongByGuCodeAndDong(Integer guCode, String dong) {
        return regionDongRepository.findById_GuCodeAndId_Dong(guCode, dong)
                .orElseThrow(() -> {
                    log.error("동 정보를 찾을 수 없습니다: guCode={}, dong={}", guCode, dong);
                    return new IllegalArgumentException(
                            String.format("해당 동이 존재하지 않습니다: guCode=%d, dong=%s", guCode, dong));
                });
    }

    @Override
    @Transactional
    public RegionDong saveRegionDong(RegionDong regionDong) {
        log.info("동 정보 저장: city={}, gu={}, dong={}",
                regionDong.getCity(), regionDong.getGu(), regionDong.getId().getDong());
        return regionDongRepository.save(regionDong);
    }

    @Override
    @Transactional
    public void deleteRegionDong(Long id) {
        log.info("동 정보 삭제: id={}", id);
        regionDongRepository.deleteById(id);
    }

    @Override
    public List<RegionDong> getRegionDongsByRegionCode(RegionCode regionCode) {
        return regionDongRepository.findById_GuCode(regionCode.getId().getGuCode());
    }
}