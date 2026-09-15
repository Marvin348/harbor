import { Search } from "lucide-react";
import { Input } from "@/components/ui/input.tsx";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { TICKET_STATUS_FILTER_OPTIONS } from "@/features/tickets/constants/ticketStatusFilterOptions.ts";
import { useRequesterTicketParams } from "@/features/tickets/hooks/useRequesterTicketParams.ts";
import type { TicketResponseStatusEnum } from "@/api/generated/models/ticket-response.ts";
import { useEffect, useState } from "react";
import { useDebounce } from "@/shared/hooks/useDebounce.ts";

export const TicketToolbar = () => {
  const { params, setStatus, setSearch } = useRequesterTicketParams();
  const [searchValue, setSearchValue] = useState(params.search ?? "");

  const debouncedSearch = useDebounce(searchValue, 300);

  useEffect(() => {
    const nextSearch = debouncedSearch.trim() || undefined;

    if (nextSearch === params.search) {
      return;
    }

    setSearch(nextSearch);
  }, [debouncedSearch, params.search, setSearch]);

  return (
    <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
      <div className="relative">
        <Search className="pointer-events-none absolute top-1/2 left-2.5 size-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          className="w-full pl-8 sm:w-64"
          placeholder="Anfragen suchen"
        />
      </div>

      <Select
        value={params.status ?? "ALL"}
        onValueChange={(value) =>
          setStatus(
            value === "ALL" ? undefined : (value as TicketResponseStatusEnum),
          )
        }
        items={TICKET_STATUS_FILTER_OPTIONS}
      >
        <SelectTrigger className="w-full sm:w-40">
          <SelectValue />
        </SelectTrigger>

        <SelectContent alignItemWithTrigger={false} className="p-1">
          {TICKET_STATUS_FILTER_OPTIONS.map((option) => (
            <SelectItem key={option.value} value={option.value}>
              {option.label}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  );
};
