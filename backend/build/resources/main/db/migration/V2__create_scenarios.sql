CREATE TABLE scenarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    subtitle VARCHAR(255),
    tier VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    duration_minutes INT NOT NULL DEFAULT 15,
    accent_color_hex BIGINT,
    xp_reward INT NOT NULL DEFAULT 30,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE TABLE scenario_skills (
    scenario_entity_id UUID REFERENCES scenarios(id) ON DELETE CASCADE,
    skills_tested VARCHAR(255)
);
