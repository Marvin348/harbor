import { TicketItem } from "@/features/tickets/components/overview/TicketItem.tsx";
import type { RequesterTicketItemResponse } from "@/api/generated/models/requester-ticket-item-response.ts";

type TicketListProps = {
  tickets: RequesterTicketItemResponse[];
};

export const TicketList = ({ tickets }: TicketListProps) => {
  return (
    <div className="divide-y divide-border">
      {tickets.map((ticket) => (
        <TicketItem key={ticket.id} ticket={ticket} />
      ))}
    </div>
  );
};
