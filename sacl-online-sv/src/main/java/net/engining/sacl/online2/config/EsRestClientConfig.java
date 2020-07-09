package net.engining.sacl.online2.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2020-07-03 17:36
 * @since :
 **/
@Configuration
public class EsRestClientConfig extends AbstractElasticsearchConfiguration {

    @Autowired
    RestClientProperties restClientProperties;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(ClientConfiguration.create(restClientProperties.getUris().get(0))).rest();
    }

    // no special bean creation needed
}
