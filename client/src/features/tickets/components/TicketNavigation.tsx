import { TICKET_NAVIGATION } from "@/features/tickets/constants/ticketNavigation.ts";
import { Link } from "@tanstack/react-router";
import { Route } from "@/routes/_app.tickets_.$id.tsx";

export const TicketNavigation = () => {
  const { id } = Route.useParams();

  return (
    <nav
      aria-label="Ticket-Bereiche"
      className="flex  items-end justify-center gap-8 overflow-x-auto border-t border-border"
    >
      {TICKET_NAVIGATION.map((nav) => (
        <Link
          key={nav.label}
          to={nav.to}
          params={{ id }}
          activeOptions={{ exact: true }}
          className="flex h-10 shrink-0 items-center border-b-2 border-transparent px-1 text-sm font-medium text-muted-foreground transition-colors hover:text-foreground"
          activeProps={{
            className: "border-primary text-foreground",
          }}
        >
          {nav.label}
        </Link>
      ))}
    </nav>
  );
};
