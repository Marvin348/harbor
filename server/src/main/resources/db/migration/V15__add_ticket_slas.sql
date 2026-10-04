ALTER TABLE sla_policies
    ADD CONSTRAINT uq_sla_policies_id_organization
        UNIQUE (id, organization_id);

CREATE TABLE ticket_slas
(
    id                     BIGSERIAL PRIMARY KEY,
    organization_id        BIGINT    NOT NULL,
    ticket_id              BIGINT    NOT NULL,
    sla_policy_id          BIGINT    NOT NULL,
    response_due_at        TIMESTAMP NOT NULL,
    resolution_due_at      TIMESTAMP NOT NULL,
    first_responded_at     TIMESTAMP,
    resolved_at            TIMESTAMP,
    response_breached_at   TIMESTAMP,
    resolution_breached_at TIMESTAMP,
    created_at             TIMESTAMP NOT NULL,

    CONSTRAINT uq_ticket_slas_ticket
        UNIQUE (ticket_id),

    CONSTRAINT fk_ticket_sla_ticket_org
        FOREIGN KEY (ticket_id, organization_id)
            REFERENCES tickets (id, organization_id),

    CONSTRAINT fk_ticket_slas_policy
        FOREIGN KEY (sla_policy_id, organization_id)
            REFERENCES sla_policies (id, organization_id)
);