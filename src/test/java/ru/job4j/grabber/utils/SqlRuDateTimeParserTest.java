package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

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