import type { ServiceResponse } from "@/api/generated/models/service-response.ts";

export const SERVICE_STATUS_LABELS: Record<ServiceResponse["status"], string> =
  {
    ACTIVE: "Aktiv",
    DRAFT: "Entwurf",
    INACTIVE: "Inaktiv",
  };
