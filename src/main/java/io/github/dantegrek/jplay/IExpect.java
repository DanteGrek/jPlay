package io.github.dantegrek.jplay;

/**
 * This interface is used in implementation of Expect as functional interface.
 */
interface IExpect {

    /**
     * Each check in Expect do only one job.
     */
    void doAssert();
}
