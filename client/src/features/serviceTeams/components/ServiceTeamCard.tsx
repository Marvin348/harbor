import { CalendarDays, ChevronRight, Headset, Layers3 } from "lucide-react";
import { Link } from "@tanstack/react-router";
import type { ServiceTeamResponse } from "@/api/generated/models/service-team-response.ts";

type ServiceTeamCardProps = {
  serviceTeam: ServiceTeamResponse;
};

export const ServiceTeamCard = ({ serviceTeam }: ServiceTeamCardProps) => {
  return (
    <Link
      to="/service-teams/$id"
      params={{ id: String(serviceTeam.id) }}
      className="group flex min-w-0 flex-1 items-center justify-between gap-4 border-b border-border px-4 py-4 text-left transition-colors hover:bg-muted/40"
    >
      <div className="flex min-w-0 items-start gap-3">
        <div className="flex size-10 shrink-0 items-center justify-center rounded-md border bg-muted/30 text-muted-foreground"></div>

        <div className="min-w-0">
          <div className="flex min-w-0 flex-col gap-1 md:flex-row md:items-center md:gap-3">
            <h3 className="truncate text-sm font-medium">{serviceTeam.name}</h3>
            {/*<span className="w-fit rounded-sm border bg-background px-1.5 py-0.5 text-xs text-muted-foreground">*/}
            {/*  Aktiv*/}
            {/*</span>*/}
          </div>

          <p className="mt-1 line-clamp-1 text-xs text-muted-foreground">
            {serviceTeam.description}
          </p>

          <div className="mt-3 flex flex-wrap items-center gap-x-4 gap-y-2 text-xs text-muted-foreground">
            <span className="inline-flex items-center gap-1.5">
              <Headset className="size-3.5" />8 Agents
            </span>
            <span className="inline-flex items-center gap-1.5">
              <Layers3 className="size-3.5" />
              14 Services
            </span>
            <span className="inline-flex items-center gap-1.5">
              <CalendarDays className="size-3.5" />
              Erstellt am 12.09.2026
            </span>
          </div>
        </div>
      </div>

      <ChevronRight className="size-4 shrink-0 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
    </Link>
  );
};
