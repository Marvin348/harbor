import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_app/service-teams_/$id/tickets')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/_app/service-teams/$id/tickets"!</div>
}
