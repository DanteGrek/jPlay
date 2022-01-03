package io.github.dantegrek.jplay;

/**
 * This interface will make end user to implement actor tasks properly.
 */
interface ITask {
    /**
     * This method is used by Actor class to execute end user tasks.
     */
    void perform();
}
