CREATE TABLE sla_policies
(
    id                      BIGSERIAL PRIMARY KEY,
    organization_id         BIGINT       NOT NULL,
    name                    VARCHAR(100) NOT NULL,
    ticket_priority         VARCHAR(30)  NOT NULL,
    response_time_minutes   INTEGER      NOT NULL,
    resolution_time_minutes INTEGER      NOT NULL,
    enabled                 BOOLEAN      NOT NULL,

    CONSTRAINT fk_sla_policies_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id),

    CONSTRAINT uq_sla_policies_organization_priority
        UNIQUE (organization_id, ticket_priority),

    CONSTRAINT chk_sla_policies_priority
        CHECK (ticket_priority IN ('LOW',
                            'MEDIUM',
                            'HIGH',
                            'CRITICAL')),

    CONSTRAINT chk_sla_policies_response_time
        CHECK (response_time_minutes > 0),

    CONSTRAINT chk_sla_policies_resolution_time
        CHECK (resolution_time_minutes >= response_time_minutes)
);
