import { Spinner } from "@/components/ui/spinner.tsx";

export const TicketMessagesLoadingState = () => {
  return (
    <div
      className="flex min-h-0 flex-1 items-center justify-center py-16"
      role="status"
      aria-live="polite"
    >
      <div className="flex max-w-sm flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-border bg-muted/30">
          <Spinner className="size-5 text-muted-foreground" />
        </div>

        <h1 className="mt-5 text-base font-semibold">
          Nachrichten werden geladen
        </h1>
        <p className="text-sm leading-6 text-muted-foreground">
          Der Gesprächsverlauf wird vorbereitet.
        </p>
      </div>
    </div>
  );
};
