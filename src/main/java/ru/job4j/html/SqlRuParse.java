package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.utils.SqlRuDateTimeParser;
import ru.job4j.model.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
   /* private static final Logger LOG =  LoggerFactory.getLogger(SqlRuParse.class.getName());
   private String url;
   private SqlRuParse(String url) {
        this.url = url;
   }*/

    public SqlRuParse() {

    }

    public static void main(String[] args) throws Exception {
            SqlRuParse sqlRuParse = new SqlRuParse();
            for (int i = 1; i < 6; i++) {
                List<Post> posts = sqlRuParse.list("https://www.sql.ru/forum/job-offers/" + i);
                 System.out.println(posts);
                posts.forEach(System.out::println);
           }
        }

        @Override
        public List<Post> list(String link) throws IOException {
            List<Post> listPost = new ArrayList<>();
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                /*System.out.println(href.text());
                 String name = href.text();*/
                String linkName = href.attr("href");
                Post post = detail(linkName);
                /*post.setName(name);*/
                post.setLink(linkName);
                listPost.add(post);
            }
            return listPost; /*список из вакансий , делает вызов detail*/
        }

        @Override
        public Post detail(String link) throws IOException {
            Post post = new Post();
            PostParser pp = new PostParser();
            String[] data = pp.loadAdDetails(link);
            post.setName(data[0]);
            post.setLink(data[1]);
            post.setText(data[2]);
            SqlRuDateTimeParser stm = new SqlRuDateTimeParser();
            LocalDateTime createDate = stm.parse(data[3]);
            post.setCreateData(createDate);
            return post; /*объект пост*/
        }
    }

  /*   for (int i = 1; i < 6; i++) {
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
      System.out.println("\n");
        PostParser pp = new PostParser();
        String url = "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        String[] created_data = pp.loadAdDetails(url);
        System.out.println(Arrays.toString(created_data));*/


