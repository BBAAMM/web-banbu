package com.banbu.kim.repository;

import com.banbu.kim.entity.AptSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AptSalesRepository extends JpaRepository<AptSales, Long> {

  List<AptSales> findByLawdCdAndDealDateBetween(
      String lawdCd, LocalDate startDate, LocalDate endDate);

  List<AptSales> findByApartmentNameContaining(String apartmentName);

  List<AptSales> findByDongAndDealDateBetween(
      String dong, LocalDate startDate, LocalDate endDate);

  @Query("SELECT a FROM AptSales a WHERE a.lawdCd = :lawdCd " +
      "AND a.dealAmount BETWEEN :minAmount AND :maxAmount " +
      "AND a.dealDate BETWEEN :startDate AND :endDate")
  List<AptSales> findByPriceRange(
      @Param("lawdCd") String lawdCd,
      @Param("minAmount") int minAmount,
      @Param("maxAmount") int maxAmount,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
