ALTER TABLE tickets
    ADD CONSTRAINT uq_tickets_id_organization
        UNIQUE (id, organization_id);


CREATE TABLE ticket_messages
(
    id              BIGSERIAL PRIMARY KEY,
    ticket_id       BIGINT      NOT NULL,
    organization_id BIGINT      NOT NULL,
    author_id       BIGINT      NOT NULL,
    type            VARCHAR(30) NOT NULL,
    body            TEXT        NOT NULL,
    created_at      TIMESTAMP   NOT NULL,

    CONSTRAINT fk_ticket_messages_ticket
        FOREIGN KEY (ticket_id, organization_id)
            REFERENCES tickets (id, organization_id),

    CONSTRAINT fk_ticket_messages_author
        FOREIGN KEY (author_id, organization_id)
            REFERENCES users (id, organization_id),

    CONSTRAINT chk_ticket_messages_type
        CHECK ( type IN ('REPLY',
                         'INTERNAL_NOTE'))
);

CREATE INDEX idx_ticket_messages_org_ticket_created_at
    on ticket_messages (organization_id, ticket_id, created_at);