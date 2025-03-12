package com.banbu.kim.repository;

import com.banbu.kim.entity.RegionCode;
import com.banbu.kim.entity.RegionCode.RegionCodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionCodeRepository extends JpaRepository<RegionCode, RegionCodeId> {
    List<RegionCode> findById_CityCode(Integer cityCode);

    Optional<RegionCode> findById_CityCodeAndId_GuCode(Integer cityCode, Integer guCode);

    Optional<RegionCode> findById_GuCode(Integer guCode);

    List<RegionCode> findByCity(String city);

    void deleteById_CityCodeAndId_GuCode(Integer cityCode, Integer guCode);
}