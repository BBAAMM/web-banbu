package com.banbu.kim.feign;

import com.banbu.kim.dto.AptTradeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;

@FeignClient(
        name = "aptTradeClient",
        url = "http://apis.data.go.kr"
//        configuration = JacksonXmlFeignConfiguration.class
)
public interface AptTradeFeignClient {

    /**
     * 공공데이터포털에서 제공하는 아파트 매매 실거래가 상세 자료 API를 호출합니다.
     *
     * @param serviceKey  인증키 (공공데이터포털에서 발급받은 서비스 키, URL Encode 필요)
     * @param pageNo      페이지 번호 (요청할 페이지 번호, 예: 1)
     * @param numOfRows   한 페이지 결과 수 (한 페이지에 반환될 데이터 개수, 예: 10)
     * @param lawdCd      지역코드 (행정표준코드관리시스템의 법정동코드 앞 5자리, 예: "11110" - 서울 종로구)
     * @param dealYmd     계약월 (실거래 자료의 계약 년월, 6자리 숫자, 예: "202407" - 2024년 07월)
     * @return            API 응답 데이터를 매핑한 AptTradeResponse 객체
     */
    @GetMapping(
            value = "/1613000/RTMSDataSvcAptTradeDev/getRTMSDataSvcAptTradeDev",
            produces = MediaType.APPLICATION_XML_VALUE)
    AptTradeResponse getAptTradeData(
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("numOfRows") int numOfRows,
            @RequestParam("LAWD_CD") String lawdCd,
            @RequestParam("DEAL_YMD") String dealYmd
    );
}
