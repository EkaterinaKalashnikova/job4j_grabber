package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PostParser {
    public String loadAdDetails() throws IOException {
        String url = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        Document document = Jsoup.connect(url).get();
        /*Elements footer = document.select(".msgFooter");
        System.out.println(footer.first().childNodes().get(0));

         Elements footer = document.getElementsByAttributeValue("class", "msgFooter").first();
       for (Node node :footer.childNodes()){
            if (node instanceof TextNode){
                System.out.println(((TextNode)node).text());
            }
        }*/

        Element link = document.getElementsByAttributeValue("class", "msgFooter").first();
        String s = link.ownText().substring(0, 16);
        System.out.println(link.ownText().substring(0, 16));

      return url;
    }
}





 /* Elements column = document.select(".msgFooter");
        for (Element td : column) {
            Element date = td.parent();
           System.out.println(date.attr("td.msgFooter"));
           System.out.println(date.attr("#text"));
           System.out.println(date.text());

        }*/