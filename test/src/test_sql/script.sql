CREATE TABLE fichier (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(255),
    extension VARCHAR(50),
    content BYTEA,
    size BIGINT
);