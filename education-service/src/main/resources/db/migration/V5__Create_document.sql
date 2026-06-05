CREATE TABLE document (
    id BIGSERIAL PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE,

    university_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    media_id BIGINT NULL,
    thumbnail_url VARCHAR(1000) NULL,

    title VARCHAR(255),
    year INT,
    page_count INT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (university_id) REFERENCES university(id),
    FOREIGN KEY (subject_id) REFERENCES subject(id)
);
