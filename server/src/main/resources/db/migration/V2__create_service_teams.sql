CREATE TABLE service_teams
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    organization_id BIGINT       NOT NULL,

    CONSTRAINT fk_service_teams_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
);