import { ChevronDown } from "lucide-react";
import {
  mapAgentTicketWorkflowFields,
  type AgentTicketWorkflowDetails,
} from "@/features/tickets/agent/mappers/mapAgentTicketWorkflowFields.ts";

type AgentTicketWorkflowBarProps = {
  workflowDetails: AgentTicketWorkflowDetails;
};

export const AgentTicketWorkflowBar = ({
  workflowDetails,
}: AgentTicketWorkflowBarProps) => {
  const workflowFields = mapAgentTicketWorkflowFields(workflowDetails);

  return (
    <section className="overflow-hidden rounded-md border border-border bg-background">
      <div className="border-b border-border px-4 py-3">
        <h2 className="text-sm font-semibold">Ticket steuern</h2>
        <p className="mt-0.5 text-xs text-muted-foreground">
          Workflow und Zuständigkeit direkt bearbeiten
        </p>
      </div>

      <div className="flex flex-col lg:flex-row">
        {workflowFields.map((field) => (
          <button
            key={field.label}
            type="button"
            className="flex flex-1 items-center justify-between gap-4 border-t border-border px-4 py-3 text-left transition-colors first:border-t-0 hover:bg-muted/40 lg:border-t-0 lg:border-l lg:first:border-l-0"
          >
            <span className="min-w-0">
              <span className="block text-xs text-muted-foreground">
                {field.label}
              </span>
              <span className="mt-1 block truncate text-sm font-medium">
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
