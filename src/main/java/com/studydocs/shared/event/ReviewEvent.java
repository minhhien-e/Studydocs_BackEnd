package com.studydocs.shared.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEvent {
    private String documentId;
    private String userId;
    
    /** 
     * true: thêm review
     * false: xóa review
     */
    private boolean isAdd;
}
