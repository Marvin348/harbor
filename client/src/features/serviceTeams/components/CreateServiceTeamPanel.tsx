import { CreateServiceTeamForm } from "@/features/serviceTeams/components/CreateServiceTeamForm.tsx";
import {
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet.tsx";

type CreateServiceTeamPanelProps = {
  onClosePanel: () => void;
};

export const CreateServiceTeamPanel = ({
  onClosePanel,
}: CreateServiceTeamPanelProps) => {
  return (
    <SheetContent side="right" className="w-[420px] gap-0 sm:max-w-[420px]">
      <SheetHeader className="shrink-0 border-b border-border">
        <SheetTitle className="text-lg font-medium">
          Neues Service-Team
        </SheetTitle>
        <SheetDescription>
          Erstelle ein Team als organisatorischen Einstieg. Details kannst du
          später im Team pflegen.
        </SheetDescription>
      </SheetHeader>

      <div className="min-h-0 flex-1">
        <CreateServiceTeamForm onClosePanel={onClosePanel} />
      </div>
    </SheetContent>
  );
};
