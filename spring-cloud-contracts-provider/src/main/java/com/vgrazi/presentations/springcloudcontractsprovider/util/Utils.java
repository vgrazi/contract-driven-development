package com.vgrazi.presentations.springcloudcontractsprovider.util;

public class Utils {
    /**
     * This method rounds the request, mostly for display purpposes
     * If the amount request is a multiple of 10,000 (supplied as the "to" parameter
     * for flexibility), returns that value
     * Otherwise,rounds up to the next 10,000 ("to")
     */
    public static double round(double value, int to) {
        if (value % to == 0) {
            return value;
        } else {
            // divide by 10,000
            // add 1
            value = (int) value / to + 1;
            // multiply by 10,000
            value = value * to;
            return value;
        }
    }
}
