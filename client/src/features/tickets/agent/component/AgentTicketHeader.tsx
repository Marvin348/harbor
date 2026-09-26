import { Button } from "@/components/ui/button.tsx";
import { Link } from "@tanstack/react-router";
import {
  ArrowLeft,
  Check,
  MessageSquareText,
  MoreHorizontal,
} from "lucide-react";
import type { AgentTicketDetailsResponse } from "@/api/generated/models/agent-ticket-details-response.ts";
import { formatDate } from "@/shared/utils/formatDate.ts";
import { TICKET_STATUS_LABELS } from "@/features/tickets/constants/ticketStatusLabels.ts";
import { TICKET_PRIORITY_LABELS } from "@/features/tickets/constants/ticketPriorityLabels.ts";

type AgentTicketHeaderProps = {
  agentTicketDetails: AgentTicketDetailsResponse;
};

export const AgentTicketHeader = ({
  agentTicketDetails,
}: AgentTicketHeaderProps) => {
  return (
    <header className="border-b border-border pb-6">
      <Link
        to="/tickets"
        search={{ page: 1 }}
        className="mb-5 inline-flex items-center gap-1.5 text-sm text-muted-foreground transition-colors hover:text-foreground"
      >
        <ArrowLeft className="size-3.5" />
        Alle Tickets
      </Link>

      <div className="flex flex-col gap-5 xl:flex-row xl:items-start xl:justify-between">
        <div className="min-w-0">
          <div className="flex flex-wrap items-center gap-2">
            <span className="font-mono text-xs font-medium text-muted-foreground">
              {agentTicketDetails.id}
            </span>
            <span className="rounded-md border border-border bg-muted/40 px-2 py-1 text-xs font-medium">
              {TICKET_STATUS_LABELS[agentTicketDetails.status]}
            </span>
            <span className="rounded-md border border-border px-2 py-1 text-xs font-medium text-muted-foreground">
              Priorität: {TICKET_PRIORITY_LABELS[agentTicketDetails.priority]}
            </span>
            <span className="rounded-md border border-destructive/30 px-2 py-1 text-xs font-medium text-destructive">
              SLA: noch 28 Min.
            </span>
          </div>

          <h1 className="mt-3 text-2xl font-semibold tracking-tight">
            {agentTicketDetails.subject}
          </h1>
          <p className="mt-2 text-sm text-muted-foreground">
            {agentTicketDetails.requesterName} · Erstellt{" "}
            {formatDate(agentTicketDetails.createdAt)} · Aktualisiert{" "}
            {formatDate(agentTicketDetails.updatedAt)}
          </p>
        </div>

        <div className="flex shrink-0 flex-wrap items-center gap-2">
          <Button type="button">
            <Check />
            Ticket übernehmen
          </Button>
          <Button type="button" variant="outline">
            <MessageSquareText />
            Kommunikation
            <span className="ml-0.5 rounded bg-muted px-1.5 py-0.5 text-[11px] font-semibold tabular-nums">
              100
            </span>
          </Button>
          <Button type="button" variant="outline">
            Interne Notiz
          </Button>
          <Button
            type="button"
            variant="outline"
            size="icon"
            aria-label="Weitere Ticket-Aktionen"
          >
            <MoreHorizontal />
          </Button>
        </div>
      </div>
    </header>
  );
};
