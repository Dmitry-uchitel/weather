CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     login VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
                                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                         name VARCHAR(50) NOT NULL,
                                         userId BIGINT references users (id),
                                         latitude NUMERIC(15,8),
                                         longitude NUMERIC(15,8)
);

CREATE TABLE IF NOT EXISTS sessions (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        userId BIGINT references users (id),
                                        expires_at TIMESTAMP WITHOUT TIME ZONE
);