package com.cofeeshoporder.client;

import static com.cofeeshoporder.config.Constants.AUTHORIZATION_HEADER;
import static org.springframework.http.HttpMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cofeeshoporder.dto.DishDTO;
import com.cofeeshoporder.dto.ReservedOrderQueueDTO;
import com.cofeeshoporder.dto.response.CoffeeShopClientResponse;
import com.cofeeshoporder.exception.ClientException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CoffeeShopMngClient {

    @Value("${coffeeshopmng.host}")
    private String coffeeshopmngHost;

    @Value("${coffeeshopmng.port}")
    private String coffeeshopmngPort;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_GET_DISH_INFO = "http://#COFFEESHOPMNG_HOST:#COFFEESHOPMNG_PORT/coffee_shop_management/dish-internal/#DISH_ID";

    private static final String API_GET_ORDER_QUEUE_INFO = "http://#COFFEESHOPMNG_HOST:#COFFEESHOPMNG_PORT/coffee_shop_management/order-queue-internal/#ORDER_QUEUE_ID";

    private static final String API_GET_ALL_MANAGED_ORDER_QUEUE = "http://#COFFEESHOPMNG_HOST:#COFFEESHOPMNG_PORT/coffee_shop_management/order-queue/all-managed-queues";

    public DishDTO getDishInfo(Long dishId) {
        String url = API_GET_DISH_INFO.replace("#COFFEESHOPMNG_HOST", coffeeshopmngHost)
                                        .replace("#COFFEESHOPMNG_PORT", coffeeshopmngPort)
                                        .replace("#DISH_ID", String.valueOf(dishId));
        log.info("Start to get dish info for " + dishId + " by " + url);
        HttpServletRequest currentHttpRequest = 
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, currentHttpRequest.getHeader(AUTHORIZATION_HEADER));
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            String apiResponse = restTemplate.exchange(url, GET, requestEntity, String.class).getBody();
            TypeReference<CoffeeShopClientResponse<DishDTO>> typeRef = new TypeReference<CoffeeShopClientResponse<DishDTO>>() {};
            return objectMapper.readValue(apiResponse, typeRef).getData();
        } catch(Exception e) {
            throw new ClientException(e.getMessage());
        }
    }

    public ReservedOrderQueueDTO getOrderQueueInfo(Long queueId) {
        log.info("Start to get queue info for " + queueId);
        String url = API_GET_ORDER_QUEUE_INFO.replace("#COFFEESHOPMNG_HOST", coffeeshopmngHost)
                                        .replace("#COFFEESHOPMNG_PORT", coffeeshopmngPort)
                                        .replace("#ORDER_QUEUE_ID", String.valueOf(queueId));
        HttpServletRequest currentHttpRequest = 
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, currentHttpRequest.getHeader(AUTHORIZATION_HEADER));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            String apiResponse = restTemplate.exchange(url, GET, requestEntity, String.class).getBody();
            TypeReference<CoffeeShopClientResponse<ReservedOrderQueueDTO>> typeRef = new TypeReference<CoffeeShopClientResponse<ReservedOrderQueueDTO>>() {};
            return objectMapper.readValue(apiResponse, typeRef).getData();
        } catch(Exception e) {
            throw new ClientException(e.getMessage());
        }
    }

    public ReservedOrderQueueDTO[] getAllManagedQueues() {
        String url = API_GET_ALL_MANAGED_ORDER_QUEUE.replace("#COFFEESHOPMNG_HOST", coffeeshopmngHost)
                                        .replace("#COFFEESHOPMNG_PORT", coffeeshopmngPort);
        HttpServletRequest currentHttpRequest = 
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, currentHttpRequest.getHeader(AUTHORIZATION_HEADER));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            String apiResponse = restTemplate.exchange(url, GET, requestEntity, String.class).getBody();
            TypeReference<CoffeeShopClientResponse<ReservedOrderQueueDTO[]>> typeRef = 
                    new TypeReference<CoffeeShopClientResponse<ReservedOrderQueueDTO[]>>() {};
            return objectMapper.readValue(apiResponse, typeRef).getData();
        } catch(Exception e) {
            throw new ClientException(e.getMessage());
        }
    }
    
}
