CREATE TABLE lessons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    unit_id VARCHAR(50) NOT NULL,
    position INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'LOCKED',
    xp_reward INT NOT NULL DEFAULT 15,
    phrase_content TEXT,
    pronunciation_tips_json TEXT DEFAULT '[]'
);
CREATE INDEX idx_lessons_unit ON lessons(unit_id, position);
