import { Link } from "react-router-dom";
import { TICKET_PRIORITY_LABELS } from "@/features/tickets/constants/ticketPriorityLabels.ts";
import type { RequesterTicketItemResponse } from "@/api/generated/models/requester-ticket-item-response.ts";

type TicketItemProps = {
  ticket: RequesterTicketItemResponse;
};

export const TicketItem = ({ ticket }: TicketItemProps) => {
  return (
    <Link
      to={`/tickets/${ticket.id}`}
      className="block w-full px-4 py-4 text-left transition-colors hover:bg-muted/40"
    >
      <div className="flex min-w-0 flex-col gap-2">
        <div className="flex min-w-0 flex-wrap items-center gap-2">
          <span className="font-mono text-xs font-medium text-muted-foreground">
            #{ticket.id}
          </span>
          <h3 className="min-w-0 text-sm font-medium">{ticket.subject}</h3>
        </div>

        <div className="grid gap-1 text-xs text-muted-foreground md:grid-cols-[minmax(0,1fr)_160px_minmax(0,1fr)] md:items-center">
          <div className="flex min-w-0 flex-wrap items-center gap-x-2 gap-y-1">
            <span>{ticket.serviceName}</span>
            <span>·</span>
            <span>
              Priorität:{" "}
              {ticket.priority ? TICKET_PRIORITY_LABELS[ticket.priority] : ""}
            </span>
            <span>·</span>
            <span>
              {ticket.assignedAgentName
                ? `Agent: ${ticket.assignedAgentName}`
                : "Noch kein Agent"}
            </span>
          </div>

          <div className="flex flex-col items-center gap-2  md:mx-auto">
            <span className="rounded-md border px-2 py-1 font-mono text-xs font-medium">
              {ticket.status}
            </span>
          </div>

          <div className="flex flex-wrap items-center gap-x-2 gap-y-1 md:justify-end">
            <span>Erstellt: {ticket.createdAt}</span>
            <span>·</span>
            <span>Aktualisiert: {ticket.updatedAt}</span>
            <span>·</span>
            {/*<span>{ticket.commentCount} Kommentare</span>*/}
          </div>
        </div>
      </div>
    </Link>
  );
};
