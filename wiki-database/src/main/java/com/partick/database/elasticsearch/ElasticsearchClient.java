package com.partick.database.elasticsearch;

import com.partick.common.config.RefreshNacosConfig;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ElasticsearchClient {

    @Resource
    private RefreshNacosConfig refreshNacosConfig;

    public RestHighLevelClient getRestHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(refreshNacosConfig.getEsHost(),9200)));
    }
}
