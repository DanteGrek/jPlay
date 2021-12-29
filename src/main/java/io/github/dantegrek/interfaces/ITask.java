package io.github.dantegrek.interfaces;

/**
 * This interface will make end user to implement actor tasks properly.
 */
public interface ITask {
    /**
     * This method is used by Actor class to execute end user tasks.
     */
    void perform();
}
