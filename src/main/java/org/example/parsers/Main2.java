package org.example.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main2 {
    public static void main(String[] args) {
        Document doc;
        try {
            doc = Jsoup.connect("https://www.mobafire.com/league-of-legends/items").get();
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong accessing the site");
        }

        Elements elements = doc.select("div.comments");
        for (Element el : elements) {
            System.out.println(el.select("img").attr("alt"));
        }
        // TODO: save in file
        //
    }
}
