import { ServiceTeamTicketTable } from "@/features/tickets/components/serviceTeamTicket/ServiceTeamTicketTable.tsx";
import { useGetServiceTeamTickets } from "@/features/tickets/hooks/useGetServiceTeamTickets.ts";
import { Route } from "@/routes/_app.service-teams_.$id.tsx";
import { ServiceTeamTicketToolbar } from "@/features/tickets/components/serviceTeamTicket/ServiceTeamTicketToolbar.tsx";
import { ServiceTeamTicketLoadingState } from "@/features/tickets/components/serviceTeamTicket/state/ServiceTeamTicketLoadingState.tsx";
import { ServiceTeamTicketErrorState } from "@/features/tickets/components/serviceTeamTicket/state/ServiceTeamTicketErrorState.tsx";

export const ServiceTeamDetailsTicketsPage = () => {
  const { id } = Route.useParams();

  const { data, isLoading, error } = useGetServiceTeamTickets(Number(id));

  if (isLoading) return <ServiceTeamTicketLoadingState />;
  if (error || !data) return <ServiceTeamTicketErrorState />;

  return (
    <>
      <h1 className="text-xl font-semibold tracking-tight">Tickets</h1>

      <ServiceTeamTicketToolbar />
      <ServiceTeamTicketTable ticketPage={data} />
    </>
  );
};
