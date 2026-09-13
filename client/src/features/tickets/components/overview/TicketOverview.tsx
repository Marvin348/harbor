import { TicketList } from "@/features/tickets/components/overview/TicketList.tsx";
import { TicketPagination } from "@/features/tickets/components/overview/TicketPagination.tsx";
import { TicketToolbar } from "@/features/tickets/components/overview/TicketToolbar.tsx";
import { useGetRequesterTickets } from "@/features/tickets/hooks/useGetRequesterTickets.ts";
import { TicketOverviewLoadingState } from "@/features/tickets/components/overview/state/TicketOverviewLoadingState.tsx";
import { TicketOverviewErrorState } from "@/features/tickets/components/overview/state/TicketOverviewErrorState.tsx";
import { TicketOverviewEmptyState } from "@/features/tickets/components/overview/state/TicketOverviewEmptyState.tsx";

type TicketOverviewProps = {
  onCreateTicket: () => void;
};

export const TicketOverview = ({ onCreateTicket }: TicketOverviewProps) => {
  const { requesterTickets, isLoading, isError } = useGetRequesterTickets();

  const tickets = requesterTickets?.content ?? [];

  const currentPage = (requesterTickets?.number ?? 0) + 1;
  const totalPages = requesterTickets?.totalPages ?? 1;
  const totalElements = requesterTickets?.totalElements ?? 0;

  return (
    <section className="rounded-md border border-border bg-background">
      <div className="flex flex-col gap-3 border-b border-border px-4 py-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h2 className="text-base font-semibold">Meine Anfragen</h2>
          <p className="mt-1 text-sm text-muted-foreground">
            {totalElements} aktive Anfragen, sortiert nach letzter Aktivität.
          </p>
        </div>

        <TicketToolbar />
      </div>

      {isLoading ? (
        <TicketOverviewLoadingState />
      ) : isError ? (
        <TicketOverviewErrorState />
      ) : tickets.length === 0 ? (
        <TicketOverviewEmptyState onCreateTicket={onCreateTicket} />
      ) : (
        <TicketList tickets={tickets} />
      )}

      {totalElements > 0 && (
        <TicketPagination
          currentPage={currentPage}
          totalPages={totalPages}
          totalElements={totalElements}
        />
      )}
    </section>
  );
};
