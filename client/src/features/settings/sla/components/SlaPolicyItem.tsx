import type { SlaPolicyResponse } from "@/api/generated/models/sla-policy-response.ts";
import { TICKET_PRIORITY_LABELS } from "@/features/tickets/constants/ticketPriorityLabels.ts";

type SlaPolicyItemProps = {
  policy: SlaPolicyResponse;
};

export const SlaPolicyItem = ({ policy }: SlaPolicyItemProps) => {
  return (
    <article className="grid gap-4 px-4 py-4 md:grid-cols-[minmax(0,1fr)_9rem_9rem_auto] md:items-center">
      <div className="min-w-0">
        <h3 className="truncate text-sm font-medium">{policy.name}</h3>
        <span className="mt-1 inline-flex rounded-md border px-2 py-0.5 text-xs text-muted-foreground">
          {TICKET_PRIORITY_LABELS[policy.ticketPriority]}
        </span>
      </div>

      <div>
        <p className="text-xs text-muted-foreground">Reaktionszeit</p>
        <p className="mt-1 text-sm font-medium">
          {policy.responseTimeMinutes} Min.
        </p>
      </div>

      <div>
        <p className="text-xs text-muted-foreground">Lösungszeit</p>
        <p className="mt-1 text-sm font-medium">
          {policy.resolutionTimeMinutes} Min.
        </p>
      </div>

      <span className="w-fit rounded-md border px-2 py-1 text-xs font-medium text-muted-foreground">
        {policy.enabled ? "Aktiv" : "Deaktiviert"}
      </span>
    </article>
  );
};
