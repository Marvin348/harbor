import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";
import { RequesterTicketDetailsPage } from "@/features/tickets/requester/pages/RequesterTicketDetailsPage.tsx";
import { AgentTicketDetailsPage } from "@/features/tickets/agent/pages/AgentTicketDetailsPage.tsx";

export const TicketDetailsLayout = () => {
  const { user } = useCurrentUser();

  if (!user) return null;

  switch (user.role) {
    case "REQUESTER":
      return <RequesterTicketDetailsPage />;

    case "AGENT":
    case "ORGANIZATION_ADMIN":
      return <AgentTicketDetailsPage />;
  }
};
