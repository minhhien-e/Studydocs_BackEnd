package studydocs.media.web.rest.exception;

import studydocs.media.web.rest.exception.enums.ErrorCode;

import java.util.Map;

public class StorageException extends BaseException {
    public StorageException(String message) {
        super(ErrorCode.STORAGE_ERROR, message, Map.of());
    }

    public StorageException(ErrorCode errorCode, String message, Map<String, Object> attributes) {
        super(errorCode, message, attributes);
    }
}
