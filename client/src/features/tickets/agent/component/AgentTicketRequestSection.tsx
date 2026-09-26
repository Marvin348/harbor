import { Button } from "@/components/ui/button.tsx";

type AgentTicketRequestSectionProps = {
  description: string;
};

export const AgentTicketRequestSection = ({
  description,
}: AgentTicketRequestSectionProps) => {
  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="flex items-start justify-between gap-4 border-b border-border px-4 py-3">
        <div>
          <h2 className="text-sm font-semibold">Anfrage</h2>
          <p className="mt-0.5 text-xs text-muted-foreground">
            Ursprüngliche Beschreibung des Requesters
          </p>
        </div>
        <Button type="button" variant="ghost" size="xs">
          Bearbeiten
        </Button>
      </div>

      <div className="px-4 py-4">
        <p className="text-sm leading-7 text-foreground/90">{description}</p>
      </div>

      <dl className="grid border-t border-border sm:grid-cols-3">
        <div className="px-4 py-3">
          <dt className="text-xs text-muted-foreground">Gerät</dt>
          <dd className="mt-1 text-sm font-medium">NB-ANNA-14</dd>
          <p className="mt-0.5 text-xs text-muted-foreground">Windows 11</p>
        </div>
        <div className="border-t border-border px-4 py-3 sm:border-t-0 sm:border-l">
          <dt className="text-xs text-muted-foreground">Anwendung</dt>
          <dd className="mt-1 text-sm font-medium">Harbor VPN Client</dd>
          <p className="mt-0.5 text-xs text-muted-foreground">Version 4.8.2</p>
        </div>
        <div className="border-t border-border px-4 py-3 sm:border-t-0 sm:border-l">
          <dt className="text-xs text-muted-foreground">Quelle</dt>
          <dd className="mt-1 text-sm font-medium">Self-Service-Portal</dd>
          <p className="mt-0.5 text-xs text-muted-foreground">
            Vom Requester erstellt
          </p>
        </div>
      </dl>
    </section>
  );
};
