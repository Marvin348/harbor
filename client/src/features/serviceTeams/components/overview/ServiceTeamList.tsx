import { ServiceTeamCard } from "@/features/serviceTeams/components/overview/ServiceTeamCard.tsx";
import type { ServiceTeamListItemResponse } from "@/api/generated/models/service-team-list-item-response.ts";

type ServiceTeamListProps = {
  serviceTeams: ServiceTeamListItemResponse[];
};

export const ServiceTeamList = ({ serviceTeams }: ServiceTeamListProps) => {
  return (
    <div className="w-full">
      {serviceTeams.map((team) => (
        <ServiceTeamCard serviceTeam={team} key={team.id} />
      ))}
    </div>
  );
};
