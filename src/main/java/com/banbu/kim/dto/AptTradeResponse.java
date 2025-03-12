package com.banbu.kim.dto;

import com.banbu.kim.entity.AptSales;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * AptTradeResponse는 공공데이터포털의 아파트 매매 실거래가 상세 자료 API 응답 XML을 매핑하는 클래스입니다.
 * 응답은 <response> 루트 엘리먼트 아래에 header와 body로 구성되어 있습니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "response")
@Schema(description = "API Response for apartment sales data")
public class AptTradeResponse {

    /**
     * API 호출 결과를 포함하는 header 영역
     * - resultCode: 결과 코드 (예: "00"이면 성공)
     * - resultMsg: 결과 메시지
     */
    @JacksonXmlProperty(localName = "header")
    private Header header;

    /**
     * 실제 거래 데이터와 페이지 정보를 포함하는 body 영역
     * - numOfRows: 한 페이지에 반환되는 결과 수
     * - pageNo: 요청한 페이지 번호
     * - totalCount: 전체 결과 수
     * - items: 거래 내역 목록
     */
    @JacksonXmlProperty(localName = "body")
    private Body body;

    /**
     * Header 클래스는 API 응답의 상태 정보를 담고 있습니다.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    /**
     * Body 클래스는 페이지 정보와 거래 상세 내역을 포함합니다.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        @JacksonXmlProperty(localName = "numOfRows")
        private int numOfRows;

        @JacksonXmlProperty(localName = "pageNo")
        private int pageNo;

        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;

        @JacksonXmlProperty(localName = "items")
        private Items items;

        // 저장 작업 결과를 위한 필드 추가
        @JacksonXmlProperty(localName = "savedCount")
        private Integer savedCount;

        @JacksonXmlProperty(localName = "message")
        private String message;
    }

    /**
     * Items 클래스는 다수의 거래 내역(Item)들을 포함합니다.
     * XML에서는 여러 <item> 요소가 래퍼 없이 나열되는 구조이므로 useWrapping=false를 지정합니다.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<Item> item;
    }

    /**
     * Item 클래스는 개별 거래 내역의 세부 정보를 담고 있습니다.
     * 아래 필드들은 공공데이터포털 API 응답에 정의된 모든 요소를 매핑합니다.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @JacksonXmlProperty(localName = "sggCd")
        private String sggCd;

        @JacksonXmlProperty(localName = "umdNm")
        private String umdNm;

        @JacksonXmlProperty(localName = "jibun")
        private String jibun;

        @JacksonXmlProperty(localName = "aptNm")
        private String aptNm;

        @JacksonXmlProperty(localName = "excluUseAr")
        private String excluUseAr;

        @JacksonXmlProperty(localName = "dealYear")
        private String dealYear;

        @JacksonXmlProperty(localName = "dealMonth")
        private String dealMonth;

        @JacksonXmlProperty(localName = "dealDay")
        private String dealDay;

        @JacksonXmlProperty(localName = "dealAmount")
        private String dealAmount;

        @JacksonXmlProperty(localName = "floor")
        private String floor;

        @JacksonXmlProperty(localName = "buildYear")
        private String buildYear;

        @JacksonXmlProperty(localName = "cdealType")
        private String cdealType;

        @JacksonXmlProperty(localName = "estateAgentSggNm")
        private String estateAgentSggNm;

        @JacksonXmlProperty(localName = "rgstDate")
        private String rgstDate;

        @JacksonXmlProperty(localName = "aptDong")
        private String aptDong;

        @JacksonXmlProperty(localName = "slerGbn")
        private String slerGbn;

        @JacksonXmlProperty(localName = "buyerGbn")
        private String buyerGbn;

        @JacksonXmlProperty(localName = "aptSeq")
        private String aptSeq;

        @JacksonXmlProperty(localName = "roadNm")
        private String roadNm;

        @JacksonXmlProperty(localName = "roadNmBonbun")
        private String roadNmBonbun;

        @JacksonXmlProperty(localName = "roadNmBubun")
        private String roadNmBubun;
    }
}
