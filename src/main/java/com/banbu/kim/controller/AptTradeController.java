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

@Tag(name = "Apartment Sales Data API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/apt-trade", produces = MediaType.APPLICATION_XML_VALUE)
public class AptTradeController {
    private final AptTradeService aptTradeService;

    @Operation(summary = "Request Apartment Sales Data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Request Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Request Failed")
    })
    @GetMapping("/sales")
    public AptTradeResponse getAptTrade(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "11110") String lawdCd,
            @RequestParam(defaultValue = "202409") String dealYmd) {
        return aptTradeService.getAptTradeData(pageNo, numOfRows, lawdCd, dealYmd);
    }

    @Operation(summary = "Save Specific Region Apartment Sales Data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Save Success", content = @Content(schema = @Schema(implementation = AptTradeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Save Failed")
    })
    @PostMapping("/save")
    public AptTradeResponse saveAptTrade(
            @RequestParam(defaultValue = "11110") String lawdCd,
            @RequestParam(defaultValue = "202409") String dealYmd) {
        int totalSaved = aptTradeService.saveAptTradeData(lawdCd, dealYmd);

        AptTradeResponse response = new AptTradeResponse();
        response.setHeader(new AptTradeResponse.Header("00", "Success"));

        AptTradeResponse.Body body = new AptTradeResponse.Body();
        body.setMessage(String.format("Successfully saved %d apartment sales records", totalSaved));
        body.setSavedCount(totalSaved);
        response.setBody(body);

        return response;
    }

    @Operation(summary = "Save All Region Apartment Sales Data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Save Success", content = @Content(schema = @Schema(implementation = AptTradeResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Save Failed")
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