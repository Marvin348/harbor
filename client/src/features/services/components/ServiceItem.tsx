import type { ServiceResponse } from "@/api/generated/models/service-response.ts";
import { SERVICE_STATUS_LABELS } from "@/features/services/constants/serviceStatusLabel.ts";

type ServiceItemProps = {
  service: ServiceResponse;
};

export const ServiceItem = ({ service }: ServiceItemProps) => {
  return (
    <article className="flex flex-col gap-4 px-4 py-4 transition-colors hover:bg-muted/40 md:flex-row md:items-center md:justify-between">
      <div className="min-w-0">
        <div className="flex flex-wrap items-center gap-2">
          <h2 className="text-sm font-medium">{service.name}</h2>
          <span className="rounded-md border border-border px-2 py-0.5 text-xs text-muted-foreground">
            {SERVICE_STATUS_LABELS[service.status]}
          </span>
        </div>

        {service.description ? (
          <p className="mt-1 max-w-2xl text-sm text-muted-foreground">
            {service.description}
          </p>
        ) : null}
      </div>
    </article>
  );
};
