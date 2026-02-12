package com.veloland.backend.controller;

import com.veloland.backend.dto.OrderDto;
import com.veloland.backend.model.Order;
import com.veloland.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderDto request) {return orderService.createOrder(request);}
}
