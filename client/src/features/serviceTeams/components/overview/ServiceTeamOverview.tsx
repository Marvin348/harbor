import { ServiceTeamToolbar } from "@/features/serviceTeams/components/ServiceTeamToolbar.tsx";
import { ServiceTeamsEmptyState } from "@/features/serviceTeams/components/overview/state/ServiceTeamsEmptyState.tsx";
import { ServiceTeamList } from "@/features/serviceTeams/components/overview/ServiceTeamList.tsx";
import { ServiceTeamPagination } from "@/features/serviceTeams/components/overview/ServiceTeamPagination.tsx";
import { useGetServiceTeams } from "@/features/serviceTeams/hooks/useGetServiceTeams.ts";
import { ServiceTeamOverviewLoadingState } from "@/features/serviceTeams/components/overview/state/ServiceTeamOverviewLoadingState.tsx";
import { ServiceTeamOverviewErrorState } from "@/features/serviceTeams/components/overview/state/ServiceTeamOverviewErrorState.tsx";

type ServiceTeamOverviewProps = {
  onCreateTeam: () => void;
};

export const ServiceTeamOverview = ({
  onCreateTeam,
}: ServiceTeamOverviewProps) => {
  const {
    serviceTeams,
    totalElements,
    totalPages,
    currentPage,
    isLoading,
    isError,
  } = useGetServiceTeams();

  return (
    <section className="rounded-md border border-border bg-background">
      <div className="flex flex-col gap-3 border-b border-border px-4 py-4 md:flex-row md:items-center md:justify-between">
        <div>
          <h2 className="text-base font-semibold">Service-Teams</h2>
          <p className="mt-1 text-sm text-muted-foreground">
            Bestehende Teams und ihre aktuellen Service-Zuordnungen.
          </p>
        </div>
        <ServiceTeamToolbar />
      </div>

      {isLoading ? (
        <ServiceTeamOverviewLoadingState />
      ) : isError ? (
        <ServiceTeamOverviewErrorState />
      ) : totalElements === 0 ? (
        <ServiceTeamsEmptyState onCreateTeam={onCreateTeam} />
      ) : (
        <div className="flex min-w-0 items-stretch">
          <ServiceTeamList serviceTeams={serviceTeams} />
        </div>
      )}
      {totalElements > 0 && (
        <ServiceTeamPagination
          currentPage={currentPage}
          totalPages={totalPages}
          totalElements={totalElements}
        />
      )}
    </section>
  );
};
