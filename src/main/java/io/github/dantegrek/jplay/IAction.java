package io.github.dantegrek.jplay;

/**
 * This interface provides end user with ability switch from Action chain back to actor invocation chain.
 */
interface IAction {
    /**
     * Return you to actor invocation chain.
     * @return instance of Actor
     */
    public Actor then();

    /**
     * Return you to actor invocation chain.
     * @return instance of Actor.
     */
    public Actor and();
}
