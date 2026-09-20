import { Outlet } from "@tanstack/react-router";
import { Route } from "@/routes/_app.service-teams_.$id.tsx";
import { ServiceTeamDetailsHeader } from "@/features/serviceTeams/components/details/ServiceTeamDetailsHeader.tsx";
import { useGetServiceTeamDetails } from "@/features/serviceTeams/hooks/useGetServiceTeamDetails.ts";
import { ServiceTeamDetailsLoadingState } from "@/features/serviceTeams/components/details/ServiceTeamDetailsLoadingState.tsx";
import { ServiceTeamDetailsErrorState } from "@/features/serviceTeams/components/details/ServiceTeamDetailsErrorState.tsx";

export const ServiceTeamDetailsLayout = () => {
  const { id } = Route.useParams();

  const { serviceTeamDetails, isLoading, isError, refetch } =
    useGetServiceTeamDetails(Number(id));

  if (isLoading) return <ServiceTeamDetailsLoadingState />;
  if (isError || !serviceTeamDetails)
    return <ServiceTeamDetailsErrorState onRetry={() => void refetch()} />;

  return (
    <div className="mx-auto w-full max-w-screen-2xl">
      <ServiceTeamDetailsHeader
        serviceTeamDetails={serviceTeamDetails}
        id={id}
      />

      <div className="pt-6">
        <Outlet />
      </div>
    </div>
  );
};
