import { Button } from "@/components/ui/button.tsx";
import { ChevronLeft, ChevronRight } from "lucide-react";

type TablePaginationProps = {
  currentPage: number;
  totalPages: number;
  totalElements: number;
  pageSize: number;
  currentItemsCount: number;
  onPageChange: (currentPage: number) => void;
};

export const TablePagination = ({
  currentPage,
  totalPages,
  totalElements,
  pageSize,
  currentItemsCount,
  onPageChange,
}: TablePaginationProps) => {
  const firstVisibleTicket =
    totalElements === 0 ? 0 : (currentPage - 1) * pageSize + 1;

  const lastVisibleTicket =
    totalElements === 0
      ? 0
      : Math.min(firstVisibleTicket + currentItemsCount - 1, totalElements);

  const goToPreviousPage = () => onPageChange(Math.max(currentPage - 1, 1));
  const goToNextPage = () =>
    onPageChange(Math.min(currentPage + 1, totalPages));

  return (
    <div className="flex flex-col gap-3 border-t border-border px-4 py-3 text-sm text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
      <p>
        {firstVisibleTicket}–{lastVisibleTicket} von {totalElements} Tickets
      </p>

      <div className="flex items-center gap-2">
        <span className="mr-1 text-xs">
          Seite {totalPages === 0 ? 0 : currentPage} von {totalPages}
        </span>
        <Button
          type="button"
          variant="outline"
          size="icon-sm"
          aria-label="Vorherige Seite"
          disabled={currentPage <= 1 || totalPages === 0}
          onClick={goToPreviousPage}
        >
          <ChevronLeft />
        </Button>
        <Button
          type="button"
          variant="outline"
          size="icon-sm"
          aria-label="Nächste Seite"
          disabled={currentPage >= totalPages || totalPages === 0}
          onClick={goToNextPage}
        >
          <ChevronRight />
        </Button>
      </div>
    </div>
  );
};
