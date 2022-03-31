package com.example.appstraining.towermeasurement.util;

public class CustomMath {
    public static int getHypotenuse(int legOne, int legTwo) {
        return (int)Math.round(Math.sqrt(Math.pow(legOne, 2) + Math.pow(legTwo, 2)));
    }
}
