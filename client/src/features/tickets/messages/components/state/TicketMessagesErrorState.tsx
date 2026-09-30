import { Button } from "@/components/ui/button.tsx";
import { CircleAlert, RefreshCw } from "lucide-react";

type TicketMessagesErrorStateProps = {
  onRetry: () => void;
};

export const TicketMessagesErrorState = ({
  onRetry,
}: TicketMessagesErrorStateProps) => {
  return (
    <div
      className="flex min-h-0 flex-1 items-center justify-center py-16"
      role="alert"
    >
      <div className="flex max-w-lg flex-col items-center text-center">
        <div className="flex size-14 items-center justify-center rounded-md border border-destructive/30 bg-destructive/10">
          <CircleAlert className="size-6 text-destructive" />
        </div>

        <p className="mt-6 text-xs font-medium tracking-wide text-muted-foreground uppercase">
          Nachrichten
        </p>
        <h1 className="mt-2 text-xl font-semibold tracking-tight">
          Gesprächsverlauf nicht verfügbar
        </h1>
        <p className="mt-3 text-sm leading-6 text-muted-foreground">
          Die Nachrichten konnten nicht geladen werden. Prüfe deine Verbindung
          und versuche es erneut.
        </p>

        <Button
          type="button"
          variant="outline"
          className="mt-7"
          onClick={onRetry}
        >
          <RefreshCw />
          Erneut versuchen
        </Button>
      </div>
    </div>
  );
};
