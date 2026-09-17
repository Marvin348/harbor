import { Skeleton } from "@/components/ui/skeleton.tsx";

export const ServiceTeamOverviewLoadingState = () => {
  return (
    <div className="divide-y divide-border">
      {Array.from({ length: 5 }).map((_, index) => (
        <div className="px-4 py-4" key={index}>
          <div className="flex items-center justify-between gap-4">
            <div className="flex min-w-0 flex-1 items-center gap-2">
              <Skeleton className="h-4 w-56 max-w-full" />
            </div>

            <Skeleton className="h-6 w-24 rounded-md" />
          </div>

          <div className="mt-3 grid gap-2 md:grid-cols-[minmax(0,1fr)_160px_minmax(0,1fr)]">
            <Skeleton className="h-4 w-64 max-w-full" />
            <Skeleton className="h-4 w-28 md:mx-auto" />
            <Skeleton className="h-4 w-72 max-w-full md:ml-auto" />
          </div>
        </div>
      ))}
    </div>
  );
};
