package io.github.dantegrek.enums;

/**
 * This enum represents all keys on keyboard.
 */
public enum Key {

    /**
     * Key F1
     */
    F1("F1"),
    /**
     * Key F2
     */
    F2("F2"),
    /**
     * Key F3
     */
    F3("F3"),
    /**
     * Key F4
     */
    F4("F4"),
    /**
     * Key F5
     */
    F5("F5"),
    /**
     * Key F6
     */
    F6("F6"),
    /**
     * Key F7
     */
    F7("F7"),
    /**
     * Key F8
     */
    F8("F8"),
    /**
     * Key F9
     */
    F9("F9"),
    /**
     * Key F10
     */
    F10("F10"),
    /**
     * Key F11
     */
    F11("F11"),
    /**
     * Key F12
     */
    F12("F12"),
    /**
     * Key 0
     */
    DIGIT_0("Digit0"),
    /**
     * Key 1
     */
    DIGIT_1("Digit1"),
    /**
     * Key 2
     */
    DIGIT_2("Digit2"),
    /**
     * Key 3
     */
    DIGIT_3("Digit3"),
    /**
     * Key 4
     */
    DIGIT_4("Digit4"),
    /**
     * Key 5
     */
    DIGIT_5("Digit5"),
    /**
     * Key 6
     */
    DIGIT_6("Digit6"),
    /**
     * Key 7
     */
    DIGIT_7("Digit7"),
    /**
     * Key 8
     */
    DIGIT_8("Digit8"),
    /**
     * Key 9
     */
    DIGIT_9("Digit9"),
    /**
     * Key A
     */
    KEY_A("KeyA"),
    /**
     * Key B
     */
    KEY_B("KeyB"),
    /**
     * Key C
     */
    KEY_C("KeyC"),
    /**
     * Key D
     */
    KEY_D("KeyD"),
    /**
     * Key E
     */
    KEY_E("KeyE"),
    /**
     * Key F
     */
    KEY_F("KeyF"),
    /**
     * Key G
     */
    KEY_G("KeyG"),
    /**
     * Key H
     */
    KEY_H("KeyH"),
    /**
     * Key I
     */
    KEY_I("KeyI"),
    /**
     * Key J
     */
    KEY_J("KeyJ"),
    /**
     * Key K
     */
    KEY_K("KeyK"),
    /**
     * Key L
     */
    KEY_L("KeyL"),
    /**
     * Key M
     */
    KEY_M("KeyM"),
    /**
     * Key N
     */
    KEY_N("KeyN"),
    /**
     * Key O
     */
    KEY_O("KeyO"),
    /**
     * Key P
     */
    KEY_P("KeyP"),
    /**
     * Key Q
     */
    KEY_Q("KeyQ"),
    /**
     * Key R
     */
    KEY_R("KeyR"),
    /**
     * Key S
     */
    KEY_S("KeyS"),
    /**
     * Key T
     */
    KEY_T("KeyT"),
    /**
     * Key U
     */
    KEY_U("KeyU"),
    /**
     * Key V
     */
    KEY_V("KeyV"),
    /**
     * Key W
     */
    KEY_W("KeyW"),
    /**
     * Key X
     */
    KEY_X("KeyX"),
    /**
     * Key Y
     */
    KEY_Y("KeyY"),
    /**
     * Key Z
     */
    KEY_Z("KeyZ"),
    /**
     * Key ` or ~
     */
    BACK_QUOTE("Backquote"),
    /**
     * Key ' or "
     */
    QUOTE("Quote"),
    /**
     * Key - or _
     */
    MINUS("Minus"),
    /**
     * Key = or +
     */
    EQUAL("Equal"),
    /**
     * Key \ or |
     */
    BACKSLASH("Backslash"),
    /**
     * Key Backspace
     */
    BACKSPACE("Backspace"),
    /**
     * Key Tab
     */
    TAB("Tab"),
    /**
     * Key Delete
     */
    DELETE("Delete"),
    /**
     * Key Escape
     */
    ESCAPE("Escape"),
    /**
     * Key End
     */
    END("End"),
    /**
     * Key Enter
     */
    ENTER("Enter"),
    /**
     * Key Home
     */
    HOME("Home"),
    /**
     * Key Insert
     */
    INSERT("Insert"),
    /**
     * Key PageUp
     */
    PAGE_UP("PageUp"),
    /**
     * Key PageDown
     */
    PAGE_DOWN("PageDown"),
    /**
     * Key ArrowDown
     */
    ARROW_DOWN("ArrowDown"),
    /**
     * Key ArrowUp
     */
    ARROW_UP("ArrowUp"),
    /**
     * Key ArrowRight
     */
    ARROW_RIGHT("ArrowRight"),
    /**
     * Key ArrowLeft
     */
    ARROW_LEFT("ArrowLeft"),
    /**
     * Key Shift
     */
    SHIFT("Shift"),
    /**
     * Key Shift left
     */
    SHIFT_LEFT("ShiftLeft"),
    /**
     * Key Control
     */
    CONTROL("Control"),
    /**
     * Key Alt
     */
    ALT("Alt"),
    /**
     * Key Meta/Command
     */
    COMMAND("Meta"),
    /**
     * Key Meta/Command
     */
    META("Meta"),
    /**
     * Key " "
     */
    SPACE("Space"),
    /**
     * Key CapsLock
     */
    CAPS_LOCK("CapsLock"),
    /**
     * Key / or ?
     */
    SLASH("Slash"),
    /**
     * Key ,
     */
    COMMA("Comma"),
    /**
     * Key .
     */
    PERIOD("Period"),
    /**
     * Key ; or :
     */
    SEMICOLON("Semicolon"),
    /**
     * Key [ or {
     */
    BRACKET_LEFT("BracketLeft"),
    /**
     * Key ] or }
     */
    BRACKET_RIGHT("BracketRight");

    /**
     * Key code for raw playwright method.
     */
    public final String keyCode;

    Key(String key) {
        this.keyCode = key;
    }

}
