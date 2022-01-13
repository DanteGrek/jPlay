package io.github.dantegrek.jplay;

import static io.github.dantegrek.jplay.Actor.actor;

/**
 * This class contain only static methods, each of them returns instance of Actor.
 * All those methods are just syntax sugar for BDD style of tests.
 */
public final class Jplay {
    private Jplay() {
    }

    // Synonyms of Actor.

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor given() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor given(Task task) {
        return actor().attemptTo(task);
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor when() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor when(Task task) {
        return actor().attemptTo(task);
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor then() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor then(Task task) {
        return actor().attemptTo(task);
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor and() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor and(Task task) {
        return actor().attemptTo(task);
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor user() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor user(Task task) {
        return actor().attemptTo(task);
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor jPlay() {
        return actor();
    }

    /**
     * BDD syntax sugar
     * @return instance of Actor
     */
    public static Actor jPlay(Task task) {
        return actor().attemptTo(task);
    }

}
