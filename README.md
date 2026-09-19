# Harbor

Harbor is a B2B service desk and enterprise service management platform for organizing internal support processes within
companies.

The goal is to provide organizations with a central platform where employees can submit service requests and support
teams can manage and process them.

### Project Status

Harbor is currently in early development.

The current focus is on building a solid foundation for a multi-tenant service management platform with clear separation
between organizations, users, service teams, services and tickets.

Additional infrastructure such as background jobs, queues and real-time communication will be added later.

### Tech Stack

#### Frontend

- React
- TypeScript
- Vite
- Tailwind CSS
- TanStack Query
- TanStack Router
- Axios
- Zod
- React Hook Form

#### Backend

- Java
- Spring Boot
- Maven
- PostgreSQL

### Getting Started

#### Prerequisites

Make sure the following tools are installed:

- Node.js
- npm
- Java
- Maven
- Docker

### Installation
#### 1. Clone the repository

```bash
git clone https://github.com/Marvin348/harbor.git
cd harbor
```

#### 2. Create the environment file

Create a .env file based on the provided example:

cp  client/.env.example /.env server/.env

Then replace the placeholder values in /.env and server/.env with your own configuration.

#### 3. Build and start the frontend

Install the frontend dependencies:

```bash
cd client
npm install
```

Start the development server:

```bash
npm run dev
```

#### 4. Build and start the application
Start the PostgreSQL database:

```bash
docker compose up -d
```

Then start the Spring Boot application from the server directory:
```bash
cd server
./mvnw spring-boot:run
```
Alternatively, the backend can be started directly from IntelliJ IDEA.

#### 5. Seed the local database
After the database is running, populate it with development data:

```bash
./mvnw spring-boot:run -Pseed
```