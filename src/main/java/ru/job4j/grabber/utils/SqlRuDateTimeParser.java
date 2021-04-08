package ru.job4j.grabber.utils;

//import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.logging.LogManager;
//import org.apache.log4j.LogManager;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public class SqlRuDateTimeParser implements DateTimeParser {
    private Logger LOG;

    public SqlRuDateTimeParser() {
        try {
            LogManager.getLogManager()
                    .readConfiguration(new FileInputStream("C:\\projects\\job4j_grabber\\src\\main\\resources\\rabbit.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG = Logger.getLogger(SqlRuDateTimeParser.class);
    }

    int year = 0;
    int month = 0;
    int dayOfMonth = 0;
    int hour = 0;
    int minute = 0;

    List<String> shortMonths = Arrays.asList(
            "янв", "фев", "мар", "апр", "май", "июн",
            "июл", "авг", "сен", "окт", "ноя", "дек");
    LocalDate ld;

    @Override
    public LocalDateTime parse(String parse) {
        LOG.info("вызван метод parse");
        parse = parse.replaceAll(",", "");
        if (parse.startsWith("вчера")) {
            ld = LocalDate.now().minusDays(1);
            String time = parse.substring(parse.indexOf(" ") + 1);
            hour = getHours(time);
            minute = getMinute(time);
        } else if (parse.startsWith("сегодня")) {
            ld = LocalDate.now();
            String time = parse.substring(parse.indexOf(" ") + 1);
            hour = getHours(time);
            minute = getMinute(time);
        } else {
            String[] dataNumber = parse.split(" ");
            dayOfMonth = Integer.parseInt(dataNumber[0]);
            month = shortMonths.indexOf(dataNumber[1]) + 1;
            year = Integer.parseInt(20 + dataNumber[2]);
            ld = LocalDate.of(year, month, dayOfMonth);
            hour = getHours(dataNumber[3]);
            minute = getMinute(dataNumber[3]);
        }
        year = ld.getYear();
        month = ld.getMonthValue();
        dayOfMonth = ld.getDayOfMonth();

        LocalDateTime dateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
        return dateTime;
    }

    private int getHours(String time) {
        LOG.info("вызван метод getHours");
        String[] times = time.split(":");
        return Integer.parseInt(times[0]);
    }

    private int getMinute(String time) {
        LOG.info("вызван метод getMinute");
        String[] times = time.split(":");
        return Integer.parseInt(times[1]);
    }

    public static void main(String[] args) {
        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        LocalDateTime time = sqlRuDateTimeParser.parse("вчера, 19:23");
        LocalDateTime time1 = sqlRuDateTimeParser.parse("2 дек 19, 22:29");
        LocalDateTime time2 = sqlRuDateTimeParser.parse("25 июн 20, 21:56");
        System.out.println(time);
        System.out.println(time1);
        System.out.println(time2);
    }
}

