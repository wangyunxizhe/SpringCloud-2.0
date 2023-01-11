package com.wy.es;

import com.alibaba.fastjson.JSON;
import com.wy.es.pojo.Hotel;
import com.wy.es.pojo.HotelDoc;
import com.wy.es.service.IHotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class HotelDocumentTest {

    @Autowired
    private IHotelService hotelService;

    private RestHighLevelClient client;

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.68.132:9200")));
    }

    @AfterEach
    void tearDown() throws IOException {
        this.client.close();
    }

    //添加ES文档
    @Test
    void testAddDoc() throws IOException {
        //根据id去MySQL查询酒店的数据
        Hotel hotel = hotelService.getById(61083L);
        //转换为ES文档类型
        HotelDoc hotelDoc = new HotelDoc(hotel);

        //将MySQL中的数据添加到ES中
        //1，准备request对象
        IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
        //2，准备JSON格式文档
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        //3，发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    //查询ES文档
    @Test
    void testGetDocById() throws IOException {
        //1，准备request对象
        GetRequest request = new GetRequest("hotel").id("61083");
        //2，发送请求，得到响应
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //3，解析响应结果
        String json = response.getSourceAsString();

        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        System.err.println(hotelDoc);
    }

    //更新ES文档（局部更新）
    @Test
    void testUpdateDoc() throws IOException {
        //1，准备request对象
        UpdateRequest request = new UpdateRequest("hotel", "61083");
        //2，准备请求参数
        request.doc(
                "price", "952",
                "starName", "四钻"
        );
        //3，发送请求
        client.update(request, RequestOptions.DEFAULT);
    }

    //删除ES文档
    @Test
    void testDeleteDoc() throws IOException {
        //1，准备request对象
        DeleteRequest request = new DeleteRequest("hotel", "61083");
        //2，发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }

    //批量添加ES文档
    @Test
    void testBulk() throws IOException {
        //从MySQL中查询出全部数据
        List<Hotel> hotels = hotelService.list();

        //1，准备request对象
        BulkRequest request = new BulkRequest();
        //2，准备请求参数，添加多个新增的request
        for (Hotel hotel : hotels) {
            //将Hotel对象转换为适配ES的HotelDoc对象
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel").id(hotel.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        //3，发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }

}
