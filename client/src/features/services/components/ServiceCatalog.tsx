import { ServiceCatalogEmptyState } from "@/features/services/components/ServiceCatalogEmptyState";
import { ServiceCatalogErrorState } from "@/features/services/components/ServiceCatalogErrorState";
import { ServiceCatalogLoadingState } from "@/features/services/components/ServiceCatalogLoadingState";
import { ServiceList } from "@/features/services/components/ServiceList";
import { useGetServices } from "@/features/services/hooks/useGetServices.ts";

type ServiceCatalogProps = {
  onCreateService: () => void;
};

export const ServiceCatalog = ({ onCreateService }: ServiceCatalogProps) => {
  const { services = [], isLoading, isError } = useGetServices();

  return (
    <section className="rounded-md border border-border bg-background">
      <div className="border-b border-border px-4 py-4">
        <h2 className="text-base font-semibold">Verfügbare Services</h2>
        <p className="mt-1 text-sm text-muted-foreground">
          {services.length} Services sind aktuell vorbereitet.
        </p>
      </div>
      {isLoading ? (
        <ServiceCatalogLoadingState />
      ) : isError ? (
        <ServiceCatalogErrorState />
      ) : services.length === 0 ? (
        <ServiceCatalogEmptyState onCreateService={onCreateService} />
      ) : (
        <ServiceList services={services} />
      )}
    </section>
  );
};
