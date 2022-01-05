package io.github.dantegrek.jplay;

interface IConfigSwitcher {

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    Actor finished();

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    Actor configIsFinished();

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    Actor and();

    /**
     * Return you to actor invocation chain.
     *
     * @return instance of Actor
     */
    Actor andActor();
}
