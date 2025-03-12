package com.banbu.kim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Table(name = "region_dong", indexes = {
        @Index(name = "idx_region_dong", columnList = "gu_code"),
        @Index(name = "idx_city_gu_dong", columnList = "city,gu,dong")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegionDong {

    @EmbeddedId
    private RegionDongId id;

    @Column(name = "city", nullable = false, length = 10)
    private String city;

    @Column(name = "gu", nullable = false, length = 20)
    private String gu;

    @Column(name = "city_code", nullable = false)
    private Integer cityCode;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class RegionDongId {
        @Column(name = "gu_code", nullable = false)
        private Integer guCode;

        @Column(name = "dong", nullable = false, length = 30)
        private String dong;

        public RegionDongId(Integer guCode, String dong) {
            this.guCode = guCode;
            this.dong = dong;
        }
    }
}