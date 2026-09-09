import { Skeleton } from "@/components/ui/skeleton";

export const ServiceCatalogLoadingState = () => {
  return (
    <div className="divide-y divide-border">
      {Array.from({ length: 3 }).map((_, index) => (
        <div className="px-4 py-4" key={index}>
          <div className="flex items-center gap-2">
            <Skeleton className="h-4 w-40" />
            <Skeleton className="h-5 w-16 rounded-md" />
          </div>

          <Skeleton className="mt-3 h-4 max-w-2xl" />
        </div>
      ))}
    </div>
  );
};
