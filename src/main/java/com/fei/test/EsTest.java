package com.fei.test;

import com.fei.tools.ElasticSearchUtil;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * @ProjectName: elasticsearch
 * @Package: com.fei.test
 * @ClassName: EsTest
 * @Author: feixiaoteng
 * @Date: 2020-03-11 15:36
 * @Version: 1.0.0
 * @Description: 测试好不好使
 */
public class EsTest {
    private static final String IP = "192.168.40.166";
    private static final Integer PORT = 9300;

    public static void main(String[] args) throws Exception {
        //addDocument();

        //deleteDocument();

        //updateDocument();

        //updateOrInsert();

        //multiGet();

    }
    /**
     * 批量获取
     */
    private static void multiGet() throws Exception {
        MultiGetResponse mgResponse = ElasticSearchUtil.client(IP, PORT).prepareMultiGet()
                .add("index-a", "users", "1", "2")
                .add("users-index", "users", "2", "3")
                .get();
        for (MultiGetItemResponse response : mgResponse) {
            GetResponse rp = response.getResponse();
            if (rp != null && rp.isExists()) {
                System.out.println(rp.getSourceAsString());
            }
        }
    }

    /**
     * 不存在就是新增,存在就是删除
     */
    private static void updateOrInsert() throws Exception {
        IndexRequest request1 = new IndexRequest()
                .source(
                        XContentFactory.jsonBuilder()
                                .startObject()
                                .field("name", "小黑")
                                .field("desc", "是一个高富帅")
                                .field("birthday", "2011-11-11")
                                .field("age", "100")
                                .endObject()
                );
        UpdateRequest request2 = new UpdateRequest("users-index", "users", "5")
                .doc(XContentFactory.jsonBuilder().startObject().field("desc", "是一个矮穷矬").endObject()).upsert(request1);
        UpdateResponse response = ElasticSearchUtil.client(IP, PORT).update(request2).get();
        System.out.println(response.status());
    }

    /**
     * 添加索引
     */
    private static void addDocument() throws Exception {
        XContentBuilder document = XContentFactory.jsonBuilder().startObject()
                .field("name", "代腾飞")
                .field("desc", "是一个帅哥")
                .field("birthday", "2010-10-10")
                .field("sex", "1")
                .field("age", "20")
                .endObject();
        IndexResponse response = ElasticSearchUtil.client(IP, PORT).prepareIndex("users-index", "users", "2")
                .setSource(document)
                .get();
        System.out.println(response.status());
    }

    /**
     * 更新文档(不会删除原有的字段)
     */
    private static void updateDocument() throws Exception {
        UpdateRequest request = new UpdateRequest();
        request.index("users-index")
                .type("users")
                .id("XjyFynAB83LleEnBYWjN")
                .doc(XContentFactory.jsonBuilder().startObject().field("name", "小红").endObject());
        System.out.println(ElasticSearchUtil.client(IP, PORT).update(request).get().status());
    }

    /**
     * 根据id删除文档
     */
    private static void deleteDocument() throws Exception {
        System.out.println(ElasticSearchUtil.client(IP, PORT).prepareDelete("users-index", "users", "2").get().status());
    }
}
