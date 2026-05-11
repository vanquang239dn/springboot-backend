package vn.vanquang239dn.exception;

import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private final String fieldName;
    private final String fieldValue;

    public DuplicateResourceException(String fieldName, String fieldValue, String message) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}