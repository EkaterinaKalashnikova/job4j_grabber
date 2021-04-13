package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PostParser {
    public  String loadAdDetails() throws IOException {
        String url = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        Document document = Jsoup.connect(url).get();
        Elements column = document.select(".msgFooter");
        int index = column.size();
        for (Element td : column) {
            Element date = td.parent();
            System.out.println(date.attr("msgFooter"));
            System.out.println(date.text());
            //System.out.println(column.get(index).text());
           // index++;
        }
        return String.valueOf(column.text());
    }
}
