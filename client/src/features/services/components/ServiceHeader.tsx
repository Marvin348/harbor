import { Plus } from "lucide-react";
import { Button } from "@/components/ui/button";

type ServiceHeaderProps = {
  onCreateService: () => void;
};

export const ServiceHeader = ({ onCreateService }: ServiceHeaderProps) => {
  return (
    <section className="mb-6 flex flex-col gap-4 xl:flex-row xl:items-end xl:justify-between">
      <div className="max-w-3xl">
        <h1 className="text-2xl font-semibold tracking-normal">Services</h1>
        <p className="mt-2 max-w-2xl text-sm leading-6 text-muted-foreground">
          Verwalte die Services, die in deiner Organisation verfügbar sind.
        </p>
      </div>

      <Button type="button" onClick={onCreateService}>
        <Plus />
        Service erstellen
      </Button>
    </section>
  );
};
