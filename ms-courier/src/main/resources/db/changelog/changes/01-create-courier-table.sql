CREATE TABLE couriers (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          status VARCHAR(20) NOT NULL DEFAULT 'FREE',
                          created_at TIMESTAMP NOT NULL DEFAULT NOW()
);