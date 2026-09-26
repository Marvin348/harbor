import { Button } from "@/components/ui/button.tsx";
import { formatDate } from "@/shared/utils/formatDate.ts";

type AgentTicketActivitySectionProps = {
  createdAt: string;
  updatedAt: string;
};

export const AgentTicketActivitySection = ({
  createdAt,
  updatedAt,
}: AgentTicketActivitySectionProps) => {
  const activity = [
    {
      id: 1,
      timestamp: formatDate(updatedAt),
      title: "Öffentliche Antwort gesendet",
      description: "Daniel Weber antwortete dem Requester.",
    },
    {
      id: 2,
      timestamp: "09:51 Uhr",
      title: "Priorität auf Hoch gesetzt",
      description: "Automatische Bewertung anhand von Impact und Urgency.",
    },
    {
      id: 3,
      timestamp: formatDate(createdAt),
      title: "Ticket erstellt",
      description: "Eingegangen über das Self-Service-Portal.",
    },
  ];

  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="flex items-start justify-between gap-4 border-b border-border px-4 py-3">
        <div>
          <h2 className="text-sm font-semibold">Aktivität</h2>
          <p className="mt-0.5 text-xs text-muted-foreground">
            Änderungen und Bearbeitungsschritte
          </p>
        </div>
        <Button type="button" variant="ghost" size="xs">
          Gesamten Verlauf anzeigen
        </Button>
      </div>

      <ol className="px-4 py-2">
        {activity.map((item, index) => (
          <li key={item.id} className="relative flex gap-4 py-3">
            {index < activity.length - 1 && (
              <span
                aria-hidden="true"
                className="absolute top-5 bottom-[-12px] left-[4px] w-px bg-border"
              />
            )}

            <span
              aria-hidden="true"
              className="relative mt-1.5 size-2.5 shrink-0 rounded-full border-2 border-background bg-muted-foreground"
            />

            <div className="min-w-0 flex-1">
              <div className="flex flex-wrap items-start justify-between gap-x-4 gap-y-1">
                <p className="text-sm font-medium">{item.title}</p>
                <time className="shrink-0 text-xs text-muted-foreground">
                  {item.timestamp}
                </time>
              </div>
              <p className="mt-1 text-xs leading-5 text-muted-foreground">
                {item.description}
              </p>
            </div>
          </li>
        ))}
      </ol>
    </section>
  );
};
