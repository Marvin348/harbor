import { Button } from "@/components/ui/button.tsx";
import { Plus } from "lucide-react";

type TicketHeaderProps = {
  onCreateTicket: () => void;
};

export const TicketHeader = ({ onCreateTicket }: TicketHeaderProps) => {
  return (
    <section className="flex flex-col gap-4 xl:flex-row xl:items-end xl:justify-between">
      <div className="max-w-3xl">
        <h1 className="text-2xl font-semibold tracking-normal">Tickets</h1>
        <p className="mt-2 max-w-2xl text-sm leading-6 text-muted-foreground">
          Erstelle neue Anfragen und verfolge, was mit deinen bestehenden
          Tickets passiert.
        </p>
      </div>

      <Button type="button" onClick={onCreateTicket}>
        <Plus />
        Neue Anfrage
      </Button>
    </section>
  );
};
