package com.berkanterdogan.microservices.lab.elastic.config;

import com.berkanterdogan.microservices.lab.app.config.data.elastic.ElasticConfigData;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackages = "com.berkanterdogan.microservices.lab.elastic.index.client.repository")
@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private final ElasticConfigData elasticConfigData;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        String connectionUrl = elasticConfigData.getConnectionUrl();
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(connectionUrl).build();


        String hostname = Objects.requireNonNull(uriComponents.getHost());
        int port = uriComponents.getPort();
        String scheme = uriComponents.getScheme();

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                        hostname,
                        port,
                        scheme
                )).setRequestConfigCallback(
                        requstConfigBuilder ->
                        {
                            Integer connectionTimeoutMs = elasticConfigData.getConnectionTimeoutMs();
                            Integer socketTimeoutMs = elasticConfigData.getSocketTimeoutMs();
                            return requstConfigBuilder
                                    .setConnectTimeout(connectionTimeoutMs)
                                    .setSocketTimeout(socketTimeoutMs);
                        }
                )
        );
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}
