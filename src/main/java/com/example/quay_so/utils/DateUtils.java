package com.example.quay_so.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date convertStringToDate(String dateString) {
        // Định dạng ngày tháng năm
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }
    public static String formatDate(Date date) {
        // Định dạng ngày tháng năm
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Chuyển đổi đối tượng Date thành chuỗi ngày
        return dateFormat.format(date);
    }
}
