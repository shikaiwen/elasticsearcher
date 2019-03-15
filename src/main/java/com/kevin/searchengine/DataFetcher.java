package com.kevin.searchengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DataFetcher {

    public static void main(String[] args) throws Exception{
        getResultByKeywords("炊飯器");
    }

    static List<Map> getResultByKeywords(String keywords) {

        String serverDomain = "https://www.yodobashi.com/";
//        %E7%82%8A%E9%A3%AF%E5%99%A8
        String utf8Str = null;
        try {
            utf8Str = URLEncoder.encode(keywords, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String listUrl = "https://www.yodobashi.com/?word="+utf8Str;

        List<Map> rootList = new ArrayList();

        Document doc = null;
        try {
            doc = Jsoup.connect(listUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
            return rootList;
        }
//        System.out.println(doc.title());
//        Elements newsHeadlines = doc.select("#mp-itn b a");



        Elements elts = doc.select("#listContents > div.spt_hznList.tileTypeList");
        if(elts.size() > 0){
            Element listRootElt = elts.get(0);

            Elements children= listRootElt.select("div.pListBlock");
            for (Element child : children) {

                String href = child.select("a.js_productListPostTag").attr("href");

                String detailUrl = serverDomain + href;


                String proDesc = child.select(".pName").text();
                String imgUrl = child.select(".pImg img").attr("src");
                String priceStr = child.select(".pInfo .productPrice").text();


                Map map = new LinkedHashMap();
                map.put("proDesc", proDesc);
                map.put("imgUrl", imgUrl);
                map.put("priceStr", priceStr);
                map.put("detailUrl", detailUrl);
                rootList.add(map);
//                if(rootList.size()>2){
//                    break;
//                }
            }
        }


        return rootList;


//        for (Element headline : newsHeadlines) {
//            System.out.println(
//                    headline.attr("title"));
//        }

//        String detailUrl = doc.select("#listContents > div.spt_hznList.tileTypeList.mob_tileTypeList.areaLimitationTileMove.js_productList > div:nth-child(1) > div > a").get(0).attr("href");

    }
}
