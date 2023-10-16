package com.coffeeshopauthentication.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeeshopauthentication.config.Constants;

import static com.coffeeshopauthentication.config.Constants.TOKEN_BLACKLIST_VALUE;
import static com.coffeeshopauthentication.config.Constants.TOKEN_BLACKLIST_TTL_SECOND;

@Component
public class TokenBlacklistUtils {
    @Autowired
    private RedisUtils redisUtils;

    public void addTokenToBlackList(String token) {
        redisUtils.setKey(token, TOKEN_BLACKLIST_VALUE, TOKEN_BLACKLIST_TTL_SECOND, TimeUnit.SECONDS);
    }

    public static String extractTokenFromBearerHeader(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }

    public boolean verifyTokenInBlackList(String token) {
        return Constants.TOKEN_BLACKLIST_VALUE.equals(redisUtils.get(token));
    }
}
