import { Link } from "@tanstack/react-router";
import {
  ArrowRight,
  CheckCircle2,
  ChevronRight,
  CircleAlert,
  Clock3,
  UserRound,
} from "lucide-react";
import { Route } from "@/routes/_app.service-teams_.$id.tsx";

export const ServiceTeamOverviewPage = () => {
  const { id } = Route.useParams();

  const ticketsRequiringAttention = [
    {
      id: 1842,
      subject: "VPN-Zugang auf neuem Gerät nicht möglich",
      service: "Identity & Access",
      priority: "Kritisch",
      assignee: "Nicht zugewiesen",
      target: "Erstreaktion in 18 Min.",
      isCritical: true,
    },
    {
      id: 1837,
      subject: "Freigabe für Adobe Creative Cloud steht aus",
      service: "Software & Lizenzen",
      priority: "Hoch",
      assignee: "Jonas Klein",
      target: "Lösung in 1 Std. 42 Min.",
      isCritical: false,
    },
    {
      id: 1829,
      subject: "MacBook-Bereitstellung für neuen Mitarbeiter",
      service: "Workplace Hardware",
      priority: "Mittel",
      assignee: "Mara Seidel",
      target: "Wartet seit 3 Std. auf Freigabe",
      isCritical: false,
    },
    {
      id: 1816,
      subject: "Dockingstation verliert Netzwerkverbindung",
      service: "Workplace Support",
      priority: "Mittel",
      assignee: "David Nguyen",
      target: "Neue Antwort vor 12 Min.",
      isCritical: false,
    },
  ];

  const recentActivity = [
    {
      time: "Vor 8 Min.",
      title: "Ticket #1842 wurde an das Team eskaliert",
      description: "Kritischer Zugriffsfehler · noch nicht zugewiesen",
    },
    {
      time: "Vor 34 Min.",
      title: "Jonas Klein hat Ticket #1837 übernommen",
      description: "Software & Lizenzen",
    },
    {
      time: "Heute, 09:12",
      title: "Service „Mobile Devices“ wurde dem Team zugeordnet",
      description: "Geändert von Clara Hoffmann",
    },
    {
      time: "Gestern, 16:48",
      title: "Ticket #1794 wurde gelöst",
      description: "Wiederherstellung eines gesperrten Benutzerkontos",
    },
  ];

  return (
    <div className="space-y-6">
      <section className="overflow-hidden rounded-md border border-border bg-background">
        <div className="flex flex-col gap-1 border-b border-border px-4 py-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-sm font-semibold">Operative Lage</h2>
            <p className="mt-0.5 text-xs text-muted-foreground">
              Aktuelle Arbeitslast des gesamten Teams
            </p>
          </div>
          <span className="text-xs text-muted-foreground">
            Stand vor 5 Minuten
          </span>
        </div>

        <div className="flex flex-col lg:flex-row">
          <Link
            to="/service-teams/$id/tickets"
            params={{ id }}
            className="group flex flex-1 items-center justify-between gap-4 border-t border-border px-4 py-4 first:border-t-0 hover:bg-muted/40 lg:border-t-0 lg:border-l lg:first:border-l-0"
          >
            <div>
              <p className="text-2xl font-semibold tabular-nums">32</p>
              <p className="mt-0.5 text-sm font-medium">Offene Tickets</p>
              <p className="mt-1 text-xs text-muted-foreground">
                18 aktiv in Bearbeitung
              </p>
            </div>
            <ChevronRight className="size-4 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
          </Link>

          <Link
            to="/service-teams/$id/tickets"
            params={{ id }}
            className="group flex flex-1 items-center justify-between gap-4 border-t border-border px-4 py-4 hover:bg-muted/40 lg:border-t-0 lg:border-l"
          >
            <div>
              <p className="text-2xl font-semibold tabular-nums">6</p>
              <p className="mt-0.5 text-sm font-medium">Nicht zugewiesen</p>
              <p className="mt-1 text-xs text-muted-foreground">
                Benötigen eine erste Sichtung
              </p>
            </div>
            <ChevronRight className="size-4 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
          </Link>

          <Link
            to="/service-teams/$id/tickets"
            params={{ id }}
            className="group flex flex-1 items-center justify-between gap-4 border-t border-border px-4 py-4 hover:bg-muted/40 lg:border-t-0 lg:border-l"
          >
            <div>
              <p className="text-2xl font-semibold tabular-nums">5</p>
              <p className="mt-0.5 text-sm font-medium">SLA gefährdet</p>
              <p className="mt-1 text-xs text-muted-foreground">
                Ziel innerhalb der nächsten 2 Std.
              </p>
            </div>
            <ChevronRight className="size-4 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
          </Link>

          <Link
            to="/service-teams/$id/tickets"
            params={{ id }}
            className="group flex flex-1 items-center justify-between gap-4 border-t border-border px-4 py-4 hover:bg-muted/40 lg:border-t-0 lg:border-l"
          >
            <div>
              <p className="text-2xl font-semibold text-destructive tabular-nums">
                2
              </p>
              <p className="mt-0.5 text-sm font-medium">SLA verletzt</p>
              <p className="mt-1 text-xs text-destructive">
                Sofortige Bearbeitung erforderlich
              </p>
            </div>
            <ChevronRight className="size-4 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
          </Link>
        </div>
      </section>

      <div className="grid items-start gap-6 xl:grid-cols-[minmax(0,1.65fr)_minmax(300px,0.75fr)]">
        <section className="overflow-hidden rounded-md border border-border bg-background">
          <div className="flex items-start justify-between gap-4 border-b border-border px-4 py-3">
            <div>
              <h2 className="text-sm font-semibold">Handlungsbedarf</h2>
              <p className="mt-0.5 text-xs text-muted-foreground">
                Nach Dringlichkeit und nächstem Ziel priorisiert
              </p>
            </div>
            <Link
              to="/service-teams/$id/tickets"
              params={{ id }}
              className="inline-flex shrink-0 items-center gap-1 text-xs font-medium text-muted-foreground transition-colors hover:text-foreground"
            >
              Alle Tickets
              <ArrowRight className="size-3.5" />
            </Link>
          </div>

          <div className="divide-y divide-border">
            {ticketsRequiringAttention.map((ticket) => (
              <Link
                key={ticket.id}
                to="/tickets/$id"
                params={{ id: String(ticket.id) }}
                className="group block px-4 py-3.5 transition-colors hover:bg-muted/40"
              >
                <div className="flex min-w-0 items-start justify-between gap-4">
                  <div className="min-w-0">
                    <div className="flex min-w-0 flex-wrap items-center gap-x-2 gap-y-1">
                      <span className="font-mono text-xs text-muted-foreground">
                        #{ticket.id}
                      </span>
                      <h3 className="truncate text-sm font-medium">
                        {ticket.subject}
                      </h3>
                    </div>
                    <div className="mt-1.5 flex flex-wrap items-center gap-x-2 gap-y-1 text-xs text-muted-foreground">
                      <span>{ticket.service}</span>
                      <span aria-hidden="true">·</span>
                      <span>Priorität: {ticket.priority}</span>
                      <span aria-hidden="true">·</span>
                      <span>{ticket.assignee}</span>
                    </div>
                  </div>

                  <div
                    className={`hidden shrink-0 items-center gap-1.5 text-xs font-medium sm:flex ${
                      ticket.isCritical
                        ? "text-destructive"
                        : "text-muted-foreground"
                    }`}
                  >
                    {ticket.isCritical ? (
                      <CircleAlert className="size-3.5" />
                    ) : (
                      <Clock3 className="size-3.5" />
                    )}
                    {ticket.target}
                  </div>
                </div>

                <div
                  className={`mt-2 flex items-center gap-1.5 text-xs font-medium sm:hidden ${
                    ticket.isCritical
                      ? "text-destructive"
                      : "text-muted-foreground"
                  }`}
                >
                  {ticket.isCritical ? (
                    <CircleAlert className="size-3.5" />
                  ) : (
                    <Clock3 className="size-3.5" />
                  )}
                  {ticket.target}
                </div>
              </Link>
            ))}
          </div>
        </section>

        <div className="space-y-6">
          <section className="rounded-md border border-border bg-background">
            <div className="border-b border-border px-4 py-3">
              <h2 className="text-sm font-semibold">
                Verantwortung &amp; Betrieb
              </h2>
              <p className="mt-0.5 text-xs text-muted-foreground">
                Operativer Rahmen dieses Teams
              </p>
            </div>

            <dl className="divide-y divide-border px-4">
              <div className="py-3">
                <dt className="text-xs text-muted-foreground">Teamleitung</dt>
                <dd className="mt-1 flex items-center gap-2 text-sm font-medium">
                  <span className="flex size-6 items-center justify-center rounded-full border border-border bg-muted text-[10px]">
                    LH
                  </span>
                  Lena Hoffmann
                </dd>
              </div>
              <div className="py-3">
                <dt className="text-xs text-muted-foreground">Servicezeit</dt>
                <dd className="mt-1 text-sm font-medium">
                  Montag–Freitag, 07:00–18:00
                </dd>
              </div>
              <div className="py-3">
                <dt className="text-xs text-muted-foreground">
                  Eskalationsweg
                </dt>
                <dd className="mt-1 text-sm font-medium">
                  IT Operations · Bereitschaft außerhalb der Servicezeit
                </dd>
              </div>
              <div className="py-3">
                <dt className="text-xs text-muted-foreground">Zuständigkeit</dt>
                <dd className="mt-1 text-sm leading-5">
                  Arbeitsplatzgeräte, Standardsoftware, Benutzerzugänge und
                  technischer First-Level-Support.
                </dd>
              </div>
            </dl>
          </section>

          <section className="rounded-md border border-border bg-background">
            <div className="flex items-start justify-between gap-4 border-b border-border px-4 py-3">
              <div>
                <h2 className="text-sm font-semibold">Abdeckung heute</h2>
                <p className="mt-0.5 text-xs text-muted-foreground">
                  5 von 8 Agents verfügbar
                </p>
              </div>
              <Link
                to="/service-teams/$id/agents"
                params={{ id }}
                className="text-xs font-medium text-muted-foreground transition-colors hover:text-foreground"
              >
                Agents ansehen
              </Link>
            </div>

            <div className="px-4 py-4">
              <div className="h-1.5 overflow-hidden rounded-full bg-muted">
                <div className="h-full w-5/8 rounded-full bg-foreground/70" />
              </div>
              <div className="mt-4 grid grid-cols-3 gap-3 text-center">
                <div>
                  <p className="text-base font-semibold tabular-nums">5</p>
                  <p className="mt-0.5 text-xs text-muted-foreground">
                    Verfügbar
                  </p>
                </div>
                <div className="border-l border-border">
                  <p className="text-base font-semibold tabular-nums">2</p>
                  <p className="mt-0.5 text-xs text-muted-foreground">
                    Ausgelastet
                  </p>
                </div>
                <div className="border-l border-border">
                  <p className="text-base font-semibold tabular-nums">1</p>
                  <p className="mt-0.5 text-xs text-muted-foreground">
                    Abwesend
                  </p>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>

      <section className="overflow-hidden rounded-md border border-border bg-background">
        <div className="border-b border-border px-4 py-3">
          <h2 className="text-sm font-semibold">Letzte relevante Aktivität</h2>
          <p className="mt-0.5 text-xs text-muted-foreground">
            Änderungen an Tickets und am Verantwortungsbereich des Teams
          </p>
        </div>

        <ol className="divide-y divide-border">
          {recentActivity.map((activity, index) => (
            <li
              key={activity.title}
              className="grid gap-2 px-4 py-3 sm:grid-cols-[120px_minmax(0,1fr)] sm:gap-4"
            >
              <time className="text-xs text-muted-foreground">
                {activity.time}
              </time>
              <div className="flex min-w-0 items-start gap-2.5">
                <span className="mt-0.5 flex size-5 shrink-0 items-center justify-center text-muted-foreground">
                  {index === 3 ? (
                    <CheckCircle2 className="size-4" />
                  ) : index === 1 ? (
                    <UserRound className="size-4" />
                  ) : (
                    <span className="size-1.5 rounded-full bg-muted-foreground" />
                  )}
                </span>
                <div className="min-w-0">
                  <p className="text-sm font-medium">{activity.title}</p>
                  <p className="mt-0.5 text-xs text-muted-foreground">
                    {activity.description}
                  </p>
                </div>
              </div>
            </li>
          ))}
        </ol>
      </section>
    </div>
  );
};
