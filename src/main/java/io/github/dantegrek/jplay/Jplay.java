package io.github.dantegrek.jplay;

import static io.github.dantegrek.jplay.Actor.actor;

/**
 * This class contain only static methods, each of them returns instance of Actor.
 * All those methods are just syntax sugar for BDD style of tests.
 */
public final class Jplay {
    private Jplay() {
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static final Actor given() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static final Actor when() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static final Actor then() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static final Actor and() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static final Actor user() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static final Actor jPlay() {
        return actor();
    }
}
