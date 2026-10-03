import { SlaPolicyList } from "@/features/settings/sla/components/SlaPolicyList.tsx";
import { SlaPolicyListEmptyState } from "@/features/settings/sla/components/state/SlaPolicyListEmptyState.tsx";
import { SlaPolicyListErrorState } from "@/features/settings/sla/components/state/SlaPolicyListErrorState.tsx";
import { SlaPolicyListLoadingState } from "@/features/settings/sla/components/state/SlaPolicyListLoadingState.tsx";
import { useGetSlaPolicies } from "@/features/settings/sla/hooks/useGetSlaPolicies.ts";
import { TICKET_PRIORITY_OPTIONS } from "@/features/tickets/constants/ticketPriorityLabels.ts";

type SlaPolicyOverviewProps = {
  onCreatePolicy: () => void;
};

export const SlaPolicyOverview = ({
  onCreatePolicy,
}: SlaPolicyOverviewProps) => {
  const { slaPolicies = [], isLoading, isError, refetch } = useGetSlaPolicies();

  const summary = isLoading
    ? "Richtlinien werden geladen."
    : isError
      ? "Der aktuelle Stand konnte nicht ermittelt werden."
      : `${slaPolicies.length} von ${TICKET_PRIORITY_OPTIONS.length} Ticket-Prioritäten sind konfiguriert.`;

  return (
    <section className="rounded-md border border-border bg-background">
      <div className="border-b border-border px-4 py-4">
        <h3 className="text-base font-semibold">Konfigurierte Richtlinien</h3>
        <p className="mt-1 text-sm text-muted-foreground">{summary}</p>
      </div>

      {isLoading ? (
        <SlaPolicyListLoadingState />
      ) : isError ? (
        <SlaPolicyListErrorState onRetry={() => void refetch()} />
      ) : slaPolicies.length === 0 ? (
        <SlaPolicyListEmptyState onCreatePolicy={onCreatePolicy} />
      ) : (
        <SlaPolicyList policies={slaPolicies} />
      )}
    </section>
  );
};
