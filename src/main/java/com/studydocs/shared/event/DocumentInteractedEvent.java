package com.studydocs.shared.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInteractedEvent {
    private String documentId;
    private String userId;
    
    /** 
     * Loại tương tác: LIKE, BOOKMARK 
     */
    private String type;
    
    /** 
     * true: thêm tương tác (Like/Bookmark)
     * false: xóa tương tác (Unlike/Unbookmark) 
     */
    private boolean isAdd;
}
