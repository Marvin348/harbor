import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { CreateTicketForm } from "@/features/tickets/components/CreateTicketForm.tsx";

type CreateTicketDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

export const CreateTicketDialog = ({
  open,
  onOpenChange,
}: CreateTicketDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange} disablePointerDismissal>
      <DialogContent className="sm:max-w-xl">
        <DialogHeader>
          <DialogTitle>Neue Anfrage</DialogTitle>
        </DialogHeader>

        <CreateTicketForm onCreated={() => onOpenChange(false)} />
      </DialogContent>
    </Dialog>
  );
};
