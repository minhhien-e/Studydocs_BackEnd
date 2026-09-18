package com.studydocs.shared.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kafka event payload được publish sau khi tài liệu được upload thành công.
 * Consumer sẽ dùng event này để tải PDF và đếm số trang bất đồng bộ.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadedEvent {

    /** ID của document trong DB */
    private String documentId;

    /** URL đầy đủ của file PDF (Cloudinary URL hoặc nội bộ) */
    private String fileUrl;

    /** ID của người upload để cập nhật stats */
    private String uploaderId;
}
