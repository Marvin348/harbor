import type { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { TICKET_PRIORITY_OPTIONS } from "@/features/tickets/constants/ticketPriorityLabels.ts";

type SlaPolicyPrioritySelectProps = {
  value?: TicketResponsePriorityEnum;
  onValueChange: (value: TicketResponsePriorityEnum) => void;
  ariaInvalid?: boolean;
  ariaDescribedBy?: string;
};

export const SlaPolicyPrioritySelect = ({
  value,
  onValueChange,
  ariaInvalid,
  ariaDescribedBy,
}: SlaPolicyPrioritySelectProps) => {
  return (
    <Select
      value={value ?? null}
      onValueChange={(selectedValue) => {
        if (selectedValue !== null) {
          onValueChange(selectedValue as TicketResponsePriorityEnum);
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
