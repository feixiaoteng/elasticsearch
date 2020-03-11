package com.fei.tools;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * 引入依赖
 * <dependency>
 * <groupId>org.elasticsearch.client</groupId>
 * <artifactId>transport</artifactId>
 * <version>6.2.4</version>
 * </dependency>
 * <p>
 * ElasticSearchUtil获取连接工具类
 */
public class ElasticSearchUtil {
    /**
     * 获取es连接
     * @param ip 服务ip地址
     * @param port 服务端口号
     */
    public static TransportClient client(String ip, int port) throws Exception {
        /**
         * cluster.name:在es安装目录/config/elasticsearch.yml中指定,默认的名称(elasticsearch)
         */
        return new PreBuiltTransportClient(Settings.builder().put("cluster.name", "elasticsearch").build()).addTransportAddress(new TransportAddress(InetAddress.getByName(ip), port));
    }

}
