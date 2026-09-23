import { useTable } from "@tanstack/react-table";
import type { PageResponseServiceTeamTicketListItemResponse } from "@/api/generated/models/page-response-service-team-ticket-list-item-response.ts";
import { useServiceTeamTicketParams } from "@/features/serviceTeams/hooks/useServcieTeamTicketParams.ts";
import {
  serviceTeamTicketColumns,
  serviceTeamTicketTableFeatures,
} from "@/features/tickets/components/serviceTeamTicket/serviceTeamTicketColumns.tsx";
import { TablePagination } from "@/features/tickets/components/serviceTeamTicket/TablePagination.tsx";

type ServiceTeamTicketTableProps = {
  ticketPage: PageResponseServiceTeamTicketListItemResponse;
};

export const ServiceTeamTicketTable = ({
  ticketPage,
}: ServiceTeamTicketTableProps) => {
  const { setPage } = useServiceTeamTicketParams();
  const serviceTeamTickets = ticketPage.content;

  const table = useTable({
    features: serviceTeamTicketTableFeatures,
    columns: serviceTeamTicketColumns,
    data: serviceTeamTickets,
    getRowId: (ticket) => String(ticket.id),
  });

  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="table-scrollbar overflow-x-auto">
        <table className="w-full min-w-275 border-collapse text-sm">
          <caption className="sr-only">Tickets des Service-Teams</caption>
          <thead className="bg-muted/40 text-left text-xs text-muted-foreground">
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id} className="border-b border-border">
                {headerGroup.headers.map((header) => (
                  <th
                    key={header.id}
                    scope="col"
                    className="h-10 px-4 font-medium whitespace-nowrap"
                  >
                    {header.isPlaceholder ? null : (
                      <table.FlexRender header={header} />
                    )}
                  </th>
                ))}
              </tr>
            ))}
          </thead>

          <tbody>
            {table.getRowModel().rows.length > 0 ? (
              table.getRowModel().rows.map((row) => (
                <tr
                  key={row.id}
                  className="border-b border-border transition-colors last:border-b-0 hover:bg-muted/30"
                >
                  {row.getAllCells().map((cell) => (
                    <td
                      key={cell.id}
                      className="px-4 py-3 text-muted-foreground"
                    >
                      <table.FlexRender cell={cell} />
                    </td>
                  ))}
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={serviceTeamTicketColumns.length}
                  className="h-64 px-4 text-center text-sm text-muted-foreground"
                >
                  Für dieses Service-Team wurden keine Tickets gefunden.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <TablePagination
        currentPage={ticketPage.number + 1}
        totalPages={ticketPage.totalPages}
        totalElements={ticketPage.totalElements}
        pageSize={ticketPage.size}
        currentItemsCount={serviceTeamTickets.length}
        onPageChange={setPage}
      />
    </section>
  );
};
