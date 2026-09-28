import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_app/tickets_/$id/activity')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Hello "/_app/tickets_/$id/activity"!</div>
}
