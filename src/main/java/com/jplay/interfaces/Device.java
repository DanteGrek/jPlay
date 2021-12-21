package com.jplay.interfaces;

public interface Device {

    String getUserAgent();

    int getViewportHeight();

    int getViewportWidth();

    int getDeviceScaleFactor();

    boolean isMobile();

    boolean hasTouch();

    String getDeviceName();
}
