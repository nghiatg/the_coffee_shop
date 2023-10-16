package com.cofeeshop.controller.privatecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.ReservedOrderQueueDTO;
import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.service.ReservedOrderQueueService;

@RequestMapping("/order-queue")
@RestController
public class ReservedOrderQueueController {

    @Autowired
    private ReservedOrderQueueService reservedOrderQueueService;

    @GetMapping("/{orderQueueId}")
    public GenericApiResponse getOrderQueueInfo(@PathVariable Long orderQueueId) {
        return GenericApiResponse.builder().data(reservedOrderQueueService.getOrderQueue(orderQueueId)).build();
    }

    @PutMapping("/{orderQueueId}")
    public GenericApiResponse updateOrderQueueInfo(@PathVariable Long orderQueueId, @RequestBody ReservedOrderQueueDTO orderQueueDTO) {
        reservedOrderQueueService.updateOrderQueue(orderQueueId, orderQueueDTO);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @GetMapping("/all-managed-queues")
    public GenericApiResponse getAllQueueFromManagedShops() {
        return GenericApiResponse.builder().data(reservedOrderQueueService.listAllManagedQueue()).build();
    }
    
}