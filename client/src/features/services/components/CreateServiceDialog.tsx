import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { CreateServiceForm } from "@/features/services/components/CreateServiceForm";

type CreateServiceDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
};

export const CreateServiceDialog = ({
  open,
  onOpenChange,
}: CreateServiceDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange} disablePointerDismissal>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Service erstellen</DialogTitle>
        </DialogHeader>

        <CreateServiceForm onCreated={() => onOpenChange(false)} />
      </DialogContent>
    </Dialog>
  );
};
