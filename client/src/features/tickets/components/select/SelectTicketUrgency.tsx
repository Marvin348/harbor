import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import type { TicketAssessmentRequestUrgencyEnum } from "@/api/generated/models/ticket-assessment-request.ts";
import { urgencyOptions } from "@/features/tickets/constants/ticketUrgency.ts";

type SelectTicketUrgencyProps = {
  value?: TicketAssessmentRequestUrgencyEnum;
  onValueChange: (value: TicketAssessmentRequestUrgencyEnum | null) => void;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const SelectTicketUrgency = ({
  value,
  onValueChange,
  ariaInvalid,
  ariaDescribedBy,
}: SelectTicketUrgencyProps) => {
  return (
    <Select
      value={value ?? null}
      onValueChange={onValueChange}
      items={urgencyOptions}
    >
      <SelectTrigger
        className="w-full"
        aria-invalid={ariaInvalid}
        aria-describedby={ariaDescribedBy}
      >
        <SelectValue placeholder="Auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        {urgencyOptions.map((option) => (
          <SelectItem key={option.value} value={option.value}>
            {option.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
