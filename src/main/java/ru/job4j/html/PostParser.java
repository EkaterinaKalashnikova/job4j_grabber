package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PostParser {
    public String[] loadAdDetails(String url) throws IOException {
        /*String url = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";*/
        Document document = Jsoup.connect(url).get();
        Element el = document.getElementsByAttributeValue("class", "messageHeader").get(0);
        String name = el.ownText();
        /*System.out.println(name);*/
        Element element = document.getElementsByAttributeValue("class", "msgFooter").first();
        String s = element.ownText().substring(0, element.ownText().indexOf("["));
       /*System.out.println(s);*/
        String[] str = new String[4];
        Elements element1 = document.select(".msgBody");
        Element element2 = element1.get(0).child(0);
       /*System.out.println(element2);*/
        String link = element2.attr("href");
        Element element3 = element1.get(1);
        String text = element3.text();
        /*System.out.println(text);*/
        str[0] = name;
        str[1] = link;
        str[2] = text;
        str[3] = s;
        return str;
    }
}



 /*Elements footer = document.select(".msgFooter");
        System.out.println(footer.first().childNodes().get(0));

         Elements footer = document.getElementsByAttributeValue("class", "msgFooter").first();
       for (Node node :footer.childNodes()){
            if (node instanceof TextNode){
                System.out.println(((TextNode)node).text());
            }
        }*/

 /* Elements column = document.select(".msgFooter");
        for (Element td : column) {
            Element date = td.parent();
           System.out.println(date.attr("td.msgFooter"));
           System.out.println(date.attr("#text"));
           System.out.println(date.text());

        }*/