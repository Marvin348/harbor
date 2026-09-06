import { ChevronRight } from "lucide-react";
import { Link } from "react-router-dom";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";

type ServiceTeamCardProps = {
  serviceTeam: ServiceTeamResponse;
};

export const ServiceTeamCard = ({ serviceTeam }: ServiceTeamCardProps) => {
  return (
    <Link to="/services" className="min-w-0 flex-1 divide-y divide-border">
      <button
        type="button"
        className="flex w-full items-center justify-between gap-4 px-4 py-4 text-left transition-colors hover:bg-muted/40"
      >
        <div className="flex min-w-0 items-start gap-3">
          <div className="flex size-10 shrink-0 rounded-md items-center justify-center border">
            {/*<Icon className="size-4" />*/}
          </div>

          <div className="min-w-0">
            <h3 className="truncate text-sm font-medium">{serviceTeam.name}</h3>
            {/*<p className="mt-1 text-sm text-muted-foreground">*/}
            {/*  {serviceTeam.members} Mitglieder · {serviceTeam.services} Services*/}
            {/*</p>*/}
            <p className="mt-1 line-clamp-1 text-xs text-muted-foreground">
              {serviceTeam.description}
            </p>
          </div>
        </div>

        <ChevronRight className="size-4 shrink-0 text-muted-foreground" />
      </button>
    </Link>
  );
};
