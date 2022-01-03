package io.github.dantegrek.jplay;

class JsStrings {
    static final String JS_PSEUDO_ELEMENT_CONTENT = "getComputedStyle(document.querySelector('%s'), '%s').content";
    static final String JS_PSEUDO_ELEMENT_PROPERTY = "getComputedStyle(document.querySelector('%s'),'%s').getPropertyValue('%s')";

    private JsStrings() {
    }
}
