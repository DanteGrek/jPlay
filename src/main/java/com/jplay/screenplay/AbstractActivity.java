package com.jplay.screenplay;

import com.microsoft.playwright.Page;

class AbstractActivity {
    protected Actor actor;
    protected Page p;

    void setActor(Actor actor) {
        this.actor = actor;
        this.p = this.actor.currentPage();
    }

}
