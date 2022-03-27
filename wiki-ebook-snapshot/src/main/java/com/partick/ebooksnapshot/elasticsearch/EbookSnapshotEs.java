package com.partick.ebooksnapshot.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.partick.common.resp.StatisticResp;
import com.partick.database.elasticsearch.ElasticsearchClient;
import com.partick.database.entity.EbookSnapshot;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class EbookSnapshotEs {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    public List<StatisticResp> get30StatisticES() {
        List<StatisticResp> respList = this.getStatisticES(31);
        respList.remove(30);
        return respList;
    }

    public List<StatisticResp> getStatisticES(){
        return this.getStatisticES(2);
    }

    private List<StatisticResp> getStatisticES(int size) {
        RestHighLevelClient client = null;
        try {
            client = elasticsearchClient.getRestHighLevelClient();
            SearchRequest request = new SearchRequest("ebooksnapshot");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            TermsAggregationBuilder builder = AggregationBuilders
                    .terms("date_count").field("date")
                    .size(size)
                    .order(BucketOrder.key(false));
            SumAggregationBuilder viewCountBuilder = AggregationBuilders.sum("viewCount").field("viewCount");
            SumAggregationBuilder voteCountBuilder = AggregationBuilders.sum("voteCount").field("voteCount");
            SumAggregationBuilder viewIncreaseBuilder = AggregationBuilders.sum("viewIncrease").field("viewIncrease");
            SumAggregationBuilder voteIncreaseBuilder = AggregationBuilders.sum("voteIncrease").field("voteIncrease");
            builder.subAggregation(viewCountBuilder)
                    .subAggregation(voteCountBuilder)
                    .subAggregation(viewIncreaseBuilder)
                    .subAggregation(voteIncreaseBuilder);
            sourceBuilder.aggregation(builder);

            request.source(sourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            Terms dateCount = response.getAggregations().get("date_count");
            ArrayList<StatisticResp> respArrayList = new ArrayList<>();
            //遍历结果集
            for (int i = dateCount.getBuckets().size() - 1; i >= 0; i--) {
                Terms.Bucket bucket = dateCount.getBuckets().get(i);
                ParsedSum viewCountSum = bucket.getAggregations().get("viewCount");
                ParsedSum voteCountSum = bucket.getAggregations().get("voteCount");
                ParsedSum viewIncreaseSum = bucket.getAggregations().get("viewIncrease");
                ParsedSum voteIncreaseSum = bucket.getAggregations().get("voteIncrease");
                StatisticResp resp = new StatisticResp();
                resp.setViewCount((int)viewCountSum.getValue());
                resp.setVoteCount((int)voteCountSum.getValue());
                resp.setViewIncrease((int)viewIncreaseSum.getValue());
                resp.setVoteIncrease((int)voteIncreaseSum.getValue());
                resp.setDate(DateUtils.parseDate(bucket.getKeyAsString(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                respArrayList.add(resp);
            }
            return respArrayList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Boolean putBulkEbookSnapshotEs(List<EbookSnapshot> ebookSnapshotList) {
        RestHighLevelClient client = null;
        try {
            client = elasticsearchClient.getRestHighLevelClient();
            BulkRequest request = new BulkRequest();
            request.timeout("2s");

            ObjectMapper mapper = new ObjectMapper();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (EbookSnapshot ebookSnapshot : ebookSnapshotList) {
                //时间戳和书本id进行Hash取值确定id
                String id = ((Integer) (ebookSnapshot.getDate().hashCode() + ebookSnapshot.getEbookId().intValue())).hashCode() + "";
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.setDateFormat(format);
                String source = mapper.writeValueAsString(ebookSnapshot);
                request.add(new IndexRequest("ebooksnapshot").id(id).source(source, XContentType.JSON));
            }
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            return bulk.hasFailures();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public Boolean delBulkEbookSnapshotEs(List<EbookSnapshot> ebookSnapshotList) {
        RestHighLevelClient client = null;
        try {
            client = elasticsearchClient.getRestHighLevelClient();
            BulkRequest request = new BulkRequest();
            request.timeout("2s");
            //默认每一秒刷新一次索引,将最近一秒产生的数据刷新到索引
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

            for (EbookSnapshot ebookSnapshot : ebookSnapshotList) {
                //时间戳和书本id进行Hash取值确定id
                String id = ((Integer) (ebookSnapshot.getDate().hashCode() + ebookSnapshot.getEbookId().intValue())).hashCode() + "";
                request.add(new DeleteRequest("ebooksnapshot").id(id));
            }
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            return bulk.hasFailures();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
