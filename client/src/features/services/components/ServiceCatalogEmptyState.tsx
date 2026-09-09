import { Boxes, Plus } from "lucide-react";

import { Button } from "@/components/ui/button";

type ServiceCatalogEmptyStateProps = {
  onCreateService: () => void;
};

export const ServiceCatalogEmptyState = ({
  onCreateService,
}: ServiceCatalogEmptyStateProps) => {
  return (
    <div className="flex min-h-[360px] items-center justify-center px-4 py-12">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-border bg-muted/40">
          <Boxes className="size-5 text-muted-foreground" />
        </div>

        <h3 className="mt-5 text-base font-semibold">Noch keine Services</h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Erstelle den ersten Service, damit Mitarbeitende später passende
          Angebote im Servicekatalog finden können.
        </p>

        <Button className="mt-5" onClick={onCreateService}>
          <Plus />
          Ersten Service erstellen
        </Button>
      </div>
    </div>
  );
};
