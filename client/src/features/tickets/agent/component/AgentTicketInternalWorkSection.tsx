import { Button } from "@/components/ui/button.tsx";
import { Plus } from "lucide-react";

export const AgentTicketInternalWorkSection = () => {
  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold">Interne Bearbeitung</h2>
        <p className="mt-0.5 text-xs text-muted-foreground">
          Nur für Agents sichtbar
        </p>
      </div>

      <div className="px-4 py-4">
        <p className="text-sm leading-6">
          VPN-Gateway-Logs prüfen und bei erneutem Verbindungsabbruch an Network
          Operations eskalieren.
        </p>
        <p className="mt-2 text-xs text-muted-foreground">
          Daniel Weber · Heute, 10:16 Uhr
        </p>
      </div>

      <div className="flex gap-2 border-t border-border px-4 py-3">
        <Button type="button" variant="outline" size="sm" className="flex-1">
          <Plus />
          Notiz hinzufügen
        </Button>
        <Button type="button" variant="outline" size="sm" className="flex-1">
          Eskalieren
        </Button>
      </div>
    </section>
  );
};
