package com.samuilolegovich.util;

import java.security.SecureRandom;

public class Generator {
    public static SecureRandom secureRandom = new SecureRandom();


    // генерирует число
    public static int generate() {
//        return (byte) ( Math.random() * 101 );
        return secureRandom.nextInt(101);
    }
}
