ALTER TABLE ticket_slas
    ADD CONSTRAINT uq_ticket_slas_id_org UNIQUE (id, organization_id);

CREATE TABLE sla_breach_processing
(
    id              BIGSERIAL PRIMARY KEY,
    organization_id BIGINT      NOT NULL,
    ticket_sla_id   BIGINT      NOT NULL,
    status          VARCHAR(30) NOT NULL,
    breach_type     VARCHAR(30) NOT NULL,
    queued_at       TIMESTAMP,
    processed_at    TIMESTAMP,
    created_at      TIMESTAMP   NOT NULL,

    CONSTRAINT fk_sla_breach_processing_org_ticket_sla
        FOREIGN KEY (ticket_sla_id, organization_id)
            REFERENCES ticket_slas (id, organization_id),

    CONSTRAINT uq_sla_breach_processing_ticket_sla_id_breach_type
        UNIQUE (ticket_sla_id, breach_type),

    CONSTRAINT chk_sla_breach_processing_status
        CHECK (status IN ('PENDING',
                          'PROCESSING',
                          'COMPLETED')),

    CONSTRAINT chk_sla_breach_processing_breach_type
        CHECK (breach_type IN ('RESPONSE',
                               'RESOLUTION'))

);