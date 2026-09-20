import { Link } from "@tanstack/react-router";
import { ArrowLeft, CircleAlert, RefreshCw } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type ServiceTeamDetailsErrorStateProps = {
  onRetry: () => void;
};

export const ServiceTeamDetailsErrorState = ({
  onRetry,
}: ServiceTeamDetailsErrorStateProps) => {
  return (
    <div className="mx-auto flex min-h-[calc(100vh-7.5rem)] w-full max-w-screen-2xl items-center justify-center px-4 py-16">
      <div className="flex max-w-lg flex-col items-center text-center">
        <div className="flex size-14 items-center justify-center rounded-md border border-destructive/30 bg-destructive/10">
          <CircleAlert className="size-6 text-destructive" />
        </div>

        <p className="mt-6 text-xs font-medium tracking-wide text-muted-foreground uppercase">
          Service-Team
        </p>
        <h1 className="mt-2 text-xl font-semibold tracking-tight">
          Service-Team nicht verfügbar
        </h1>
        <p className="mt-3 text-sm leading-6 text-muted-foreground">
          Das Service-Team konnte nicht geöffnet werden. Es wurde möglicherweise
          entfernt, du hast keinen Zugriff darauf oder die Verbindung zum Server
          ist fehlgeschlagen.
        </p>

        <div className="mt-7 flex flex-col-reverse gap-2 sm:flex-row">
          <Button type="button" variant="outline" onClick={onRetry}>
            <RefreshCw />
            Erneut versuchen
          </Button>
          <Button render={<Link to="/service-teams" search={{ page: 1 }} />}>
            <ArrowLeft />
            Zurück zu Service-Teams
          </Button>
        </div>
      </div>
    </div>
  );
};
