package com.jplay.screenplay;

import com.microsoft.playwright.Page;

class AbstractActivity {
    protected Actor actor;
    protected Page page;

    void setActor(Actor actor) {
        this.actor = actor;
    }

    void setPage(Page page) {
        this.page = page;
    }
}
