import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_app/service-teams_/$id/services')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/_app/service-teams/$id/services"!</div>
}
