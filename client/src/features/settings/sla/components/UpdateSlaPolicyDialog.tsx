import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog.tsx";
import { UpdateSlaPolicyForm } from "@/features/settings/sla/components/UpdateSlaPolicyForm.tsx";

type UpdateSlaPolicyDialogProps = {
  policy: SlaPolicyResponse;
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

export const UpdateSlaPolicyDialog = ({
  policy,
  open,
  onOpenChange,
}: UpdateSlaPolicyDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange} disablePointerDismissal>
      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle>SLA-Richtlinie bearbeiten</DialogTitle>
          <DialogDescription>
            Passe die Reaktions- und Lösungszeiten für diese Ticket-Priorität
            an.
          </DialogDescription>
        </DialogHeader>

        <UpdateSlaPolicyForm
          policy={policy}
          onUpdated={() => onOpenChange(false)}
        />
      </DialogContent>
    </Dialog>
  );
};
