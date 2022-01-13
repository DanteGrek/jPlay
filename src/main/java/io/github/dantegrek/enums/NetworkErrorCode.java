package io.github.dantegrek.enums;

/**
 * Aborts the network request.
 */
public enum NetworkErrorCode {
    /**
     * An operation was aborted (due to user action)
     */
    ABORTED("aborted"),
    /**
     * Permission to access a resource, other than the network, was denied
     */
    ACCESS_DENIED("accessdenied"),
    /**
     * The IP address is unreachable. This usually means that there is no route to the specified host or network.
     */
    ADDRESS_UNREACHABLE("addressunreachable"),
    /**
     * The client chose to block the request.
     */
    BLOCKED_BY_CLIENT("blockedbyclient"),
    /**
     * The request failed because the response was delivered along with requirements which are not met
     * ('X-Frame-Options' and 'Content-Security-Policy' ancestor checks, for instance).
     */
    BLOCKED_BY_RESPONSE("blockedbyresponse"),
    /**
     * A connection timed out as a result of not receiving an ACK for data sent.
     */
    CONNECTION_ABORTED("connectionaborted"),
    /**
     * A connection was closed (corresponding to a TCP FIN).
     */
    CONNECTION_CLOSED("connectionclosed"),
    /**
     * A connection attempt failed.
     */
    CONNECTION_FAILED("connectionfailed"),
    /**
     * A connection attempt was refused.
     */
    CONNECTION_REFUSED("connectionrefused"),
    /**
     * A connection was reset (corresponding to a TCP RST).
     */
    CONNECTION_RESET("connectionreset"),
    /**
     * The Internet connection has been lost.
     */
    INTERNET_DISCONNECTED("internetdisconnected"),
    /**
     * The host name could not be resolved.
     */
    NAME_NOT_RESOLVED("namenotresolved"),
    /**
     * An operation timed out.
     */
    TIMEOUT("timedout"),
    /**
     * A generic failure occurred.
     */
    FAILED("failed");

    public final String code;

    NetworkErrorCode(String code) {
        this.code = code;
    }
}
