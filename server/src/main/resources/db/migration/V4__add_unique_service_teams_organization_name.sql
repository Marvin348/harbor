CREATE UNIQUE INDEX uq_service_teams_organization_name
    ON service_teams (organization_id, LOWER(name));