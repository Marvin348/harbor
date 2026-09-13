ALTER TABLE services
    ADD CONSTRAINT uq_services_id_organization
        UNIQUE (id, organization_id);

CREATE TABLE tickets
(
    id                   BIGSERIAL PRIMARY KEY,
    organization_id      BIGINT        NOT NULL,
    requester_id         BIGINT        NOT NULL,
    service_id           BIGINT        NOT NULL,
    service_team_id      BIGINT        NOT NULL,
    assigned_agent_id    BIGINT,
    subject              VARCHAR(150)  NOT NULL,
    description          VARCHAR(2000) NOT NULL,
    impact               VARCHAR(30)   NOT NULL,
    urgency              VARCHAR(30)   NOT NULL,
    business_criticality VARCHAR(30)   NOT NULL,
    priority             VARCHAR(30),
    status               VARCHAR(30)   NOT NULL DEFAULT 'OPEN',
    created_at           TIMESTAMP     NOT NULL,
    updated_at           TIMESTAMP     NOT NULL,

    CONSTRAINT fk_ticket_user_organization
        FOREIGN KEY (requester_id, organization_id)
            REFERENCES users (id, organization_id),

    CONSTRAINT fk_ticket_service_organization
        FOREIGN KEY (service_id, organization_id)
            REFERENCES services (id, organization_id),

    CONSTRAINT fk_ticket_service_team_organization
        FOREIGN KEY (service_team_id, organization_id)
            REFERENCES service_teams (id, organization_id),

    CONSTRAINT fk_ticket_assigned_agent_organization
        FOREIGN KEY (assigned_agent_id, organization_id)
            REFERENCES users (id, organization_id),

    CONSTRAINT chk_ticket_impact
        CHECK (impact IN ('SINGLE_USER',
                          'TEAM',
                          'MULTIPLE_TEAMS',
                          'ORGANIZATION')),

    CONSTRAINT chk_ticket_urgency
        CHECK (urgency IN ('WORK_AROUND_AVAILABLE',
                           'WORK_DEGRADED',
                           'WORK_BLOCKED')),

    CONSTRAINT chk_ticket_business_criticality
        CHECK (business_criticality IN ('LOW',
                                        'NORMAL',
                                        'HIGH',
                                        'MISSION_CRITICAL')),

    CONSTRAINT chk_ticket_priority
        CHECK ( priority IN ('LOW',
                             'MEDIUM',
                             'HIGH',
                             'CRITICAL')),

    CONSTRAINT chk_ticket_status
        CHECK ( status IN ('OPEN',
                           'IN_PROGRESS',
                           'ON_HOLD',
                           'RESOLVED',
                           'CLOSED'))
)