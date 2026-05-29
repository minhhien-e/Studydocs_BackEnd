package studydocs.media.util;

import studydocs.media.model.enums.MediaType;

public interface MediaTypeDetector {
    MediaType detect(String contentType, String fileName);
}
