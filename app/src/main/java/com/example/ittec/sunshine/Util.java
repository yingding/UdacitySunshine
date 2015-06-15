package com.example.ittec.sunshine;

/**
 * Created by yingding on 15.06.15.
 */
public class Util {
    /**
     * 0 celsius = 32F, 100 celsius = 212F
     * @param celsius
     * @return
     */
    public static double CelsiusToFahrenheit(double celsius) {
        return celsius * (9.0 / 5) + 32.0;
    }
}
