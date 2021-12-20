package com.playwright.screenplay.unit.actor.tasks;

import com.playwright.screenplay.Task;

public class TestTask extends Task {

    public TestTask withName(String name) {
        return this;
    }

    @Override
    public void perform() {
        //do your staff...
    }

    public static TestTask testTask() {
        return new TestTask();
    }
}
