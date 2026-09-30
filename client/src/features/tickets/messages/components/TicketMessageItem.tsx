import type { TicketMessageResponse } from "@/api/generated/models/ticket-message-response.ts";
import { formatDate } from "@/shared/utils/formatDate.ts";
import { Lock } from "lucide-react";
import { useCurrentUser } from "@/features/auth/hooks/useCurrentUser.ts";

type TicketMessageItemProps = {
  message: TicketMessageResponse;
};

export const TicketMessageItem = ({ message }: TicketMessageItemProps) => {
  const { user } = useCurrentUser();

  const isOwnMessage = message.authorId === user?.id;
  const isSupportMessage =
    message.authorRole === "AGENT" ||
    message.authorRole === "ORGANIZATION_ADMIN";

  const isInternalNote = message.type === "INTERNAL_NOTE";

  if (isInternalNote) {
    return (
      <article
        className="flex justify-center"
        aria-label={`Interne Notiz von ${message.authorName}`}
      >
        <div className="w-full max-w-[85%] rounded-lg border border-dashed border-border bg-muted/40 px-4 py-3">
          <div className="mb-2 flex flex-wrap items-center gap-x-2 gap-y-1 text-xs">
            <span className="inline-flex items-center gap-1.5 rounded-md bg-background px-2 py-1 font-medium text-foreground shadow-xs">
              <Lock className="size-3" aria-hidden="true" />
              Interne Notiz
            </span>
            <span className="font-medium text-foreground">
              {message.authorName}
            </span>
            <time className="text-muted-foreground">
              {formatDate(message.createdAt)}
            </time>
          </div>

          <p className="text-sm leading-6 text-foreground">{message.body}</p>
        </div>
      </article>
    );
  }

  return (
    <article
      className={`flex ${isSupportMessage ? "justify-end" : "justify-start"}`}
    >
      <div className="max-w-[75%] sm:max-w-[68%]">
        <div
          className={`mb-1.5 flex items-center gap-2 text-xs ${
            isSupportMessage ? "justify-end" : "justify-start"
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
  );
};
