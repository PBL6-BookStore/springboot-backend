package com.pbl6.bookstore.exception;

/**
 * @author lkadai0801
 * @since 30/09/2022
 */

public class ObjectNotFoundException extends BookStoreException{
    private final String key;
    private final Object value;
    private final String message;

    public ObjectNotFoundException(String key, Object value) {
        super(true);
        this.key = key;
        this.value = value;
        this.message =  String.format("Object not found with value = %s", value);
    }

    public ObjectNotFoundException(Class<?> objectClass, String key, String value){
        super(true);
        this.key = objectClass.getSimpleName();
        this.value = String.format("%s = %s", key, value);
        this.message = String.format("%s not found with %s = %s", this.key, key, value);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
