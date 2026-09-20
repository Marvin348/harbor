import { Spinner } from "@/components/ui/spinner.tsx";

export const ServiceTeamDetailsLoadingState = () => {
  return (
    <div
      className="mx-auto flex min-h-[calc(100vh-7.5rem)] w-full max-w-screen-2xl items-center justify-center px-4 py-16"
      role="status"
      aria-live="polite"
    >
      <div className="flex max-w-sm flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-border bg-muted/30">
          <Spinner className="size-5 text-muted-foreground" />
        </div>

        <h1 className="mt-5 text-base font-semibold">
          Service-Team wird geladen
        </h1>
        <p className="text-sm leading-6 text-muted-foreground">
          Teamdaten und Arbeitsbereich werden vorbereitet.
        </p>
      </div>
    </div>
  );
};
