import { ServiceItem } from "@/features/services/components/ServiceItem.tsx";
import type { ServiceResponse } from "@/api/generated/models/service-response.ts";

type ServiceListProps = {
  services: ServiceResponse[];
};

export const ServiceList = ({ services }: ServiceListProps) => {
  return (
    <div className="divide-y divide-border">
      {services.map((service) => (
        <ServiceItem key={service.id} service={service} />
      ))}
    </div>
  );
};
