CREATE TABLE sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scenario_id UUID NOT NULL REFERENCES scenarios(id),
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    duration_seconds INT NOT NULL DEFAULT 0,
    overall_score INT NOT NULL DEFAULT 0,
    pronunciation_score INT NOT NULL DEFAULT 0,
    fluency_score INT NOT NULL DEFAULT 0,
    grammar_score INT NOT NULL DEFAULT 0,
    vocabulary_score INT NOT NULL DEFAULT 0,
    xp_earned INT NOT NULL DEFAULT 0,
    ai_insights_json TEXT DEFAULT '[]',
    flagged_words_json TEXT DEFAULT '[]',
    filler_words_json TEXT DEFAULT '[]',
    pacing_data_json TEXT DEFAULT '[]',
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_sessions_user ON sessions(user_id);
CREATE INDEX idx_sessions_completed ON sessions(completed_at);
