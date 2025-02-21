CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     login VARCHAR(50) NOT NULL UNIQUE,
                                     password VARCHAR(100) NOT NULL
);
