import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import type { TicketAssessmentRequestImpactEnum } from "@/api/generated/models/ticket-assessment-request.ts";
import { impactOptions } from "@/features/tickets/constants/ticketImpact.ts";

type SelectTicketImpactProps = {
  value?: TicketAssessmentRequestImpactEnum;
  onValueChange: (value: TicketAssessmentRequestImpactEnum | null) => void;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const SelectTicketImpact = ({
  value,
  onValueChange,
  ariaInvalid,
  ariaDescribedBy,
}: SelectTicketImpactProps) => {
  return (
    <Select
      value={value ?? null}
      onValueChange={onValueChange}
      items={impactOptions}
    >
      <SelectTrigger
        className="w-full"
        aria-invalid={ariaInvalid}
        aria-describedby={ariaDescribedBy}
      >
        <SelectValue placeholder="Auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        {impactOptions.map((option) => (
          <SelectItem key={option.value} value={option.value}>
            {option.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
