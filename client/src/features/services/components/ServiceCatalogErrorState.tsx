import { CircleAlert } from "lucide-react";

export const ServiceCatalogErrorState = () => {
  return (
    <div className="flex min-h-[280px] items-center justify-center px-4 py-12">
      <div className="flex max-w-md flex-col items-center text-center">
        <div className="flex size-12 items-center justify-center rounded-md border border-destructive/30 bg-destructive/10">
          <CircleAlert className="size-5 text-destructive" />
        </div>

        <h3 className="mt-5 text-base font-semibold">
          Services konnten nicht geladen werden
        </h3>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Bitte versuchen Sie es erneut oder wenden Sie sich an Ihren
          Administrator, falls das Problem bestehen bleibt.
        </p>
      </div>
    </div>
  );
};
