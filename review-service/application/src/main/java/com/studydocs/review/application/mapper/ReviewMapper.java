package com.studydocs.review.application.mapper;

import com.studydocs.review.domain.model.ReviewModel;
import com.studydocs.review.application.dto.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper chuyển đổi giữa Domain Model và DTO.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "parentId", source = "parentId")
    @Mapping(target = "content", source = "content.content")
    ReviewResponse toResponse(ReviewModel review);
}
