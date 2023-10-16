package com.cofeeshoporder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshoporder.dto.ReservedOrderDTO;
import com.cofeeshoporder.dto.response.GenericApiResponse;
import com.cofeeshoporder.dto.response.GenericCreateApiResponse;
import com.cofeeshoporder.service.OrderService;

@RequestMapping("/customer/order")
@RestController
public class CustomerOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public GenericApiResponse placeNewOrder(@RequestBody ReservedOrderDTO reservedOrderDTO) {
        Long newOrderId = orderService.placeOrder(reservedOrderDTO);
        return GenericApiResponse.builder().data(GenericCreateApiResponse.builder().id(newOrderId).build()).build();
    }

    @DeleteMapping("/{orderId}")
    public GenericApiResponse cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @GetMapping("/{orderId}")
    public GenericApiResponse getOrderDetail(@PathVariable Long orderId) {
        return GenericApiResponse.builder().data(orderService.getOrderDetail(orderId)).build();
    }
    
}