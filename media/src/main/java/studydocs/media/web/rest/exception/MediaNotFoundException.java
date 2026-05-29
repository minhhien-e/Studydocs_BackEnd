package studydocs.media.web.rest.exception;

import studydocs.media.web.rest.exception.enums.ErrorCode;

import java.util.Map;

public class MediaNotFoundException extends BaseException {
    public MediaNotFoundException(String file) {
        super(ErrorCode.MEDIA_NOT_FOUND, "File not found: " + file, Map.of("file", file));
    }
}
