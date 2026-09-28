import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";
import { formatDate } from "@/shared/utils/formatDate.ts";
import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";

type AgentTicketMessageListProps = {
  ticketMessages: TicketMessageResponse[];
};

export const AgentTicketMessageList = ({
  ticketMessages,
}: AgentTicketMessageListProps) => {
  const { user } = useCurrentUser();

  return (
    <div className="min-h-0 flex-1 overflow-y-auto">
      <div className="mx-auto flex w-full max-w-4xl flex-col gap-5 ">
        {ticketMessages.map((message) => {
          const isOwnMessage = message.authorId === user?.id;

          return (
            <div key={message.id}>
              <article
                className={`flex ${isOwnMessage ? "justify-end" : "justify-start"}`}
              >
                <div className="max-w-[75%] sm:max-w-[68%]">
                  <div
                    className={`mb-1.5 flex items-center gap-2 text-xs ${
                      isOwnMessage ? "justify-end" : "justify-start"
                    }`}
                  >
                    <span className="font-medium text-foreground">
                      {message.authorName}
                    </span>
                    <time className="text-muted-foreground">
                      {formatDate(message.createdAt)}
                    </time>
                  </div>

                  <p
                    className={`rounded-xl px-4 py-3 text-sm leading-6 ${
                      isOwnMessage
                        ? "rounded-tr-sm bg-muted"
                        : "rounded-tl-sm border border-border bg-background shadow-xs"
                    }`}
                  >
                    {message.body}
                  </p>
                </div>
              </article>
            </div>
          );
        })}
      </div>
    </div>
  );
};
