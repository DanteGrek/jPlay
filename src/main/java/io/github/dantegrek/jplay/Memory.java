package io.github.dantegrek.jplay;

import java.util.*;

/**
 * This class is responsible for storing data.
 */
final class Memory {

    private final Map<Object, Object> memory = new HashMap<>();

    /**
     * Remembers value by key.
     *
     * @param key   to value
     * @param value you need to remember for later usage in the same thread.
     */
    public void remember(Object key, Object value) {
        if (key == null) {
            throw new RuntimeException("Argument 'key' can not be null.");
        }
        if (value == null) {
            throw new RuntimeException("Argument 'value' can not be null.");
        }
        if(this.memory.containsKey(key)) {
            throw new RuntimeException(String.format("Memory already remembers this key, please use another key or use " +
                    "function forgot(key) to remove previous key value pair.\nExisting value: %s", this.memory.get(key)));
        }
        this.memory.put(key, value);
    }

    /**
     * Recalls value by key and cast it to T.
     *
     * @param key to value.
     * @param <T> type of value method should return.
     * @return value after cast to valueType
     */
    public <T> T recall(Object key) {
        if (memory.containsKey(key)) {
            return (T) memory.get(key);
        }
        throw new RuntimeException(String
                .format("Key: '%s' was not remembered. Please use memory.remember(key, value), to store data.", key));
    }

    /**
     * Recalls value by key.
     * Checks if value is an instance of valueType or interface implementation and then cast to value type.
     *
     * @param key       to value.
     * @param valueType expected value class or implemented interface.
     * @param <T>       not required for this implementation.
     * @return value after cast to valueType
     */
    public <T> T recallInstanceOf(Object key, Class<T> valueType) {
        Object value = this.recall(key);
        Class classOfValue = value.getClass();
        if (valueType.isInterface()) {
            if (Arrays.stream(classOfValue.getInterfaces()).anyMatch(inter -> inter.equals(valueType))) {
                return valueType.cast(value);
            } else {
                throw new RuntimeException(String
                        .format("Value by key: '%s' is not an implementation of '%s' but instance of '%s'.", key, valueType, classOfValue));
            }
        } else if (classOfValue.equals(valueType)) {
            return valueType.cast(value);
        } else {
            throw new RuntimeException(String
                    .format("Value by key: '%s' is not an instance of '%s' but instance of '%s'.", key, valueType, classOfValue));
        }
    }

    /**
     * Removes key and value from memory.
     *
     * @param key to value
     */
    public void forget(Object key) {
        this.memory.remove(key);
    }

    /**
     * Removes all key value pairs from memory.
     */
    public void clear() {
        this.memory.clear();
    }

}
