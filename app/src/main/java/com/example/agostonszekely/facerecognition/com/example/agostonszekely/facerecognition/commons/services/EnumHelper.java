package com.example.agostonszekely.facerecognition.com.example.agostonszekely.facerecognition.commons.services;

import java.security.SecureRandom;

/**
 * Created by agoston.szekely on 2016.10.21..
 */

public class EnumHelper {

    private static final SecureRandom random = new SecureRandom();

    public static <T extends Enum<?>> T getRandomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
