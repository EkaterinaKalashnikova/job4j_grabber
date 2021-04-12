package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 6; i++) {
            String url = "https://www.sql.ru/forum/job-offers" + "/" + i;
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            Elements elements = doc.select(".altCol");
            int dateIndex = 1;
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(elements.get(dateIndex).text());
                dateIndex += 2;
            }
        }
        PostParser pp = new PostParser();
        String created_data = pp.loadAdDetails();
        System.out.println(created_data);
    }
}


