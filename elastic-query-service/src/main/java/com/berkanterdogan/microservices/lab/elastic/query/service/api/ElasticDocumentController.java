package com.berkanterdogan.microservices.lab.elastic.query.service.api;

import com.berkanterdogan.microservices.lab.elastic.query.service.business.ElasticQueryService;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;

    @Operation(summary = "Get all elastic documents.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = {
                        @Content(
                                mediaType = "application/vnd.api.v1+json",
                                schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                        )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server error",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @GetMapping("")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        log.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get elastic document by id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server error",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get elastic document by id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v2+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModelV2.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v2+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server error",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v2+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel responseModel = elasticQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        ElasticQueryServiceResponseModelV2 responseModelV2 = transformToResponseModelV2(responseModel);
        return ResponseEntity.ok(responseModelV2);
    }

    @Operation(summary = "Get elastic document by text.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server error",
                    content = {
                            @Content(
                                    mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = String.class)
                            )
                    }
            )
    })
    @PostMapping("/get-documents-by-text")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequestModel request) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentsByText(request.getText());
        log.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponseModelV2 transformToResponseModelV2(ElasticQueryServiceResponseModel responseModel) {
        ElasticQueryServiceResponseModelV2 responseModelV2 = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(responseModel.getId()))
                .userId(responseModel.getUserId())
                .text(responseModel.getText())
                .text2("Version 2 text")
                .build();
        responseModelV2.add(responseModel.getLinks());
        return responseModelV2;
    }

}
