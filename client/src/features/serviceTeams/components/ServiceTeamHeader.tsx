export const ServiceTeamHeader = () => {
  return (
    <section className="flex flex-col gap-4 mb-6 xl:flex-row xl:items-end xl:justify-between">
      <div className="max-w-3xl">
        <h1 className="text-2xl font-semibold tracking-normal">
          Service-Teams
        </h1>
        <p className="mt-2 max-w-2xl text-sm leading-6 text-muted-foreground">
          Organisiere die Teams, die interne Services bearbeiten. Jedes Team
          kann später Mitglieder, Services und eigene Zuständigkeiten erhalten.
        </p>
      </div>

      <div className="grid gap-2 sm:grid-cols-3 xl:min-w-[420px]">
        <div className="rounded-md border border-border bg-background px-3 py-2">
          <div className="text-xs text-muted-foreground">Aktive Teams</div>
          <div className="mt-1 text-xl font-semibold">0{/*{activeTeams}*/}</div>
        </div>
        <div className="rounded-md border border-border bg-background px-3 py-2">
          <div className="text-xs text-muted-foreground">
            Teams ohne Mitglieder
          </div>
          <div className="mt-1 text-xl font-semibold">
            0{/*{teamsWithoutMembers}*/}
          </div>
        </div>
        <div className="rounded-md border border-border bg-background px-3 py-2">
          <div className="text-xs text-muted-foreground">
            Verbundene Services
          </div>
          <div className="mt-1 text-xl font-semibold">
            0{/*{connectedServices}*/}
          </div>
        </div>
      </div>
    </section>
  );
};
