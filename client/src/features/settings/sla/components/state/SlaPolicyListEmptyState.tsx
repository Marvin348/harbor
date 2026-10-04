import { Clock3 } from "lucide-react";

export const SlaPolicyListEmptyState = () => {
  return (
    <div className="flex min-h-72 items-center justify-center px-4 py-10">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border bg-muted/40">
          <Clock3 className="size-5 text-muted-foreground" />
        </div>
        <h3 className="mt-5 text-base font-semibold">
          Keine Richtlinien gefunden
        </h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Für deine Organisation sind derzeit keine SLA-Richtlinien verfügbar.
        </p>
      </div>
    </div>
  );
};
