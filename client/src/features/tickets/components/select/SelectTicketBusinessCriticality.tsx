import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import type { TicketAssessmentRequestBusinessCriticalityEnum } from "@/api/generated/models/ticket-assessment-request.ts";
import { businessCriticalityOptions } from "@/features/tickets/constants/ticketBusinessCriticality.ts";

type SelectTicketBusinessCriticalityProps = {
  value?: TicketAssessmentRequestBusinessCriticalityEnum;
  onValueChange: (
    value: TicketAssessmentRequestBusinessCriticalityEnum | null,
  ) => void;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const SelectTicketBusinessCriticality = ({
  value,
  onValueChange,
  ariaInvalid,
  ariaDescribedBy,
}: SelectTicketBusinessCriticalityProps) => {
  return (
    <Select
      value={value ?? null}
      onValueChange={onValueChange}
      items={businessCriticalityOptions}
    >
      <SelectTrigger
        className="w-full"
        aria-invalid={ariaInvalid}
        aria-describedby={ariaDescribedBy}
      >
        <SelectValue placeholder="Auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        {businessCriticalityOptions.map((option) => (
          <SelectItem key={option.value} value={option.value}>
            {option.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
