import { Skeleton } from "@/components/ui/skeleton.tsx";

export const SlaPolicyListLoadingState = () => {
  return (
    <div className="divide-y divide-border">
      {Array.from({ length: 3 }).map((_, index) => (
        <div
          key={index}
          className="grid gap-4 px-4 py-4 md:grid-cols-[minmax(0,1fr)_9rem_9rem_auto] md:items-center"
        >
          <div className="space-y-2">
            <Skeleton className="h-4 w-40" />
            <Skeleton className="h-5 w-16" />
          </div>
          <div className="space-y-2">
            <Skeleton className="h-3 w-20" />
            <Skeleton className="h-4 w-14" />
          </div>
          <div className="space-y-2">
            <Skeleton className="h-3 w-16" />
            <Skeleton className="h-4 w-14" />
          </div>
          <Skeleton className="h-7 w-14" />
        </div>
      ))}
    </div>
  );
};
