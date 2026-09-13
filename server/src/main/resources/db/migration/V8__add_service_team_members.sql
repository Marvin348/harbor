ALTER TABLE users
    ADD CONSTRAINT uq_users_id_organization
        UNIQUE (id, organization_id);


CREATE TABLE service_team_members
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT    NOT NULL,
    service_team_id BIGINT    NOT NULL,
    organization_id BIGINT    NOT NULL,
    created_at      TIMESTAMP NOT NULL,

    CONSTRAINT fk_service_team_members_user_organization
        FOREIGN KEY (user_id, organization_id)
            REFERENCES users (id, organization_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_service_team_members_team_organization
        FOREIGN KEY (service_team_id, organization_id)
            REFERENCES service_teams (id, organization_id)
            ON DELETE CASCADE,

    CONSTRAINT uq_user_and_team
        UNIQUE (user_id, service_team_id)
)