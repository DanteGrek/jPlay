package io.github.dantegrek.jplay;

/**
 * This class should be used as parent for all custom actions user wants to create, and(), then() methods
 * return user to actor invocation chain.
 */
public abstract class Action extends AbstractActivity implements IAction {

    /**
     * Return you to actor invocation chain.
     * @return instance of Actor for current thread.
     */
    @Override
    public Actor and() {
        return actor;
    }

    /**
     * Return you to actor invocation chain.
     * @return instance of Actor for current thread.
     */
    @Override
    public Actor then() {
        return actor;
    }

}
