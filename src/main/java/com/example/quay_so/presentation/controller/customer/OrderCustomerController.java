package com.example.quay_so.presentation.controller.customer;


import com.example.quay_so.model.request.orders.CreateOrderRequest;
import com.example.quay_so.model.request.orders.GetAllOrderResponse;
import com.example.quay_so.model.request.orders.OrderDetailsResponse;
import com.example.quay_so.model.response.order.CreateOrderResponse;
import com.example.quay_so.presentation.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer/order")
public class OrderCustomerController {

    @Resource
    private OrderService orderService;

    @GetMapping("/getAll")
    public GetAllOrderResponse getAllOrder() {
        return orderService.getAllCustomerOrder();
    }

    @GetMapping("/details/{id}")
    public OrderDetailsResponse getOrderDetails(@PathVariable("id") String id) {
        return orderService.getCustomerOrderDetails(id);
    }

    @PostMapping ("/create")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.createOrder(createOrderRequest);
    }


}
