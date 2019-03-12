package com.kevin.searchengine;


import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EsMain {

    static Client client;
    static {
        try {
            client = new PreBuiltTransportClient(
                    Settings.builder().put("client.transport.sniff", true)
                            .put("cluster.name","elasticsearch").build())
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            System.out.println(client);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        givenJsonString_whenJavaObject_thenIndexDocument();
    }


    public static void givenJsonString_whenJavaObject_thenIndexDocument() {
        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
                +"\"fullName\":\"John Doe\"}";
        IndexResponse response = client.prepareIndex("people", "Doe")
                .setSource(jsonObject, XContentType.JSON).get();

        String id = response.getId();
        String index = response.getIndex();
        String type = response.getType();
        long version = response.getVersion();

        System.out.println(version);
//        assertEquals(DocWriteResponse.Result.CREATED, response.getResult());
//        assertEquals(0, version);
//        assertEquals("people", index);
//        assertEquals("Doe", type);
    }
}
