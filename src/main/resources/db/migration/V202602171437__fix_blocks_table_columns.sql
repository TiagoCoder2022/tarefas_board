ALTER TABLE blocks
    CHANGE COLUMN unbloked_at unblocked_at TIMESTAMP NULL;
ALTER TABLE blocks
    MODIFY COLUMN unblock_reason VARCHAR(255) NULL;