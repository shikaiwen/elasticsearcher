package com.kevin.searchengine;


import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

//https://blog.csdn.net/weixin_36564655/article/details/79635896



//https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-search.html

/**
 * simple example code
 * https://www.baeldung.com/elasticsearch-java
 */

public class EsMain {

    static Client client = null;


    static {
        try {
            client = new PreBuiltTransportClient(
                    Settings.builder().put("client.transport.sniff", true)
                            .put("cluster.name","elasticsearch").build())
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static Client getClient(){
        try {
            Client client = new PreBuiltTransportClient(
                    Settings.builder().put("client.transport.sniff", true)
                            .put("cluster.name","elasticsearch").build())
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            System.out.println(client);
            return client;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String indexName = "mall";
    static String indexType = "product";


    public static void main(String[] args) {

        initIndex();
//        String[] arr = new String[]{"ドライヤー", "洋服"};
//        String[] arr = new String[]{"ドライヤー"};
//        makeIndex(arr, null);
//        query("炊く");
    }

    public static void initIndex(){
        Client client = getClient();

//        client.prepareBulk().add().
//        index.mapping.single_type :false

        //        jsonBuilder().startObject().endObject();
        XContentBuilder builder = null;
        try {

            builder = jsonBuilder()
                    .startObject()
                    .startObject("analysis")
                    .startObject("analyzer")
                    .startObject("my_ja_analyzer")
                    .field("type", "custom")
                    .field("tokenizer", "kuromoji_tokenizer")
                    .field("filter", new String[]{"kuromoji_baseform", "kuromoji_part_of_speech", "ja_stop","kuromoji_number","kuromoji_stemmer"})
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        CreateIndexResponse re = client.admin().indices().prepareCreate(indexName)
                .setSettings(Settings.builder().loadFromSource(
                        Strings.toString(builder),XContentType.JSON
                ))
//                    .setSource(json, XContentType.JSON)
                .execute().actionGet();
    }


    public static int makeIndex(String[] keywordArr, HttpServletRequest request){

        int total = 0;
        for (String keyword : keywordArr) {
            total += indexByKeyword(keyword);
        }
        return total;
    }


    public static List<Map> getProgress(HttpServletRequest request){

        return null;

    }


    public static List<Map> query(String keyword){

//        String keyword = "タイガー";
//        String keyword = "炊飯器";
//        String keyword = "炊く";

        Client client = null;
        if ((client = getClient()) == null) {
            throw new RuntimeException("can not create elasticsearch ");
        }

        SearchResponse response =
                client.prepareSearch()
                .setTypes()
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.multiMatchQuery(keyword,"proDesc"))
                 .setFrom(0).setSize(100)
                        .highlighter(new HighlightBuilder().field("proDesc").preTags("<em>").postTags("</em>")
                        )
//                .setPostFilter(QueryBuilders.rangeQuery("age").from(5).to(15))
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();

        List<Map> result = new ArrayList<>();

        hits.forEach(h->{
            Map<String,Object> map = h.getSourceAsMap();
            result.add(map);
            Map<String,HighlightField> hightF = h.getHighlightFields();
            hightF.forEach((key,val)->{
//                System.out.println(val.getFragments());

                Text[] commText = val.getFragments();
                if (commText != null && commText.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    Arrays.stream(commText).forEach(text->{
                        sb.append(text.toString());
                    });
                    map.put(key, sb.toString());
                }


            });
//            System.out.println(JSON.toJSONString(map));
        });

        return result;
    }


    public static int indexByKeyword(String keyword) {


        List<Map> dataList = DataFetcher.getResultByKeywords(keyword);

        for (Map data : dataList) {
            String json = JSON.toJSONString(data);


            IndexResponse response = client.prepareIndex(indexName, indexType)
                    .setSource(json, XContentType.JSON)
                    .get();

//            client.prepareIndex(indexName, indexType)
//                    .setSource(json, XContentType.JSON)
            System.out.println(response);
        }

        return dataList.size();


//        String id = response.getId();
//        String index = response.getIndex();
//        String type = response.getType();
//        long version = response.getVersion();

    }
}
