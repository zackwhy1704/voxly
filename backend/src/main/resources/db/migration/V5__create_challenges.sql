CREATE TABLE daily_challenges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    xp_reward INT NOT NULL DEFAULT 20,
    accent_color_hex BIGINT,
    resets_at TIMESTAMP NOT NULL
);
CREATE TABLE challenge_completions (
    challenge_id UUID REFERENCES daily_challenges(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (challenge_id, user_id)
);
