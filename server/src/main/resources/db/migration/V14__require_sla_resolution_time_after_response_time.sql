ALTER TABLE sla_policies
DROP
CONSTRAINT chk_sla_policies_resolution_time,
    ADD CONSTRAINT chk_sla_policies_resolution_time
        CHECK (resolution_time_minutes > response_time_minutes);
