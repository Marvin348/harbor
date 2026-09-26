import type { AgentTicketDetailsResponse } from "@/api/generated/models/agent-ticket-details-response.ts";
import { BUSINESS_CRITICALITY_LABELS } from "@/features/tickets/constants/ticketBusinessCriticality.ts";
import { IMPACT_LABELS } from "@/features/tickets/constants/ticketImpact.ts";
import { URGENCY_LABELS } from "@/features/tickets/constants/ticketUrgency.ts";
import { ChevronDown } from "lucide-react";

type AgentTicketClassificationSectionProps = {
  impact: AgentTicketDetailsResponse["impact"];
  urgency: AgentTicketDetailsResponse["urgency"];
  businessCriticality: AgentTicketDetailsResponse["businessCriticality"];
};

export const AgentTicketClassificationSection = ({
  impact,
  urgency,
  businessCriticality,
}: AgentTicketClassificationSectionProps) => {
  const classificationFields = [
    { label: "Kategorie", value: "Netzwerk & Zugriff" },
    { label: "Impact", value: IMPACT_LABELS[impact] },
    { label: "Urgency", value: URGENCY_LABELS[urgency] },
    {
      label: "Business Criticality",
      value: BUSINESS_CRITICALITY_LABELS[businessCriticality],
    },
  ];

  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold">Klassifizierung</h2>
        <p className="mt-0.5 text-xs text-muted-foreground">
          Grundlage für Priorität und Routing
        </p>
      </div>

      <div className="divide-y divide-border">
        {classificationFields.map((field) => (
          <button
            key={field.label}
            type="button"
            className="flex w-full items-center justify-between gap-4 px-4 py-3 text-left transition-colors hover:bg-muted/40"
          >
            <span>
              <span className="block text-xs text-muted-foreground">
                {field.label}
              </span>
              <span className="mt-1 block text-sm font-medium">
                {field.value}
              </span>
            </span>
            <ChevronDown className="size-3.5 shrink-0 text-muted-foreground" />
          </button>
        ))}
      </div>
    </section>
  );
};
