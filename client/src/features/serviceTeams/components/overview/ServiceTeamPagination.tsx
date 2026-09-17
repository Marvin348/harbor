import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { useServiceTeamParams } from "@/features/serviceTeams/hooks/useServiceTeamParams.ts";

type ServiceTeamPaginationProps = {
  currentPage: number;
  totalPages: number;
  totalElements: number;
};

export const ServiceTeamPagination = ({
  currentPage,
  totalElements,
  totalPages,
}: ServiceTeamPaginationProps) => {
  const { setPage } = useServiceTeamParams();

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
              variant={pageNumber === currentPage ? "default" : "outline"}
              size="icon-sm"
              aria-current={pageNumber === currentPage ? "page" : undefined}
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
