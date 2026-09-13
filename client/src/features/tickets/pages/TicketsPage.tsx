import { CreateTicketDialog } from "@/features/tickets/components/CreateTicketDialog.tsx";
import { TicketHeader } from "@/features/tickets/components/TicketHeader.tsx";
import { TicketOverview } from "@/features/tickets/components/overview/TicketOverview.tsx";
import { TicketStats } from "@/features/tickets/components/TicketStats.tsx";
import { useState } from "react";

export const TicketsPage = () => {
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);

  return (
    <div className="space-y-6">
      <TicketHeader onCreateTicket={() => setIsCreateDialogOpen(true)} />
      <TicketStats />

      <TicketOverview onCreateTicket={() => setIsCreateDialogOpen(true)} />

      <CreateTicketDialog
        open={isCreateDialogOpen}
        onOpenChange={setIsCreateDialogOpen}
      />
    </div>
  );
};
