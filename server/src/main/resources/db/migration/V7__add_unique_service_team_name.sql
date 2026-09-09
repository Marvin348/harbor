CREATE UNIQUE INDEX uq_services_team_name
    ON services (
                 organization_id,
                 service_team_id,
                 LOWER(name)
        );