package com.cofeeshop.controller.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.service.ReservedOrderQueueService;

@RequestMapping("/order-queue-internal")
@RestController
public class ReservedOrderQueuePublicController {

    @Autowired
    private ReservedOrderQueueService reservedOrderQueueService;

    @GetMapping("/{orderQueueId}")
    public GenericApiResponse getOrderQueueInfo(@PathVariable Long orderQueueId) {
        return GenericApiResponse.builder().data(reservedOrderQueueService.getOrderQueue(orderQueueId)).build();
    }
    
}