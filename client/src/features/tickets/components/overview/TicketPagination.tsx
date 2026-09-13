import { ChevronLeft, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { useRequesterTicketParams } from "@/features/tickets/hooks/useRequesterTicketParams.ts";

type TicketPaginationProps = {
  currentPage: number;
  totalPages: number;
  totalElements: number;
};

export const TicketPagination = ({
  currentPage,
  totalPages,
  totalElements,
}: TicketPaginationProps) => {
  const { setPage } = useRequesterTicketParams();

  const prevPage = () => setPage(Math.max(currentPage - 1, 1));
  const nextPage = () => setPage(Math.min(currentPage + 1, totalPages));

  const pages = Array.from({ length: totalPages }, (_, i) => i + 1);
  return (
    <div className="flex flex-col gap-3 border-t border-border px-4 py-3 text-sm text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
      <p>
        Zeigt {currentPage}-{totalPages} von {totalElements} Anfragen
      </p>

      <div className="flex items-center gap-2">
        <Button
          type="button"
          variant="outline"
          size="sm"
          disabled={currentPage === 1}
          onClick={prevPage}
        >
          <ChevronLeft />
          Zurück
        </Button>

        <div className="flex items-center gap-1">
          {pages.map((pageNumber) => (
            <Button
              key={pageNumber}
              type="button"
              variant="outline"
              size="icon-sm"
              aria-label={`Seite ${pageNumber}`}
              onClick={() => setPage(pageNumber)}
            >
              {pageNumber}
            </Button>
          ))}
        </div>

        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={() => nextPage()}
          disabled={currentPage === totalPages}
        >
          Weiter
          <ChevronRight />
        </Button>
      </div>
    </div>
  );
};
