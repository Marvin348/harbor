import {
  BookOpenText,
  ClipboardList,
  MessageSquareText,
  Tags,
  UserRound,
} from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

export const AgentTicketConversationAside = () => {
  return (
    <aside className="flex items-start justify-center py-4">
      <div className="flex flex-col gap-2">
        <Button
          type="button"
          variant="secondary"
          size="icon"
          className="rounded-full"
          aria-label="Ticket-Details öffnen"
        >
          <ClipboardList />
        </Button>
        <Button
          type="button"
          variant="secondary"
          size="icon"
          className="rounded-full"
          aria-label="Requester-Informationen öffnen"
        >
          <UserRound />
        </Button>
        <Button
          type="button"
          variant="secondary"
          size="icon"
          className="rounded-full"
          aria-label="Interne Notizen öffnen"
        >
          <MessageSquareText />
        </Button>
        <Button
          type="button"
          variant="secondary"
          size="icon"
          className="rounded-full"
          aria-label="Wissensdatenbank öffnen"
        >
          <BookOpenText />
        </Button>
        <Button
          type="button"
          variant="secondary"
          size="icon"
          className="rounded-full"
          aria-label="Tags öffnen"
        >
          <Tags />
        </Button>
      </div>
    </aside>
  );
};
