CREATE INDEX idx_ticket_slas_response_due_pending
    ON ticket_slas (response_due_at) WHERE response_breached_at IS NULL;


CREATE INDEX idx_ticket_slas_resolution_due_pending
    ON ticket_slas (resolution_due_at) WHERE resolution_breached_at IS NULL;