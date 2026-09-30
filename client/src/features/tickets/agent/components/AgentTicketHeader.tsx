import { Link } from "@tanstack/react-router";
import {
  ArrowLeft,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  Ellipsis,
} from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { showErrorToast } from "@/common/showErrorToast.ts";
import { showSuccessToast } from "@/common/showSuccessToast.ts";
import { TicketNavigation } from "@/features/tickets/components/TicketNavigation.tsx";
import { claimTicketErrorMessage } from "@/features/tickets/agent/errors/claimTicketErrorMessage.ts";
import { useClaimTicket } from "@/features/tickets/agent/hooks/useClaimTicket.ts";
import type { AgentTicketHeaderResponse } from "@/api/generated/models/agent-ticket-header-response.ts";

type AgentTicketHeaderProps = {
  agentTicketHeader: AgentTicketHeaderResponse;
};

export const AgentTicketHeader = ({
  agentTicketHeader,
}: AgentTicketHeaderProps) => {
  const { mutate: claimTicket, isPending: isClaimingTicket } = useClaimTicket();

  const isAssigned = Boolean(agentTicketHeader.assignedAgentName);

  const onClaimTicket = () => {
    if (isAssigned) return;

    claimTicket(agentTicketHeader.id, {
      onSuccess: () => {
        showSuccessToast(`Du hast Ticket #${agentTicketHeader.id} übernommen.`);
      },
      onError: (error) => {
        showErrorToast(claimTicketErrorMessage(error));
      },
    });
  };

  return (
    <header className="shrink-0 border-b border-border">
      <div className="grid gap-4 pb-3 lg:grid-cols-[minmax(10rem,1fr)_minmax(0,2fr)_minmax(10rem,1fr)] lg:items-start">
        <Link
          to="/tickets"
          search={{ page: 1 }}
          className="inline-flex w-fit items-center gap-1.5 text-sm text-muted-foreground transition-colors hover:text-foreground"
        >
          <ArrowLeft className="size-3.5" />
          Alle Tickets
        </Link>

        <div className="flex min-w-0 items-center justify-center gap-1 lg:-mt-1.5">
          <Button
            type="button"
            variant="ghost"
            size="icon-sm"
            aria-label="Vorheriges Ticket"
          >
            <ChevronLeft />
          </Button>
          <Button
            type="button"
            variant="ghost"
            size="icon-sm"
            aria-label="Nächstes Ticket"
          >
            <ChevronRight />
          </Button>

          <div className="ml-2 flex min-w-0 items-center gap-2">
            <span className="shrink-0 text-sm font-semibold tabular-nums">
              #HD-{agentTicketHeader.id}
            </span>
            <h1 className="truncate rounded-md bg-muted px-2.5 py-1.5 text-sm font-medium">
              {agentTicketHeader.subject}
            </h1>
          </div>
        </div>

        <div className="flex items-center justify-end gap-2 lg:-mt-1.5">
          <Button
            type="button"
            variant="ghost"
            size="icon"
            aria-label="Weitere Ticket-Aktionen"
          >
            <Ellipsis />
          </Button>

          <div className="flex">
            <Button
              type="button"
              className="rounded-r-none"
              disabled={isClaimingTicket}
              onClick={onClaimTicket}
            >
              {isClaimingTicket && <Spinner />}
              {isAssigned ? "Status ändern" : "Ticket übernehmen"}
            </Button>
            <Button
              type="button"
              size="icon"
              className="rounded-l-none border-l border-primary-foreground/20"
              aria-label="Weitere Statusoptionen"
            >
              <ChevronDown />
            </Button>
          </div>
        </div>
      </div>

      <TicketNavigation />
    </header>
  );
};
