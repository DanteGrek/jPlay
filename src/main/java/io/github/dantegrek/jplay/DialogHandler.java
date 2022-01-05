package io.github.dantegrek.jplay;

import com.microsoft.playwright.Dialog;
import org.opentest4j.AssertionFailedError;

import java.util.function.Consumer;

/**
 * Dialogs are dismissed automatically, unless there is a listener: acceptAll or acceptOnce
 */
public final class DialogHandler {

    private Actor actor;
    private final String alert = "alert";
    private final String prompt = "prompt";
    private final String confirm = "confirm";

    DialogHandler(Actor actor) {
        this.actor = actor;
    }

    // Common

    /**
     * Accept any Dialog once.
     *
     * @return instance of Actor
     */
    public Actor acceptOnce() {
        this.actor.currentPage().onDialog(Dialog::accept);
        return this.actor;
    }

    /**
     * Accept all dialogs in current page/tab.
     *
     * @return instance of Actor
     */
    public Actor acceptAll() {
        this.actor.currentPage().onDialog(Dialog::accept);
        return this.actor;
    }

    // Confirms

    /**
     * Accept Confirm dialog once.
     *
     * @return instance of Actor
     */
    public Actor acceptConfirmOnce() {
        Consumer<Dialog> handler = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                if (dialog.type().equals(confirm)) {
                    dialog.accept();
                    actor.currentPage().offDialog(this);
                }
            }
        };
        this.actor.currentPage().onDialog(handler);
        return this.actor;
    }

    /**
     * Accept all Confirm dialogs in current page/tab.
     *
     * @return instance of Actor
     */
    public Actor acceptAllConfirms() {
        Consumer<Dialog> handler = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                if (dialog.type().equals(confirm)) {
                    dialog.accept();
                }
            }
        };
        this.actor.currentPage().onDialog(handler);
        return this.actor;
    }

    // Prompts

    /**
     * Accept Prompt dialog once.
     *
     * @return instance of Actor
     */
    public Actor acceptPromptOnce() {
        Consumer<Dialog> handler = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                if (dialog.type().equals(prompt)) {
                    dialog.accept();
                    actor.currentPage().offDialog(this);
                }
            }
        };
        this.actor.currentPage().onDialog(handler);
        return this.actor;
    }

    /**
     * Put answer and accept Prompt dialog once.
     *
     * @param text will ba pasted in to prompt field
     * @return instance of Actor
     */
    public Actor acceptPromptOnce(String text) {
        Consumer<Dialog> handler = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                if (dialog.type().equals(prompt)) {
                    dialog.accept(text);
                    actor.currentPage().offDialog(this);
                }
            }
        };
        this.actor.currentPage().onDialog(handler);
        return this.actor;
    }

    /**
     * Accept all prompts with default value or without any value.
     *
     * @return instance of Actor
     */
    public Actor acceptAllPrompts() {
        Consumer<Dialog> handler = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                if (dialog.type().equals(prompt)) {
                    dialog.accept();
                }
            }
        };
        this.actor.currentPage().onDialog(handler);
        return this.actor;
    }

    /**
     * Accept all prompts with text
     *
     * @param text text for prompts
     * @return instance of Actor
     */
    public Actor acceptAllPrompts(String text) {
        Consumer<Dialog> handler = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                if (dialog.type().equals(prompt)) {
                    dialog.accept(text);
                }
            }
        };
        this.actor.currentPage().onDialog(handler);
        return this.actor;
    }

    // Expects

    private void assertEqual(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionFailedError(
                    String.format("Unexpected dialog message: Actual: %s\nExpected: %s", actual, expected));
        }
    }

    /**
     * Ensure that next dialog has text and dismiss dialog.
     *
     * @param expectedMessage expected text in dialog
     * @return instance of Actor
     */
    public Actor expectThatMessageEqualAndDismiss(String expectedMessage) {
        this.actor.currentPage().onceDialog(dialog -> {
                    try {
                        assertEqual(dialog.message(), expectedMessage);
                    } finally {
                        dialog.dismiss();
                    }
                }
        );
        return this.actor;
    }

    /**
     * Ensure that next dialog has text and accept dialog.
     *
     * @param expectedMessage expected text in dialog
     * @return instance of Actor
     */
    public Actor expectThatMessageEqualAndAccept(String expectedMessage) {
        this.actor.currentPage().onceDialog(dialog -> {
                    try {
                        assertEqual(dialog.message(), expectedMessage);
                    } finally {
                        dialog.accept();
                    }
                }
        );
        return this.actor;
    }
}
