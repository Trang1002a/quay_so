package com.example.quay_so.presentation.controller.admin;

import com.example.quay_so.model.request.orders.*;
import com.example.quay_so.model.request.orders.*;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.presentation.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @PostMapping("/getAll")
    public GetAllOrderResponse getAll(@RequestBody GetAllOrderRequest request) {
        return orderService.getAll(request);
    }

    @GetMapping("/{id}")
    public OrderDetailsResponse findById(@PathVariable("id") String id) {
        return orderService.findById(id);
    }

    @PostMapping("/updateStatus")
    public ResStatus updateStatus(@RequestBody UpdateStatusOrderRequest request) {
        return orderService.updateStatus(request);
    }

    @GetMapping("/total")
    public OrderTotalResponse total() {
        return orderService.total();
    }
}
