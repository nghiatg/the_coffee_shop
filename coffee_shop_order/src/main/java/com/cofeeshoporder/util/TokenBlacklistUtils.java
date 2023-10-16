package com.cofeeshoporder.util;

import static com.cofeeshoporder.config.Constants.TOKEN_BLACKLIST_TTL_SECOND;
import static com.cofeeshoporder.config.Constants.TOKEN_BLACKLIST_VALUE;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cofeeshoporder.config.Constants;

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
