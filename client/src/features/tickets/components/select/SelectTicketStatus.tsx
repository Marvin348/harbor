import type { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { TICKET_STATUS_OPTIONS } from "@/features/tickets/constants/ticketStatusLabels.ts";

type SelectTicketStatusProps = {
  value?: TicketResponseStatusEnum;
  onValueChange: (value?: TicketResponseStatusEnum) => void;
};

const ALL_OPTION = "Alle";

export const SelectTicketStatus = ({
  value,
  onValueChange,
}: SelectTicketStatusProps) => {
  return (
    <Select
      value={value ?? ALL_OPTION}
      onValueChange={(selectedValue) => {
        onValueChange(
          selectedValue === ALL_OPTION
            ? undefined
            : (selectedValue as TicketResponseStatusEnum),
        );
      }}
      items={TICKET_STATUS_OPTIONS}
    >
      <SelectTrigger className="w-full">
        <SelectValue placeholder="Auswählen" />
      </SelectTrigger>

      <SelectContent alignItemWithTrigger={false} className="p-1">
        <SelectItem value={ALL_OPTION}>Alle</SelectItem>

        {TICKET_STATUS_OPTIONS.map((option) => (
          <SelectItem key={option.value} value={option.value}>
            {option.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
};
