package com.wy.es;

import com.wy.es.constants.HotelConstants;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HotelIndexTest {

    private RestHighLevelClient client;

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.68.132:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }

    @Test
    void testInit() {
        System.out.println(client);
    }

    //创建ES索引库
    @Test
    void testCreateHotelIndex() throws IOException {
        //1，创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        //2，请求参数：DSL语句
        request.source(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
        //3，发起请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    //删除ES索引库
    @Test
    void testDeleteHotelIndex() throws IOException {
        //1，创建Request对象
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        //2，发起请求
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    //查询ES索引库是否存在
    @Test
    void testExistsHotelIndex() throws IOException {
        //1，创建Request对象
        GetIndexRequest request = new GetIndexRequest("hotel");
        //2，发起请求
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.err.println(exists ? "索引库已经存在" : "索引库不存在");
    }

}