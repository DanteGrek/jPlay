package io.github.dantegrek.interfaces;

/**
 * This interface represents all required options to emulate mobile browser.
 * Users can build custom enums with mobile settings based on this interface.
 */
public interface Device {
    /**
     * Getter
     * @return browser user agent
     */
    String getUserAgent();

    /**
     * Getter
     * @return viewport height
     */
    int getViewportHeight();

    /**
     * Getter
     * @return viewport width
     */
    int getViewportWidth();

    /**
     * Getter
     * @return device scale factor
     */
    int getDeviceScaleFactor();

    /**
     * Getter
     * @return true if mobile
     */
    boolean isMobile();

    /**
     * Getter
     * @return true if device has touch screen
     */
    boolean hasTouch();

    /**
     * Getter
     * @return device name
     */
    String getDeviceName();
}
