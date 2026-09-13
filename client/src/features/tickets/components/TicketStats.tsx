import { CheckCircle2, Clock3, FileText } from "lucide-react";

export const TicketStats = () => {
  return (
    <section className="grid gap-3 md:grid-cols-3">
      <div className="rounded-md border border-border bg-background px-4 py-3">
        <div className="flex items-center justify-between gap-3">
          <p className="text-sm text-muted-foreground">Offen</p>
          <FileText className="size-4 text-muted-foreground" />
        </div>
        <p className="mt-2 text-2xl font-semibold">0</p>
      </div>

      <div className="rounded-md border border-border bg-background px-4 py-3">
        <div className="flex items-center justify-between gap-3">
          <p className="text-sm text-muted-foreground">In Bearbeitung</p>
          <Clock3 className="size-4 text-muted-foreground" />
        </div>
        <p className="mt-2 text-2xl font-semibold">0</p>
      </div>

      <div className="rounded-md border border-border bg-background px-4 py-3">
        <div className="flex items-center justify-between gap-3">
          <p className="text-sm text-muted-foreground">Gelöst</p>
          <CheckCircle2 className="size-4 text-muted-foreground" />
        </div>
        <p className="mt-2 text-2xl font-semibold">0</p>
      </div>
    </section>
  );
};
