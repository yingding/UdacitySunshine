package com.example.ittec.sunshine;

/**
 * Created by yingding on 15.06.15.
 */
public interface FormatHighLowsInterface {
    /**
     * Prepare the weather high/lows for presentation.
     */
    String formatHighLows(double high, double low, String unitType);
}
