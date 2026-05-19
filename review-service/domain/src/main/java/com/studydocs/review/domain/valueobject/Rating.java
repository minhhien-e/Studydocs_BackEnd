package com.studydocs.review.domain.valueobject;

import com.studydocs.review.domain.exception.InvalidScoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object đại diện cho điểm đánh giá (1-5)
 */
@Embeddable
@Getter
@NoArgsConstructor
public class Rating {
    
    @Column(name = "score")
    private Integer score;

    public Rating(Integer score) {
        if (score != null && (score < 1 || score > 5)) {
            throw new InvalidScoreException();
        }
        this.score = score;
    }

    public boolean hasScore() {
        return score != null;
    }
}
