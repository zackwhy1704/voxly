CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    country VARCHAR(100),
    goal VARCHAR(50),
    current_tier VARCHAR(20) NOT NULL DEFAULT 'BRONZE',
    career_readiness_score INT NOT NULL DEFAULT 0,
    streak_days INT NOT NULL DEFAULT 0,
    total_xp INT NOT NULL DEFAULT 0,
    weekly_xp INT NOT NULL DEFAULT 0,
    rank VARCHAR(50) NOT NULL DEFAULT 'Bronze I',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_users_email ON users(email);
