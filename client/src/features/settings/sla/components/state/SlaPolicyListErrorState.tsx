import { CircleAlert, RefreshCw } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";

type SlaPolicyListErrorStateProps = {
  onRetry: () => void;
};

export const SlaPolicyListErrorState = ({
  onRetry,
}: SlaPolicyListErrorStateProps) => {
  return (
    <div className="flex min-h-72 items-center justify-center px-4 py-10">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-destructive/30 bg-destructive/10">
          <CircleAlert className="size-5 text-destructive" />
        </div>
        <h3 className="mt-5 text-base font-semibold">
          SLA-Richtlinien konnten nicht geladen werden
        </h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Versuche es erneut. Falls das Problem bestehen bleibt, wende dich an
          einen Administrator.
        </p>
        <Button className="mt-5" variant="outline" onClick={onRetry}>
          <RefreshCw />
          Erneut versuchen
        </Button>
      </div>
    </div>
  );
};
