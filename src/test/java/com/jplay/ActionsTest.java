package com.jplay;

import com.jplay.actions.TestAction;
import org.junit.jupiter.api.Test;

import static com.jplay.screenplay.Actor.actor;
import static com.jplay.tasks.TestTask.testTask;

public class ActionsTest {

    @Test
    public void attemptsToDoActionTest() {
        actor().attemptTo(TestAction.testAction());
    }

    @Test
    public void attemptsToDoTaskTest() {
        actor().attemptTo(testTask().withName("Name"));
    }
}
