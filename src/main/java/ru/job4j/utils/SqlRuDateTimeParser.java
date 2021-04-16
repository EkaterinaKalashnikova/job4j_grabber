package ru.job4j.utils;

import ru.job4j.utils.DateTimeParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SqlRuDateTimeParser implements DateTimeParser {
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
        String[] times = time.split(":");
        return Integer.parseInt(times[0]);
    }

    private int getMinute(String time) {
        String[] times = time.split(":");
        return Integer.parseInt(times[1].trim());
    }
}

