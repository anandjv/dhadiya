# dhadiya-portal

**Dhadiya Enterprise Frontend Console (dhadiya.com)**

`dhadiya-portal` is the unified Angular 18+ web console for **dhadiya.com**. It interfaces with the multi-tenant isolation service (`dhadiya-tenants`) and the unified middleware engine (`dhadiya-flow`) via a high-performance reverse proxy.

## Architecture & Integration
The portal runs on port `4200` (or `80` in Docker) and proxies API calls to:
- **Tenant Isolation:**
  - `/api/v1/realms` &rarr; `http://localhost:8081` (`dhadiya-tenants`)
- **Unified Middleware (WF & DataGrids):**
  - `/api/v1/processes` &rarr; `http://localhost:8082` (`dhadiya-flow`)
  - `/api/v1/forms` &rarr; `http://localhost:8082` (`dhadiya-flow`)
  - `/api/v1/tasks` &rarr; `http://localhost:8082` (`dhadiya-flow`)
  - `/api/v1/schemas` &rarr; `http://localhost:8082` (`dhadiya-flow`)
  - `/api/v1/grid` &rarr; `http://localhost:8082` (`dhadiya-flow`)
  - `/dynamicGrid` &rarr; `http://localhost:8082` (`dhadiya-flow`)

## Features
- **Operations Dashboard:** Live system overview, realm cards, running case statistics, and quick navigation.
- **Showroom Region Isolation:** Multi-tenant territory switcher (`dhadiya-showroom-global`, `dhadiya-showroom-americas`, `dhadiya-showroom-emea`, `dhadiya-showroom-apac`) and new region provisioning.
- **Dynamic Workflow Orchestration:** Start process cases, inspect 5-stage workflows, review transition histories.
- **Task Approval Inbox:** Actionable pending queue with one-click approval/rejection.
- **Dynamic DataGrid & Schemas:** High-speed dynamic schema explorer, column schemas, real-time query pagination, and status badges.
- **⚡ Zora Studio (Low-Code Application Builder):**
  - **Table Studio:** Visual schema modeler to dynamically create custom database tables, columns (text, number, date, select, boolean), and indexes.
  - **Form Studio:** Drag-and-drop dynamic form designer with interactive live form preview, field validation, and dropdown options.
  - **Workflow Studio:** Visual multi-stage process pipeline builder with sequential lifecycle steppers, assignees, and action triggers.

## Development Server
```bash
npm install
npm start
```
Navigate to `http://localhost:4200/`.
