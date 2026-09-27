const messages = [
  {
    id: 1,
    author: "Anna Schneider",
    role: "requester",
    time: "09:42",
    content:
      "Hallo, meine VPN-Verbindung bricht seit heute Morgen regelmäßig ab. Nach wenigen Minuten werde ich getrennt und muss mich erneut anmelden.",
  },
  {
    id: 2,
    author: "Daniel Weber",
    role: "agent",
    time: "09:51",
    content:
      "Hallo Anna, danke für die Meldung. Tritt das Problem nur im WLAN auf oder auch über eine andere Verbindung?",
  },
  {
    id: 3,
    author: "Anna Schneider",
    role: "requester",
    time: "10:03",
    content:
      "Es passiert sowohl im Büro-WLAN als auch über meinen mobilen Hotspot. Einen Neustart habe ich bereits versucht.",
  },
  {
    id: 4,
    author: "Daniel Weber",
    role: "agent",
    time: "10:12",
    content:
      "Danke, das hilft weiter. Ich prüfe gerade die Verbindungsprotokolle und melde mich gleich mit den nächsten Schritten.",
  },
  {
    id: 5,
    author: "Anna Schneider",
    role: "requester",
    time: "10:18",
    content: "Alles klar, vielen Dank.",
  },
] as const;

export const AgentTicketMessageList = () => {
  return (
    <div className="min-h-0 flex-1 overflow-y-auto">
      <div className="mx-auto flex w-full max-w-4xl flex-col gap-5 ">
        {messages.map((message, index) => {
          const isAgent = message.role === "agent";

          return (
            <div key={message.id}>
              {index === 2 && (
                <div className="mb-5 flex items-center gap-3">
                  <span className="h-px flex-1 bg-border" />
                  <p className="rounded-full border border-border bg-background px-3 py-1 text-xs text-muted-foreground">
                    Daniel Weber hat das Ticket übernommen
                  </p>
                  <span className="h-px flex-1 bg-border" />
                </div>
              )}

              <article
                className={`flex ${isAgent ? "justify-end" : "justify-start"}`}
              >
                <div className="max-w-[75%] sm:max-w-[68%]">
                  <div
                    className={`mb-1.5 flex items-center gap-2 text-xs ${
                      isAgent ? "justify-end" : "justify-start"
                    }`}
                  >
                    <span className="font-medium text-foreground">
                      {message.author}
                    </span>
                    <time className="text-muted-foreground">
                      {message.time}
                    </time>
                  </div>

                  <p
                    className={`rounded-xl px-4 py-3 text-sm leading-6 ${
                      isAgent
                        ? "rounded-tr-sm bg-muted"
                        : "rounded-tl-sm border border-border bg-background shadow-xs"
                    }`}
                  >
                    {message.content}
                  </p>
                </div>
              </article>
            </div>
          );
        })}
      </div>
    </div>
  );
};
