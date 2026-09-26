import { Copy } from "lucide-react";

type AgentTicketCopyIdActionProps = {
  ticketId: number;
};

export const AgentTicketCopyIdAction = ({
  ticketId,
}: AgentTicketCopyIdActionProps) => {
  return (
    <section className="rounded-md border border-border bg-background px-4 py-3">
      <button
        type="button"
        className="flex w-full items-center justify-center gap-2 text-xs font-medium text-muted-foreground transition-colors hover:text-foreground"
      >
        <Copy className="size-3.5" />
        Ticket #{ticketId} kopieren
      </button>
    </section>
  );
};
