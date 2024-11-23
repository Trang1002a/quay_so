package com.example.quay_so.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrdersStatus {
    WAITING_CONFIRM("Chờ xác nhận"),
    DELIVERING("Đang giao"),
    DELIVERED("Đã giao"),
    CANCEL("Đã hủy"),
    RETURNS("Trả hàng");

    private final String status;

    OrdersStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static boolean contains(String status) {
        return Arrays.stream(OrdersStatus.values())
                .anyMatch(ordersStatus -> StringUtils.equalsIgnoreCase(ordersStatus.toString(), status));
    }

    public static List<String> getAllStatus() {
        return Arrays.stream(OrdersStatus.values())
                .map(OrdersStatus::toString)
                .collect(Collectors.toList());
    }
}
