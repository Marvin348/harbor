import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import {
  Lock,
  MessageSquareText,
  Paperclip,
  PlusCircleIcon,
  Send,
  Sparkles,
} from "lucide-react";
import { CreateTicketMessageRequestTypeEnum } from "@/api/generated/models/create-ticket-message-request.ts";
import type { CreateTicketMessageFields } from "@/features/tickets/messages/schema/createTicketMessageSchema.ts";

type TicketComposerActionSelectorProps = {
  messageType: CreateTicketMessageFields["type"];
  isSubmitting: boolean;
  onMessageTypeChange: (type: CreateTicketMessageFields["type"]) => void;
};

export const TicketComposerActionSelector = ({
  messageType,
  isSubmitting,
  onMessageTypeChange,
}: TicketComposerActionSelectorProps) => {
  const isInternalNote =
    messageType === CreateTicketMessageRequestTypeEnum.InternalNote;

  const toggleMessageType = () => {
    onMessageTypeChange(
      isInternalNote
        ? CreateTicketMessageRequestTypeEnum.Reply
        : CreateTicketMessageRequestTypeEnum.InternalNote,
    );
  };

  return (
    <div className="flex items-center justify-between border-t border-border pt-2">
      <div className="flex items-center gap-2">
        <DropdownMenu>
          <DropdownMenuTrigger
            render={
              <Button
                type="button"
                variant="ghost"
                size="icon-sm"
                aria-label="Weitere Aktionen"
                disabled={isSubmitting}
              />
            }
          >
            <PlusCircleIcon className="size-5" />
          </DropdownMenuTrigger>

          <DropdownMenuContent
            side="top"
            align="start"
            sideOffset={8}
            className="w-48"
          >
            <DropdownMenuItem>
              <Paperclip />
              Datei hinzufügen
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Sparkles />
              KI fragen
            </DropdownMenuItem>
            <DropdownMenuItem onClick={toggleMessageType}>
              {isInternalNote ? <MessageSquareText /> : <Lock />}
              {isInternalNote ? "Öffentliche Antwort" : "Interne Notiz"}
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

        {isInternalNote && (
          <span className="inline-flex items-center gap-1.5 rounded-md bg-muted px-2 py-1 text-xs font-medium text-muted-foreground">
            <Lock className="size-3" aria-hidden="true" />
            Interne Notiz
          </span>
        )}
      </div>

      <Button type="submit" disabled={isSubmitting}>
        {isSubmitting ? <Spinner /> : <Send />}
        {isInternalNote ? "Notiz hinzufügen" : "Senden"}
      </Button>
    </div>
  );
};
