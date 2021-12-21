package com.jplay.tasks;

import com.jplay.screenplay.Task;

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
