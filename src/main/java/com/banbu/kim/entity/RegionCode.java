package com.banbu.kim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionCode {

    @EmbeddedId
    private RegionCodeId id;

    @Column(name = "city", nullable = false, length = 10)
    private String city;

    @Column(name = "gu", nullable = false, length = 20)
    private String gu;

    @Column(name = "area", nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegionCodeId {

        @Column(name = "city_code")
        private Integer cityCode;

        @Column(name = "gu_code")
        private Integer guCode;

        public RegionCodeId(Integer cityCode, Integer guCode) {
            this.cityCode = cityCode;
            this.guCode = guCode;
        }
    }
}