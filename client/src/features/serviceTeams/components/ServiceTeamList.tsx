import { ServiceTeamCard } from "@/features/serviceTeams/components/ServiceTeamCard.tsx";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";

type ServiceTeamListProps = {
  serviceTeams: ServiceTeamResponse[];
};

export const ServiceTeamList = ({ serviceTeams }: ServiceTeamListProps) => {
  return (
    <div className="w-full">
      {serviceTeams.map((team) => (
        <ServiceTeamCard serviceTeam={team} key={team.name} />
      ))}
    </div>
  );
};
