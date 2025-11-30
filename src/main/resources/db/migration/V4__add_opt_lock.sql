ALTER TABLE book ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
COMMENT ON COLUMN book.version IS 'Version number. Used for optimistic locking'
