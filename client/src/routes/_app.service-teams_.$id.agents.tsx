import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_app/service-teams_/$id/agents')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/_app/service-teams/$id/agents"!</div>
}
