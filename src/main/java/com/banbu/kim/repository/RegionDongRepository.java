package com.banbu.kim.repository;

import com.banbu.kim.entity.RegionDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionDongRepository extends JpaRepository<RegionDong, Long> {
    List<RegionDong> findById_GuCode(Integer guCode);

    Optional<RegionDong> findById_GuCodeAndId_Dong(Integer guCode, String dong);

    List<RegionDong> findByCityCodeAndGuCode(Integer cityCode, Integer guCode);

    List<RegionDong> findByCityAndGu(String city, String gu);

    void deleteById_GuCode(Integer guCode);
}