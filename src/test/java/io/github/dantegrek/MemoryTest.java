package io.github.dantegrek;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static io.github.dantegrek.jplay.Jplay.*;
import static org.junit.jupiter.api.Assertions.*;

public class MemoryTest {

    @AfterEach
    public void afterEach() {
        then().clearMemory();
    }

    @Test
    public void rememberAndRecallStringTest() {
        when()
                .remember("key", "value");

        assertEquals("value", then().recall("key"));
    }

    @Test
    public void rememberAndRecallMultipleTypesOfKeysAndValues() {
        List<String> objectAsKey = new ArrayList();
        objectAsKey.add("key");
        List<String> objectAsKey2 = new ArrayList();
        objectAsKey.add("key");

        when()
                .remember(1, new ArrayList())
                .remember(objectAsKey, "value 1")
                .remember(objectAsKey2, Integer.valueOf(555));

        assertAll("Remember",
                () -> assertEquals(new ArrayList(), then().recall(1)),
                () -> assertEquals("value 1", then().recall(objectAsKey)),
                () -> assertEquals(Integer.valueOf(555), then().recall(objectAsKey2))
        );
    }

    @Test
    public void forgetKeyValuePairTest() {
         given()
                 .remember("key", "value");

         when()
                 .forget("key");

         RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> then().recall("key"));
         assertEquals("Key: 'key' was not remembered. Please use memory.remember(key, value), to store data.",
                 runtimeException.getMessage());
    }

    @Test
    public void recallValueWithGenericTest() {
        when()
                .remember("key", new StringBuffer("test"));

        assertInstanceOf(StringBuffer.class, then().<String>recall("key"));
    }

    @Test
    public void recallValueWithValueTypeAsClassTest() {
        when()
                .remember("key", new StringBuffer("test"));

        assertInstanceOf(StringBuffer.class, then().recall("key", StringBuffer.class));
    }

    @Test
    public void recallValueWithValueTypeAsInterfaceTest() {
        when()
                .remember("key", new ArrayList());

        assertInstanceOf(List.class, then().recall("key", List.class));
    }

    @Test
    public void recallValueWithWrongValueTypeClassTest() {
        when()
                .remember("key", new ArrayList());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                then().recall("key", LinkedList.class)
                );
        assertEquals("Value by key: 'key' is not an instance of 'class java.util.LinkedList' " +
                "but instance of 'class java.util.ArrayList'.", runtimeException.getMessage());
    }

    @Test
    public void recallValueWithWrongValueTypeInterfaceTest() {
        when()
                .remember("key", new ArrayList());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                then().recall("key", Runnable.class)
        );

        assertEquals("Value by key: 'key' is not an implementation of 'interface java.lang.Runnable' " +
                "but instance of 'class java.util.ArrayList'.", runtimeException.getMessage());
    }

    @Test
    public void rememberOneKeyTwiceTest() {
        when()
                .remember("key", "Test value");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->
                then().remember("key", "new value")
        );
        assertEquals("Memory already remembers this key, please use another key or use function forgot(key) " +
                "to remove previous key value pair.\nExisting value: Test value", runtimeException.getMessage());
    }

}
