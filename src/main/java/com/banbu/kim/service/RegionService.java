package com.banbu.kim.service;

import com.banbu.kim.entity.RegionCode;
import com.banbu.kim.entity.RegionDong;

import java.util.List;

public interface RegionService {
    // RegionCode 관련 메서드
    List<RegionCode> getAllRegionCodes();
    List<RegionCode> getRegionCodesByCity(String city);
    RegionCode getRegionCodeByGuCode(Integer guCode);
    RegionCode saveRegionCode(RegionCode regionCode);
    void deleteRegionCode(Integer cityCode);
    
    // RegionDong 관련 메서드
    List<RegionDong> getAllRegionDongs();
    List<RegionDong> getRegionDongsByGuCode(Integer guCode);
    List<RegionDong> getRegionDongsByCityAndGu(String city, String gu);
    RegionDong getRegionDongByGuCodeAndDong(Integer guCode, String dong);
    RegionDong saveRegionDong(RegionDong regionDong);
    void deleteRegionDong(Long id);
    
    // 연관 관계 조회 메서드
    List<RegionDong> getRegionDongsByRegionCode(RegionCode regionCode);
} 