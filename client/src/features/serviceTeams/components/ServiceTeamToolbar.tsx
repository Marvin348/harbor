import { ArrowUpDown, Search, SlidersHorizontal } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import {
  InputGroup,
  InputGroupAddon,
  InputGroupInput,
} from "@/components/ui/input-group.tsx";
import { useServiceTeamParams } from "@/features/serviceTeams/hooks/useServiceTeamParams.ts";
import { useEffect, useState } from "react";
import { useDebounce } from "@/shared/hooks/useDebounce.ts";

export const ServiceTeamToolbar = () => {
  const { params, setSearch } = useServiceTeamParams();
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
    <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
      <div className="flex min-w-0 flex-1 flex-col gap-2 sm:flex-row sm:items-center">
        <InputGroup className="w-full bg-background sm:w-64">
          <InputGroupAddon>
            <Search className="size-4" />
          </InputGroupAddon>
          <InputGroupInput
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            type="search"
            placeholder="Service-Teams suchen..."
            aria-label="Service-Teams suchen"
          />
        </InputGroup>
      </div>

      <div className="flex items-center gap-2">
        <Button type="button" variant="outline" size="sm">
          <SlidersHorizontal className="size-3.5" />
          Filter
        </Button>
        <Button type="button" variant="outline" size="sm">
          <ArrowUpDown className="size-3.5" />
          Sortieren
        </Button>
      </div>
    </div>
  );
};
