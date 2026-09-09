ALTER TABLE service_teams
    ADD CONSTRAINT uq_service_teams_id_organization
        UNIQUE (id, organization_id);


CREATE TABLE services
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    description     VARCHAR(250),
    created_at      TIMESTAMP    NOT NULL,

    organization_id BIGINT       NOT NULL,
    service_team_id BIGINT       NOT NULL,

    CONSTRAINT fk_services_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id),

    CONSTRAINT fk_services_service_team_organization
        FOREIGN KEY (service_team_id, organization_id)
            REFERENCES service_teams (id, organization_id)
);