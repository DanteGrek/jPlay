package com.playwright.screenplay.unit.actor;

import org.junit.jupiter.api.Test;

import static com.playwright.screenplay.Actor.actor;
import static com.playwright.screenplay.unit.actor.actions.TestAction.testAction;
import static com.playwright.screenplay.unit.actor.tasks.TestTask.testTask;

public class ActionsTest {

    @Test
    public void attemptsToDoActionTest() {
        actor().attemptTo(testAction());
    }

    @Test
    public void attemptsToDoTaskTest() {
        actor().attemptTo(testTask().withName("Name"));
    }
}
