package com.example.microservices.limitsservice.configuration;

public class LimitConfiguration {
    private int maximum;
    private int minimum;

    public LimitConfiguration() {
    }

    public LimitConfiguration(int maximum, int minimum) {
        this.maximum = maximum;
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }
}
