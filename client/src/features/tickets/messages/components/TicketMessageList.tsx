import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";
import { TicketMessageItem } from "@/features/tickets/messages/components/TicketMessageItem.tsx";

type TicketMessageListProps = {
  ticketMessages: TicketMessageResponse[];
};

export const TicketMessageList = ({
  ticketMessages,
}: TicketMessageListProps) => {
  return (
    <div className="min-h-0 flex-1 overflow-y-auto table-scrollbar">
      <div className="mx-auto flex w-full max-w-4xl flex-col gap-5">
        {ticketMessages.map((message) => (
          <TicketMessageItem key={message.id} message={message} />
        ))}
      </div>
    </div>
  );
};
