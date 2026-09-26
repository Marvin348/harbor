import { Button } from "@/components/ui/button.tsx";
import { formatDate } from "@/shared/utils/formatDate.ts";
import { MessageSquareText } from "lucide-react";

type AgentTicketConversationSectionProps = {
  requesterName: string;
  createdAt: string;
  updatedAt: string;
};

export const AgentTicketConversationSection = ({
  requesterName,
  createdAt,
  updatedAt,
}: AgentTicketConversationSectionProps) => {
  const messages = [
    {
      id: 1,
      author: requesterName,
      role: "Requester",
      timestamp: formatDate(createdAt),
      content:
        "Das Problem tritt sowohl im WLAN als auch über den mobilen Hotspot auf. Einen Neustart und eine erneute Anmeldung habe ich bereits versucht.",
    },
    {
      id: 2,
      author: "Daniel Weber",
      role: "Agent",
      timestamp: formatDate(updatedAt),
      content:
        "Danke für die Informationen. Wir prüfen gerade die Verbindungsprotokolle und melden uns mit den nächsten Schritten.",
    },
  ];

  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="flex items-start justify-between gap-4 border-b border-border px-4 py-3">
        <div>
          <h2 className="text-sm font-semibold">Kommunikation</h2>
          <p className="mt-0.5 text-xs text-muted-foreground">
            Öffentlicher Austausch mit dem Requester
          </p>
        </div>
        <span className="text-xs text-muted-foreground">
          {messages.length} Nachrichten
        </span>
      </div>

      <div className="divide-y divide-border">
        {messages.map((message) => (
          <article key={message.id} className="px-4 py-4">
            <div className="flex flex-wrap items-center justify-between gap-2">
              <div className="flex items-center gap-2">
                <p className="text-sm font-medium">{message.author}</p>
                <span className="rounded border border-border px-1.5 py-0.5 text-[11px] text-muted-foreground">
                  {message.role}
                </span>
              </div>
              <time className="text-xs text-muted-foreground">
                {message.timestamp}
              </time>
            </div>
            <p className="mt-2 text-sm leading-6 text-foreground/85">
              {message.content}
            </p>
          </article>
        ))}
      </div>

      <div className="flex justify-end border-t border-border bg-muted/20 px-4 py-3">
        <Button type="button">
          <MessageSquareText />
          Kommunikation öffnen
        </Button>
      </div>
    </section>
  );
};
