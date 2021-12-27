package io.github.dantegrek;

import io.github.dantegrek.actions.TestAction;
import org.junit.jupiter.api.Test;

import static io.github.dantegrek.screenplay.Actor.actor;
import static io.github.dantegrek.tasks.TestTask.testTask;

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
