package com.cofeeshoporder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshoporder.dto.request.OrderHistoryRequest;
import com.cofeeshoporder.dto.response.GenericApiResponse;
import com.cofeeshoporder.service.OrderService;

@RequestMapping("/manager/order")
@RestController
public class ManagerOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order-queue/{orderQueueId}")
    public GenericApiResponse getOrderDetail(@PathVariable Long orderQueueId) {
        return GenericApiResponse.builder().data(orderService.getQueueOrder(orderQueueId)).build();
    }

    @PutMapping("/{orderId}/progress")
    public GenericApiResponse processOrder(@PathVariable Long orderId) {
        orderService.processOrder(orderId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @PutMapping("/{orderId}/finish")
    public GenericApiResponse finishOrder(@PathVariable Long orderId) {
        orderService.finishOrder(orderId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @PostMapping("/history")
    public GenericApiResponse getOrderHistoryOfUser(@RequestBody OrderHistoryRequest orderHistoryRequest) {
        return GenericApiResponse.builder().data(orderService.getOrderHistoryOfUser(orderHistoryRequest)).build();
    }
    
}