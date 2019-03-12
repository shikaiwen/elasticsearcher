package com.kevin.searchengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataFetcher {

    public static void main(String[] args) throws Exception{
        t1();
    }

    static void t1() throws Exception{

        String listUrl = "https://www.yodobashi.com/?word=%E7%82%8A%E9%A3%AF%E5%99%A8";
        Document doc = Jsoup.connect(listUrl).get();
        System.out.println(doc.title());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(
                    headline.attr("title"));
        }

        String detailUrl = doc.select("#listContents > div.spt_hznList.tileTypeList.mob_tileTypeList.areaLimitationTileMove.js_productList > div:nth-child(1) > div > a").get(0).attr("href");

    }
}
