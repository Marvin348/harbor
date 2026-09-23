import { Skeleton } from "@/components/ui/skeleton.tsx";

const COLUMN_WIDTHS = [
  "w-12",
  "w-48",
  "w-28",
  "w-32",
  "w-16",
  "w-20",
  "w-28",
  "w-20",
];

export const ServiceTeamTicketLoadingState = () => {
  return (
    <section
      aria-label="Tickets werden geladen"
      className="overflow-hidden rounded-md border border-border bg-background"
    >
      <div className="table-scrollbar overflow-x-auto">
        <table className="w-full min-w-275 border-collapse">
          <thead className="bg-muted/40">
            <tr className="border-b border-border">
              {COLUMN_WIDTHS.map((width, index) => (
                <th key={index} className="h-10 px-4">
                  <Skeleton className={`h-3 ${width}`} />
                </th>
              ))}
            </tr>
          </thead>

          <tbody>
            {Array.from({ length: 7 }).map((_, rowIndex) => (
              <tr
                key={rowIndex}
                className="border-b border-border last:border-b-0"
              >
                {COLUMN_WIDTHS.map((width, columnIndex) => (
                  <td key={columnIndex} className="h-13 px-4 py-3">
                    <Skeleton className={`h-4 ${width}`} />
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="flex min-h-14 items-center justify-between gap-4 border-t border-border px-4 py-3">
        <Skeleton className="h-4 w-40" />
        <div className="flex items-center gap-3">
          <Skeleton className="h-4 w-24" />
          <Skeleton className="size-8 rounded-md" />
          <Skeleton className="size-8 rounded-md" />
        </div>
      </div>
    </section>
  );
};
