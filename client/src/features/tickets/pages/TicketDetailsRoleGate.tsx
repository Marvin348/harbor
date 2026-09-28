import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";
import { RequesterTicketDetailsPage } from "@/features/tickets/requester/pages/RequesterTicketDetailsPage.tsx";
import { AgentTicketLayout } from "@/features/tickets/agent/pages/AgentTicketLayout.tsx";

export const TicketDetailsRoleGate = () => {
  const { user } = useCurrentUser();

  if (!user) return null;

  switch (user.role) {
    case "REQUESTER":
      return <RequesterTicketDetailsPage />; // layout later

    case "AGENT":
    case "ORGANIZATION_ADMIN":
      return <AgentTicketLayout />;
  }
};
