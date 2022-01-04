package io.github.dantegrek.interfaces;

/**
 * This interface represents all required options to emulate mobile browser.
 */
public interface Device {

    /**
     * Browser user agent
     * @return user agent as String
     */
    String getUserAgent();

    /**
     *
     * @return Viewport Height as int
     */
    int getViewportHeight();

    /**
     *
     * @return Viewport Width as int
     */
    int getViewportWidth();

    /**
     *
     * @return Device Scale Factor as int
     */
    int getDeviceScaleFactor();

    /**
     *
     * @return is mobile as boolean
     */
    boolean isMobile();

    /**
     *
     * @return has touch as boolean
     */
    boolean hasTouch();

    /**
     *
     * @return Device Name as String
     */
    String getDeviceName();
}
