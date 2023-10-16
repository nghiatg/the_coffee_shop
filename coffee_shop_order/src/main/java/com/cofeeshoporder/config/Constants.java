package com.cofeeshoporder.config;

public class Constants {

    // Default response 
    public static String DEFAULT_SUCCESS_RESPONSE = "Successful";

    // Error message
    public static String NOT_ENOUGH_PERMISSION = "Not enough permission";
    public static String SHOP_CHAIN_NOT_FOUND = "Shop-chain not found";
    public static String SHOP_NOT_FOUND = "Shop not found";
    public static String DISH_NOT_FOUND = "Dish not found";
    public static String CONTACT_NOT_FOUND = "Contact not found";
    public static String ORDER_QUEUE_NOT_FOUND = "Order-queue not found";
    public static String ORDER_NOT_FOUND = "Order not found";

    
    public static String SHOP_CHAIN_ALREADY_DELETED = "Shop-chain not found";
    public static String SHOP_ALREADY_DELETED = "Shop was deleted already";
    public static String DISH_ALREADY_DELETED = "Dish was deleted already";
    public static String CONTACT_ALREADY_DELETED = "Contact was deleted already";
    public static String ORDER_QUEUE_ALREADY_DELETED = "Order-queue was deleted already";

    
    public static String ORDER_QUEUE_IS_FULL = "Order-queue is already full";

    public static String ORDER_STATUS_INVALID = "Order's status is invalid for action";
    public static String SHOP_NOT_BELONG_TO_SHOP_CHAIN = "Shop doesn't belong to shop-chain";
    public static String CANNOT_DELETE_BECAUSE_EXIST_RESERVED_ORDER = "Cannot remove shop because shop is still having reserved orders";

    
    public static long TOKEN_BLACKLIST_TTL_SECOND = 30;

    public static String TOKEN_BLACKLIST_VALUE = "BLACKLISTED";

    public static String AUTHORIZATION_HEADER = "Authorization";
    
}
