package com.banbu.kim.entity;

import com.banbu.kim.dto.AptTradeResponse;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@ToString
@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_apt_sales_lawd_cd_deal_date", columnList = "lawd_cd,deal_date"),
        @Index(name = "idx_apt_sales_apartment_name", columnList = "apartment_name"),
        @Index(name = "idx_apt_sales_dong", columnList = "dong"),
        @Index(name = "idx_apt_sales_deal_amount", columnList = "deal_amount")
})
public class AptSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lawd_cd", length = 10, nullable = false)
    private String lawdCd;

    @Column(name = "apartment_name", length = 200, nullable = false)
    private String apartmentName;

    @Column(name = "deal_date", nullable = false)
    private LocalDate dealDate;

    @Column(name = "floor", nullable = false)
    private int floor;

    @Column(name = "area_str", length = 20, nullable = false)
    private String areaStr;

    @Column(name = "regional_code", nullable = false)
    private int regionalCode;

    @Column(name = "dong", length = 100)
    private String dong;

    @Column(name = "jibun", columnDefinition = "varchar(20) default ''")
    private String jibun;

    @Column(name = "deal_amount", columnDefinition = "int default 0")
    private int dealAmount;

    @Column(name = "build_year", columnDefinition = "int default 0")
    private int buildYear;

    @Column(name = "trade_type")
    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Column(name = "agency_address", length = 200)
    private String agencyAddress;

    @Column(name = "registration_date", length = 20)
    private String registrationDate;

    @Column(name = "seller", length = 50)
    private String seller;

    @Column(name = "buyer", length = 50)
    private String buyer;

    @Column(name = "apartment_dong", length = 100)
    private String apartmentDong;

    @Column(name = "apt_seq", length = 50)
    private String aptSeq;

    @Column(name = "road_name", length = 200)
    private String roadName;

    @Column(name = "road_name_bonbun_bubun", length = 50)
    private String roadNameBonbunBubun;

    @Builder
    private AptSales(String lawdCd, String apartmentName, LocalDate dealDate, int floor, String areaStr,
            int regionalCode, String dong, String jibun, int dealAmount, int buildYear,
            TradeType tradeType, String agencyAddress, String registrationDate,
            String seller, String buyer, String apartmentDong,
            String aptSeq, String roadName, String roadNameBonbunBubun) {
        this.lawdCd = lawdCd;
        this.apartmentName = apartmentName;
        this.dealDate = dealDate;
        this.floor = floor;
        this.areaStr = areaStr;
        this.regionalCode = regionalCode;
        this.dong = dong;
        this.jibun = jibun;
        this.dealAmount = dealAmount;
        this.buildYear = buildYear;
        this.tradeType = tradeType;
        this.agencyAddress = agencyAddress;
        this.registrationDate = registrationDate;
        this.seller = seller;
        this.buyer = buyer;
        this.apartmentDong = apartmentDong;
        this.aptSeq = aptSeq;
        this.roadName = roadName;
        this.roadNameBonbunBubun = roadNameBonbunBubun;
    }

    public enum TradeType {
        NORMAL("0"),
        CANCELED("1");

        private final String code;

        TradeType(String code) {
            this.code = code;
        }

        public static TradeType fromCode(String code) {
            if (code == null)
                return NORMAL;
            return code.equals("1") ? CANCELED : NORMAL;
        }
    }

    private static int parseIntSafely(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static AptSales convertToAptSales(AptTradeResponse.Item item, String lawdCd) {
        validateInput(item, lawdCd);

        LocalDate dealDate = parseDealDate(item.getDealYear(), item.getDealMonth(), item.getDealDay());

        return AptSales.builder()
                .lawdCd(lawdCd)
                .apartmentName(item.getAptNm())
                .dealDate(dealDate)
                .floor(parseIntSafely(item.getFloor(), 1))
                .areaStr(item.getExcluUseAr())
                .regionalCode(parseIntSafely(item.getSggCd(), 0))
                .dong(item.getUmdNm())
                .jibun(item.getJibun())
                .dealAmount(parseIntSafely(item.getDealAmount().replaceAll(",", "").trim(), 0))
                .buildYear(parseIntSafely(item.getBuildYear(), 0))
                .tradeType(TradeType.fromCode(item.getCdealType()))
                .agencyAddress(item.getEstateAgentSggNm())
                .registrationDate(item.getRgstDate())
                .seller(item.getSlerGbn())
                .buyer(item.getBuyerGbn())
                .apartmentDong(item.getAptDong())
                .aptSeq(item.getAptSeq())
                .roadName(item.getRoadNm())
                .roadNameBonbunBubun(buildRoadNameBonbunBubun(item))
                .build();
    }

    private static void validateInput(AptTradeResponse.Item item, String lawdCd) {
        if (item == null || lawdCd == null) {
            throw new IllegalArgumentException("Item and lawdCd must not be null");
        }
        if (item.getAptNm() == null || item.getAptNm().trim().isEmpty()) {
            throw new IllegalArgumentException("Apartment name must not be null or empty");
        }
        if (item.getExcluUseAr() == null || item.getExcluUseAr().trim().isEmpty()) {
            throw new IllegalArgumentException("Area must not be null or empty");
        }
    }

    private static String buildRoadNameBonbunBubun(AptTradeResponse.Item item) {
        String bonbun = item.getRoadNmBonbun();
        String bubun = item.getRoadNmBubun();

        if (bonbun == null || bonbun.trim().isEmpty()) {
            return "";
        }

        if (bubun != null && !bubun.equals("0")) {
            return bonbun + "-" + bubun;
        }

        return bonbun;
    }

    private static LocalDate parseDealDate(String year, String month, String day) {
        try {
            int y = parseIntSafely(year, -1);
            int m = parseIntSafely(month, -1);
            int d = parseIntSafely(day, -1);

            if (y <= 0 || m <= 0 || m > 12 || d <= 0 || d > 31) {
                throw new IllegalArgumentException(
                        String.format("Invalid date values: year=%s, month=%s, day=%s", year, month, day));
            }

            return LocalDate.of(y, m, d);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Failed to parse date", e);
        }
    }
}
