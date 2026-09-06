# Harbor

Harbor is a multi-tenant B2B service management application.

## Architecture

- Monorepo:
    - `server/`: Java + Spring Boot
    - `client/`: React + TypeScript + Vite
- PostgreSQL with Flyway migrations
- Redis for session infrastructure
- Backend uses a feature-based package structure
- Prefer simple, explicit architecture over unnecessary abstraction

## Backend structure

- Base package: `com.harbor.server`
- Feature code lives under `features/<feature>`
- Cross-cutting code lives under `common`
- Keep feature-specific code inside its feature package
- Do not move feature-specific logic into `common` just to reuse a small amount of code
- Controllers should stay thin
- Business logic belongs in services
- Database access belongs in repositories
- Validation belongs at the API boundary where possible
- API entities must not be returned directly; use response DTOs

Example:

```text
features/
└── serviceteam/
    ├── controller/
    ├── dto/
    ├── entity/
    ├── repository/
    └── service/
```

## Backend naming conventions

### Classes

Use explicit names that describe responsibility.

Examples:

- `ServiceTeamController`
- `ServiceTeamService`
- `ServiceTeamRepository`
- `ServiceTeam`
- `CreateServiceTeamRequest`
- `UpdateServiceTeamRequest`
- `ServiceTeamResponse`

Do not use vague names such as:

- `ServiceTeamData`
- `ServiceTeamHandler`
- `ServiceTeamManager`
- `Utils`
- `Helper`

unless the responsibility genuinely matches the name.

### DTOs

Request DTOs:

- `Create<Resource>Request`
- `Update<Resource>Request`
- `<Action><Resource>Request` for action-specific requests

Response DTOs:

- `<Resource>Response`
- `<Resource>SummaryResponse` for reduced representations

Examples:

- `CreateServiceTeamRequest`
- `UpdateServiceTeamRequest`
- `ServiceTeamResponse`
- `ServiceTeamSummaryResponse`

### Service methods

Use verbs that describe the operation.

Preferred:

- `createServiceTeam`
- `getServiceTeam`
- `getServiceTeams`
- `updateServiceTeam`
- `deleteServiceTeam`
- `addMember`
- `removeMember`

Avoid vague names such as:

- `handle`
- `process`
- `execute`
- `doStuff`

unless the surrounding abstraction intentionally represents a generic command or process.

## Multi-tenancy and authorization

Harbor is multi-tenant. Tenant isolation is a hard requirement.

- Tenant ownership must always be derived from the authenticated user
- Never accept `organizationId` from frontend requests for tenant-owned resources
- Every tenant-owned database lookup must be scoped to the authenticated organization
- Never retrieve a tenant-owned resource globally and check ownership only afterward when the query can be scoped
  directly
- Authorization checks belong in the backend even if the frontend hides the corresponding action
- Do not trust IDs received from the client to imply ownership

Preferred:

```text
repository.findByIdAndOrganizationId(resourceId, organizationId);
```

instead of:

```text
repository.findById(resourceId);
```

followed by a later tenant check.

## Database and persistence

- PostgreSQL is the source of truth for persistent business data
- Use Flyway for every schema change
- Never edit an already applied migration
- Create a new migration for every schema change
- Database constraints should enforce invariants that must always hold
- Use foreign keys where relationships require referential integrity
- Use unique constraints where duplicates must never exist
- Do not rely only on application-level checks for uniqueness

Before implementing a write operation, explicitly consider:

- Does this operation require a transaction?
- Can two requests modify the same state concurrently?
- Can a race condition violate a business invariant?
- Should the database enforce the invariant with a constraint?
- Does the operation need idempotency?
- Could duplicate requests create duplicate data?

Use transactions when multiple database changes must succeed or fail together.

Do not add transactions around read-only logic without a concrete reason.

## Error handling

- Use domain-appropriate exceptions
- Do not expose internal exception details or stack traces through the API
- Keep HTTP-specific response handling centralized where practical
- Use consistent error response DTOs
- Prefer specific error messages over generic `"Something went wrong"`

## Frontend conventions

- React + TypeScript
- TanStack Query for server state
- React Hook Form + Zod for forms
- shadcn/ui for reusable UI primitives
- Feature-based structure
- Feature-specific components stay inside their feature
- Keep components reasonably small
- Prefer neutral enterprise UI
- Do not introduce arbitrary colors
- Do not add global Zustand state for local component or page state
- Prefer TanStack Query cache over copying server state into Zustand
- Keep API interaction outside purely presentational components where practical

## TypeScript naming

- Components: `PascalCase`
- Hooks: `useSomething`
- Functions and variables: `camelCase`
- Types and interfaces: `PascalCase`
- Constants: `UPPER_SNAKE_CASE` when truly constant
- Boolean names should read naturally as predicates

Preferred:

```ts
isLoading
hasPermission
canDeleteServiceTeam
```

Avoid:

```ts
loadingFlag
permissionBoolean
deleteAllowedValue
```

## Files and components

React component files use PascalCase:

```text
ServiceTeamList.tsx
CreateServiceTeamDialog.tsx
```

Hooks use camelCase:

```text
useServiceTeams.ts
useCreateServiceTeam.ts
```

Schema files should clearly describe their purpose:

```text
serviceTeamSchema.ts
```

Avoid generic files such as:

```text
helpers.ts
utils.ts
types.ts
```

when a more specific domain-oriented name is possible.

### Constants

Use `UPPER_SNAKE_CASE`.

Example:

```text
MAX_SERVICE_TEAM_NAME_LENGTH
```

## TanStack Query

- Use TanStack Query for all server state
- Query keys must be stable and hierarchical
- Do not copy query data into Zustand
- Mutations should invalidate or update only relevant queries
- Keep API requests outside React components

## Forms

- Use React Hook Form with Zod
- Define validation schemas outside the component
- Infer TypeScript form types from Zod where practical
- Display backend validation errors separately from client-side validation errors

## Development rules

- Do not modify generated OpenAPI model files manually
- Do not add dependencies without a clear reason
- Prefer existing project dependencies and patterns
- Do not redesign unrelated code while implementing a focused task
- Do not perform broad refactors unless explicitly requested
- Follow existing project patterns before introducing a new abstraction
- Keep changes focused on the requested feature
- Do not silently weaken validation, authorization, tenant isolation, database constraints, or error handling to make an
  implementation easier
- Run relevant tests and build checks after changes
- If tests cannot be run, state why

## When implementing new features

Before writing code:

1. Inspect the existing feature structure and follow established patterns.
2. Identify which layer owns the responsibility.
3. Identify tenant and authorization requirements.
4. Identify database constraints and relationships.
5. Consider transaction boundaries and concurrency risks.
6. Reuse existing abstractions where they fit naturally.
7. Avoid creating abstractions for hypothetical future requirements.

After implementation:

1. Run relevant tests.
2. Run the relevant backend or frontend build/type checks.
3. Check that tenant isolation has not been bypassed.
4. Check that API entities are not exposed directly.
5. Check that schema changes use a new Flyway migration.
