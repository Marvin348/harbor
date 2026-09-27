import { Button } from "@/components/ui/button.tsx";

type AgentTicketRequesterSectionProps = {
  requesterDetails: {
    requester: {
      name: string;
      email: string;
    };
    service: {
      name: string;
      teamName: string;
    };
    assignedAgentName?: string;
  };
};

const getInitials = (name: string) =>
  name
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0])
    .join("")
    .toUpperCase();

export const AgentTicketRequesterSection = ({
  requesterDetails,
}: AgentTicketRequesterSectionProps) => {
  const { requester, service, assignedAgentName } = requesterDetails;

  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="flex items-start justify-between gap-4 border-b border-border px-4 py-3">
        <div>
          <h2 className="text-sm font-semibold">Requester</h2>
          <p className="mt-0.5 text-xs text-muted-foreground">
            Kontakt und Organisation
          </p>
        </div>
        <Button type="button" variant="ghost" size="xs">
          Profil
        </Button>
      </div>

      <div className="px-4 py-4">
        <div className="flex items-center gap-3">
          <span className="flex size-9 shrink-0 items-center justify-center rounded-full border border-border bg-muted text-xs font-semibold">
            {getInitials(requester.name)}
          </span>
          <div className="min-w-0">
            <p className="truncate text-sm font-medium">{requester.name}</p>
            <p className="truncate text-xs text-muted-foreground">
              {requester.email}
            </p>
          </div>
        </div>
      </div>

      <dl className="divide-y divide-border border-t border-border px-4">
        <div className="py-3">
          <dt className="text-xs text-muted-foreground">Organisation</dt>
          <dd className="mt-1 text-sm font-medium">
            Northstar GmbH SPÄTER ÄNDERN
          </dd>
        </div>
        <div className="py-3">
          <dt className="text-xs text-muted-foreground">Service</dt>
          <dd className="mt-1 text-sm font-medium">{service.name}</dd>
        </div>
        <div className="py-3">
          <dt className="text-xs text-muted-foreground">Service-Team</dt>
          <dd className="mt-1 text-sm font-medium">{service.teamName}</dd>
        </div>
        <div className="py-3">
          <dt className="text-xs text-muted-foreground">Zugewiesener Agent</dt>
          <dd className="mt-1 text-sm font-medium">
            {assignedAgentName ?? "Nicht zugewiesen"}
          </dd>
        </div>
      </dl>
    </section>
  );
};
