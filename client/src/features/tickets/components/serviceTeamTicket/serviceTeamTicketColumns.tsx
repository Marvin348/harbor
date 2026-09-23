import { Link } from "@tanstack/react-router";
import { createColumnHelper, tableFeatures } from "@tanstack/react-table";
import type { ServiceTeamTicketListItemResponse } from "@/api/generated/models/service-team-ticket-list-item-response.ts";
import { ServiceTeamTicketListItemResponsePriorityEnum } from "@/api/generated/models/service-team-ticket-list-item-response.ts";
import { TICKET_PRIORITY_LABELS } from "@/features/tickets/constants/ticketPriorityLabels.ts";
import { TICKET_STATUS_LABELS } from "@/features/tickets/constants/ticketStatusLabels.ts";
import { formatDate } from "@/shared/utils/formatDate.ts";

export const serviceTeamTicketTableFeatures = tableFeatures({});

const columnHelper = createColumnHelper<
  typeof serviceTeamTicketTableFeatures,
  ServiceTeamTicketListItemResponse
>();

export const serviceTeamTicketColumns = columnHelper.columns([
  columnHelper.accessor("id", {
    header: "Ticket",
    cell: ({ getValue }) => (
      <Link
        to="/tickets/$id"
        params={{ id: String(getValue()) }}
        className="font-mono text-xs font-medium text-muted-foreground hover:text-foreground"
      >
        #{getValue()}
      </Link>
    ),
  }),
  columnHelper.accessor("subject", {
    header: "Betreff",
    cell: ({ getValue, row }) => (
      <Link
        to="/tickets/$id"
        params={{ id: String(row.original.id) }}
        className="block max-w-80 truncate font-medium text-foreground hover:underline"
      >
        {getValue()}
      </Link>
    ),
  }),
  columnHelper.accessor("requesterName", {
    header: "Anfragender",
  }),
  columnHelper.accessor("serviceName", {
    header: "Service",
  }),
  columnHelper.accessor("priority", {
    header: "Priorität",
    cell: ({ getValue }) => {
      const priority = getValue();
      const isCritical =
        priority === ServiceTeamTicketListItemResponsePriorityEnum.Critical;

      return (
        <span
          className={
            isCritical
              ? "inline-flex rounded-md border border-destructive/30 bg-destructive/10 px-2 py-0.5 text-xs font-medium text-destructive"
              : "inline-flex rounded-md border border-border bg-muted/40 px-2 py-0.5 text-xs font-medium text-muted-foreground"
          }
        >
          {TICKET_PRIORITY_LABELS[priority]}
        </span>
      );
    },
  }),
  columnHelper.accessor("status", {
    header: "Status",
    cell: ({ getValue }) => (
      <span className="inline-flex whitespace-nowrap rounded-md border border-border px-2 py-0.5 text-xs font-medium text-foreground">
        {TICKET_STATUS_LABELS[getValue()]}
      </span>
    ),
  }),
  columnHelper.accessor("assignedAgentName", {
    header: "Zugewiesen an",
    cell: ({ getValue }) => getValue() || "Nicht zugewiesen",
  }),
  columnHelper.accessor("createdAt", {
    header: "Erstellt am",
    cell: ({ getValue }) => (
      <time dateTime={getValue()} className="whitespace-nowrap">
        {formatDate(getValue())}
      </time>
    ),
  }),
]);
