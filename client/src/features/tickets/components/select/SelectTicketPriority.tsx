import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { TICKET_PRIORITY_OPTIONS } from "@/features/tickets/constants/ticketPriorityLabels.ts";
import type { TicketResponsePriorityEnum } from "@/api/generated/models/ticket-response.ts";

type SelectTicketPriorityProps = {
  value?: TicketResponsePriorityEnum;
  onValueChange: (value?: TicketResponsePriorityEnum) => void;
};

const ALL_OPTION = "Alle";

export const SelectTicketPriority = ({
  value,
  onValueChange,
}: SelectTicketPriorityProps) => {
  return (
    <Select
      value={value ?? ALL_OPTION}
      onValueChange={(selectedValue) => {
        onValueChange(
          selectedValue === ALL_OPTION
            ? undefined
            : (selectedValue as TicketResponsePriorityEnum),
        );
      }}
      items={TICKET_PRIORITY_OPTIONS}
    >
      <SelectTrigger className="w-full">
        <SelectValue placeholder="Auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        <SelectItem value={ALL_OPTION}>Alle</SelectItem>

        {TICKET_PRIORITY_OPTIONS.map((option) => (
          <SelectItem key={option.value} value={option.value}>
            {option.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
