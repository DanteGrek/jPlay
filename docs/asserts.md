# Asserts

___

- [Locator asserts](#locatorAsserts)
    - [expectThat()](#expectThat)
    - [softExpectThat()](#softExpectThat)
    - [checkAll()](#checkAll)
    - [verifyAll()](#verifyAll)
    - [selector(String selector)](#selector)
    - [locator(Locator locator)](#locator)
    - [locators(Locator locator)](#locators)
    - [isVisible() & visible()](#isVisible)
    - [isHidden() & hidden()](#isHidden)
    - [isChecked() & checked()](#isChecked)
    - [isDisabled() & disabled()](#isDisabled)
- [Page asserts](#pageAsserts)
    - [page()](#page)
- [Dialog asserts](#dialogAsserts)
    - [expectThatMessageEqualAndDismiss(String expectedMessage)](#expectThatMessageEqualAndDismiss)
    - [expectThatMessageEqualAndAccept(String expectedMessage)](#expectThatMessageEqualAndAccept)

___
<h2 id="locatorAsserts">
    Locator asserts
</h2>

<h3 id="expectThat">
    expectThat()
</h3>
Returns instance of Expect(Asserts class), opens assert functionality.

```
  actor()
      .expectThat()
      ...
```

<h3 id="softExpectThat">
    softExpectThat()
</h3>

Returns instance of Expect(Asserts class), opens assert functionality. Perform checks but store assert errors instead of
throw them.

```
  actor()
      .softExpectThat()
      ...
```

<h3 id="checkAll">
    checkAll()
</h3>

Checks if soft assert stored any assert errors.

```
  actor()
      .softExpectThat()
      .selector("button")
      .isVisible()
      .isFocused()
      .isChecked()
      .checkAll();
```

<h3 id="verifyAll()">
    verifyAll()
</h3>
Synonym for checkAll() method.
Checks if soft assert stored any assert errors.

```
  actor()
      .softExpectThat()
      .selector("button")
      .isVisible()
      .isFocused()
      .verifyAll();
```

<h3 id="selector">
    selector(String selector)
</h3>

This method point all next checks in chain on selected web element. Method accepts any
valid [playwright selectors](https://playwright.dev/java/docs/selectors)

```
  actor()
      .expectThat()
      .selector("button")
      .isVisible();
```

<h3 id="locator">
    locator(Locator locator)
</h3>

This method point all next checks in chain on selected web element. But unlike selector it accepts playwright
object [Locator](https://playwright.dev/java/docs/api/class-locator)

```
  actor()
      .expectThat()
      .locator(jPlay().currentFrame().locator("button"))
      .isVisible();
```

<h3 id="locators">
    locators(Locator locator)
</h3>
Synonym for locator. This method represents just syntax sugar in plural form.

<h3 id="isVisible">
    isVisible() & visible()
</h3>

Ensures the Locator points to a visible DOM node.

```
  actor()
      .expectThat()
      .selector("button")
      .isVisible();
  // or
  actor()
      .expectThat()
      .selector("button")
      .is()
      .visible();
```

<h3 id="isHidden">
    isHidden() & hidden()
</h3>
Ensures the Locator points to a hidden DOM node, which is the opposite of visible.

```
  actor()
      .expectThat()
      .selector("button")
      .isHidden();
  // or
  actor()
      .expectThat()
      .selector("button")
      .is()
      .hidden();
```

<h3 id="isChecked">
    isChecked() & checked()
</h3>
Ensures the Locator points to a checked input.

```
  actor()
      .expectThat()
      .selector("[type='checkbox']")
      .isChecked();
  // or
  actor()
      .expectThat()
      .selector("[type='checkbox']")
      .is()
      .checked();
```

<h3 id="isDisabled">
    isDisabled() & disabled()
</h3>
Ensures the Locator points to a disabled element.

```
        actor()
                .expectThat()
                .selector("[type='checkbox']")
                .isDisabled();
        // or
        actor()
                .expectThat()
                .selector("[type='checkbox']")
                .is()
                .disabled();
```


___

<h2 id="pageAsserts">
    Page asserts
</h2>

<h3 id="page">
    page()
</h3>

TODO
___

<h2 id="dialogAsserts">
    Dialog asserts
</h2>

<h3 id="expectThatMessageEqualAndDismiss">
    expectThatMessageEqualAndDismiss(String expectedMessage)
</h3>

Ensure that next dialog has text and dismiss dialog.

```
  when()
      .dialog()
      .expectThatMessageEqualAndDismiss("Are you here?")
      .evaluate("window.result = confirm('Are you here?');");
  then()
      .dialog()
      .expectThatMessageEqualAndDismiss("false")
      .evaluate("alert(window.result);");
```

<h3 id="expectThatMessageEqualAndAccept">
    expectThatMessageEqualAndAccept(String expectedMessage)
</h3>

Ensure that next dialog has text and accept dialog.

```
  when()
      dialog()
      .expectThatMessageEqualAndAccept("Are you here?")
      .evaluate("window.result = confirm('Are you here?');");
  then()
      .dialog()
      .expectThatMessageEqualAndDismiss("true")
      .evaluate("alert(window.result);");
```
