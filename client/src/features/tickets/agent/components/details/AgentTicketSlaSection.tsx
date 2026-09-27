import { Button } from "@/components/ui/button.tsx";
import { Clock3 } from "lucide-react";

export const AgentTicketSlaSection = () => {
  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold">SLA &amp; Bearbeitung</h2>
        <p className="mt-0.5 text-xs text-muted-foreground">
          Nächste operative Ziele
        </p>
      </div>

      <dl className="divide-y divide-border px-4">
        <div className="py-3">
          <dt className="flex items-center justify-between gap-3 text-xs text-muted-foreground">
            <span>Erstreaktion</span>
            <span className="font-medium text-destructive">Noch 28 Min.</span>
          </dt>
          <dd className="mt-1 text-sm font-medium">Fällig heute, 10:42 Uhr</dd>
        </div>
        <div className="py-3">
          <dt className="text-xs text-muted-foreground">Lösungsziel</dt>
          <dd className="mt-1 text-sm font-medium">Morgen, 09:42 Uhr</dd>
        </div>
        <div className="py-3">
          <dt className="text-xs text-muted-foreground">
            Aktuelle Bearbeitungszeit
          </dt>
          <dd className="mt-1 flex items-center gap-1.5 text-sm font-medium">
            <Clock3 className="size-3.5 text-muted-foreground" />
            36 Minuten
          </dd>
        </div>
      </dl>

      <div className="border-t border-border px-4 py-3">
        <Button type="button" variant="outline" size="sm" className="w-full">
          SLA-Regel anzeigen
        </Button>
      </div>
    </section>
  );
};
