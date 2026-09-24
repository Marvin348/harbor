CREATE INDEX idx_ticket_org_team
    ON tickets (organization_id, service_team_id);