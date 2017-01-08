package com.jrodolfo.basichibernate.util;

import java.util.Random;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-08
 */
public class MathUtil {

    public static Long getRandonLong(long min, long max) {
        Random random = new Random();
        return min + ((long) (random.nextDouble() * (max - min)));
    }
}
