import type { SlaPolicyResponseTicketPriorityEnum } from "@/api/generated/models/sla-policy-response.ts";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { TICKET_PRIORITY_OPTIONS } from "@/features/tickets/constants/ticketPriorityLabels.ts";

type SlaPolicyPrioritySelectProps = {
  value?: SlaPolicyResponseTicketPriorityEnum;
  onValueChange?: (value: SlaPolicyResponseTicketPriorityEnum) => void;
  disabled?: boolean;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const SlaPolicyPrioritySelect = ({
  value,
  onValueChange,
  disabled,
  ariaInvalid,
  ariaDescribedBy,
}: SlaPolicyPrioritySelectProps) => {
  return (
    <Select
      value={value ?? null}
      disabled={disabled}
      onValueChange={(selectedValue) => {
        if (selectedValue !== null && onValueChange) {
          onValueChange(selectedValue as SlaPolicyResponseTicketPriorityEnum);
        }
      }}
      items={TICKET_PRIORITY_OPTIONS}
    >
      <SelectTrigger
        className="w-full"
        aria-invalid={ariaInvalid}
        aria-describedby={ariaDescribedBy}
      >
        <SelectValue placeholder="Priorität auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        {TICKET_PRIORITY_OPTIONS.map((option) => (
          <SelectItem key={option.value} value={option.value}>
            {option.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
