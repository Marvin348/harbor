import { Link } from "@tanstack/react-router";
import { ArrowLeft, Ellipsis, Pencil } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { SERVICE_TEAM_DETAILS_NAVIGATION_ITEMS } from "@/features/serviceTeams/constants/serviceTeamDetailsNavigation.ts";
import type { ServiceTeamDetailsResponse } from "@/api/generated/models/service-team-details-response.ts";

type ServiceTeamDetailsHeaderProps = {
  serviceTeamDetails: ServiceTeamDetailsResponse;
  id: string;
};

export const ServiceTeamDetailsHeader = ({
  serviceTeamDetails,
  id,
}: ServiceTeamDetailsHeaderProps) => {
  const { name, agentCount, description, serviceCount } = serviceTeamDetails;
  return (
    <header className="border-b border-border">
      <Link
        to="/service-teams"
        search={{ page: 1 }}
        className="mb-5 inline-flex items-center gap-1.5 text-sm text-muted-foreground transition-colors hover:text-foreground"
      >
        <ArrowLeft className="size-3.5" />
        Service-Teams
      </Link>

      <div className="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">
        <div className="flex min-w-0 items-start gap-4">
          <div className="min-w-0">
            <div className="flex flex-wrap items-center gap-x-3 gap-y-2">
              <h1 className="truncate text-2xl font-semibold tracking-tight">
                {name}
              </h1>
              <span className="inline-flex items-center gap-1.5 rounded-md border border-border px-2 py-0.5 text-xs font-medium text-muted-foreground">
                <span className="size-1.5 rounded-full bg-foreground/60" />
                Aktiv
              </span>
            </div>

            <p className="mt-1.5 max-w-3xl text-sm leading-6 text-muted-foreground">
              {description}
            </p>

            <div className="mt-3 flex flex-wrap items-center gap-x-4 gap-y-1 text-xs text-muted-foreground">
              <span>Team-ID {id}</span>
              <span aria-hidden="true">·</span>
              <span>{agentCount} Agents</span>
              <span aria-hidden="true">·</span>
              <span>{serviceCount} zugeordnete Services</span>
            </div>
          </div>
        </div>

        <div className="flex shrink-0 items-center gap-2">
          <Button type="button" variant="outline">
            <Pencil />
            Team bearbeiten
          </Button>
          <Button
            type="button"
            variant="outline"
            size="icon"
            aria-label="Weitere Team-Aktionen"
          >
            <Ellipsis />
          </Button>
        </div>
      </div>

      <nav
        aria-label="Service-Team-Bereiche"
        className="mt-7 flex gap-6 overflow-x-auto"
      >
        {SERVICE_TEAM_DETAILS_NAVIGATION_ITEMS.map((item) => (
          <Link
            key={item.label}
            to={item.to}
            params={{ id }}
            activeOptions={{ exact: true }}
            className="shrink-0 pb-3 text-sm font-medium text-muted-foreground transition-colors"
            activeProps={{
              className: "text-rose-800",
            }}
          >
            {item.label}
          </Link>
        ))}
      </nav>
    </header>
  );
};
