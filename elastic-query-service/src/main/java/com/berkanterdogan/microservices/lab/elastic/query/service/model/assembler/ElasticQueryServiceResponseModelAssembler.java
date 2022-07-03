package com.berkanterdogan.microservices.lab.elastic.query.service.model.assembler;

import com.berkanterdogan.microservices.lab.elastic.model.index.impl.TwitterIndexModel;
import com.berkanterdogan.microservices.lab.elastic.query.service.api.ElasticDocumentController;
import com.berkanterdogan.microservices.lab.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.berkanterdogan.microservices.lab.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticQueryServiceResponseModelAssembler extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    public ElasticQueryServiceResponseModelAssembler(ElasticToResponseModelTransformer elasticToResponseModelTransformer) {
        super(ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
        this.elasticToResponseModelTransformer = elasticToResponseModelTransformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(TwitterIndexModel twitterIndexModel) {
        ElasticQueryServiceResponseModel responseModel = elasticToResponseModelTransformer.transformResponseModel(twitterIndexModel);
        responseModel.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                ElasticDocumentController.class
                        ).getDocumentById(twitterIndexModel.getId())
                ).withSelfRel()
        );
        responseModel.add(
                WebMvcLinkBuilder.linkTo(ElasticDocumentController.class)
                        .withRel("documents")
        );

        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels.stream().map(this::toModel).collect(Collectors.toList());
    }
}
