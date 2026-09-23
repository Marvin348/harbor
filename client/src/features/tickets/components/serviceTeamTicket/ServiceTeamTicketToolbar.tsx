import { Search } from "lucide-react";
import { Input } from "@/components/ui/input.tsx";
import { SelectTicketStatus } from "@/features/tickets/components/select/SelectTicketStatus.tsx";
import { SelectTicketPriority } from "@/features/tickets/components/select/SelectTicketPriority.tsx";
import { useServiceTeamTicketParams } from "@/features/serviceTeams/hooks/useServcieTeamTicketParams.ts";
import { useEffect, useState } from "react";
import { useDebounce } from "@/shared/hooks/useDebounce.ts";

export const ServiceTeamTicketToolbar = () => {
  const { params, setSearch, setPriority, setStatus } =
    useServiceTeamTicketParams();
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
    <section
      aria-label="Ticket-Filter"
      className="mb-6 flex flex-col gap-4 md:flex-row md:items-center md:justify-between"
    >
      <div className="w-full  lg:max-w-md">
        <label
          htmlFor="service-team-ticket-search"
          className="text-xs font-medium text-foreground"
        >
          Tickets durchsuchen
        </label>
        <div className="relative">
          <Search className="pointer-events-none absolute top-1/2 left-2.5 size-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            id="service-team-ticket-search"
            type="search"
            placeholder="Betreff suchen"
            className="pl-8"
          />
        </div>
      </div>

      <div className="grid w-full gap-3 sm:grid-cols-2 lg:w-auto lg:min-w-90">
        <div className="space-y-1.5">
          <p className="text-xs font-medium text-foreground">Status</p>
          <SelectTicketStatus value={params.status} onValueChange={setStatus} />
        </div>

        <div className="space-y-1.5">
          <p className="text-xs font-medium text-foreground">Priorität</p>
          <SelectTicketPriority
            value={params.priority}
            onValueChange={setPriority}
          />
        </div>
      </div>
    </section>
  );
};
