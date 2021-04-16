package ru.job4j.grabber;

import org.junit.Test;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;

public class SqlRuDateTimeParserTest {

    @Test
    public void parseWhenRuDateTime() {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        LocalDateTime time = sqlRuDateTimeParser.parse("вчера, 19:23");
        LocalDateTime time1 = sqlRuDateTimeParser.parse("2 дек 19, 22:29");
        LocalDateTime time2 = sqlRuDateTimeParser.parse("25 июн 20, 21:56");
        System.out.println(time);
        System.out.println(time1);
        System.out.println(time2);
    }
}