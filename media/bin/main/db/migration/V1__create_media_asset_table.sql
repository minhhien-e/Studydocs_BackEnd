CREATE TABLE media_asset (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT,
    owner_id VARCHAR(255) NOT NULL,
    owner_type VARCHAR(255) NOT NULL,
    media_type VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    original_key VARCHAR(255) NOT NULL UNIQUE,
    original_filename VARCHAR(255),
    mime_type VARCHAR(100),
    size_bytes BIGINT,
    reject_reason VARCHAR(500)
);

CREATE INDEX idx_media_asset_owner ON media_asset(owner_id, owner_type);
