// src/main/java/com/banbu/kim/controller/AptTradeController.java
package com.banbu.kim.controller;

import com.banbu.kim.dto.AptTradeResponse;
import com.banbu.kim.service.AptTradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "아파트 실거래가 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/apt-trade", produces = MediaType.APPLICATION_XML_VALUE)
public class AptTradeController {
    private final AptTradeService aptTradeService;

    @Operation(summary = "실거래가 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/sales")
    public AptTradeResponse getAptTrade(
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int pageNo,
            @Parameter(description = "페이지당 데이터 수", example = "10") @RequestParam(defaultValue = "10") int numOfRows,
            @Parameter(description = "법정동코드", example = "11110") @RequestParam String lawdCd,
            @Parameter(description = "거래년월(YYYYMM)", example = "202403") @RequestParam String dealYmd) {
        return aptTradeService.getAptTradeData(pageNo, numOfRows, lawdCd, dealYmd);
    }

    @Operation(summary = "특정 지역 실거래가 데이터 저장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = AptTradeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/save")
    public AptTradeResponse saveAptTrade(
            @Parameter(description = "법정동코드", example = "11110") @RequestParam String lawdCd,
            @Parameter(description = "거래년월(YYYYMM)", example = "202403") @RequestParam String dealYmd) {
        int totalSaved = aptTradeService.saveAptTradeData(lawdCd, dealYmd);

        AptTradeResponse response = new AptTradeResponse();
        response.setHeader(new AptTradeResponse.Header("00", "Success"));

        AptTradeResponse.Body body = new AptTradeResponse.Body();
        body.setMessage(String.format("Successfully saved %d apartment sales records", totalSaved));
        body.setSavedCount(totalSaved);
        response.setBody(body);

        return response;
    }

    @Operation(summary = "전체 지역 실거래가 데이터 저장")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = AptTradeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/save-all-regions")
    public AptTradeResponse saveAllRegionsAptTrade(
            @Parameter(description = "거래년월(YYYYMM)", example = "202403") @RequestParam String dealYmd) {
        int totalSaved = aptTradeService.saveAllRegionsAptTradeData(dealYmd);

        AptTradeResponse response = new AptTradeResponse();
        response.setHeader(new AptTradeResponse.Header("00", "Success"));

        AptTradeResponse.Body body = new AptTradeResponse.Body();
        body.setMessage(String.format("Successfully saved %d apartment sales records from all regions", totalSaved));
        body.setSavedCount(totalSaved);
        response.setBody(body);

        return response;
    }
}