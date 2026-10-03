import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog.tsx";
import { CreateSlaPolicyForm } from "@/features/settings/sla/components/CreateSlaPolicyForm.tsx";

type CreateSlaPolicyDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

export const CreateSlaPolicyDialog = ({
  open,
  onOpenChange,
}: CreateSlaPolicyDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange} disablePointerDismissal>
      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle>SLA-Richtlinie erstellen</DialogTitle>
          <DialogDescription>
            Definiere verbindliche Reaktions- und Lösungszeiten für eine
            Ticket-Priorität.
          </DialogDescription>
        </DialogHeader>

        <CreateSlaPolicyForm onCreated={() => onOpenChange(false)} />
      </DialogContent>
    </Dialog>
  );
};
