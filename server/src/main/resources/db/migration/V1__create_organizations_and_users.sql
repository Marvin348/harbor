CREATE TABLE organizations
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL
);


CREATE TABLE users
(
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    password_hash   VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    created_at      TIMESTAMP    NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    organization_id BIGINT       NOT NULL,

    CONSTRAINT fk_users_organization
        FOREIGN KEY (organization_id)
        REFERENCES organizations (id)
);

