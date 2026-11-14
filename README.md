# Dhadiya Global Showroom Management Platform (dhadiya.com)

The **Dhadiya Global Showroom Management Platform** is a 3-tier enterprise cloud suite designed for luxury automotive, flagship retail boutiques, and worldwide showroom dealership networks. It features a unified frontend console with **Zora Studio**, dedicated **Tenant & Territory Isolation**, and a unified **dhadiya-flow Middleware Engine** executing both **eFormsPi Workflow Processes** (Vehicle PDI & Handover) and **Dynamic Floor Inventory DataGrids**.

---

## 🏛️ Architecture: 3-Tier Showroom Suite

| Tier | Project Name | Technology Stack | Port | Responsibilities & Domain |
|---|---|---|---|---|
| **Frontend** | [**`dhadiya-portal`**](file:///Users/apple/zora/dhadiya/dhadiya-portal) | Angular 18+, TypeScript, Plus Jakarta Sans | **4200** | Unified Showroom Web Console: **Zora Studio** (Create Tables, Dynamic Forms, Workflows), Region Management, Task Inbox, Inventory DataGrids |
| **Territory Isolation** | [**`dhadiya-tenants`**](file:///Users/apple/zora/dhadiya/dhadiya-tenants) | Spring Boot 3.2, JPA, H2 / PostgreSQL | **8081** | Multi-Tenant Showroom Realm Isolation API (`showrooms.dhadiya.com`) |
| **Unified Middleware** | [**`dhadiya-flow`**](file:///Users/apple/zora/dhadiya/dhadiya-flow) | Spring Boot 3.2, JPA, H2 / PostgreSQL | **8082** | **Unified Middleware Engine:** Integrates **eFormsPi Core Workflow Engine**, Dynamic Multi-Section Forms, and Floor Inventory DataGrids |

---

## ⚡ What is dhadiya-flow (Showroom Unified Middleware)?

`dhadiya-flow` unifies two core backend operational capabilities into a single, cohesive middleware service:

1. **eFormsPi Core Workflow Engine & Dynamic Forms:**
   * Multi-stage showroom process definitions (`WorkflowMaster`, `WorkflowStage`, `WorkflowTrigger`)
   * Live showroom case tracking (`WorkflowTrackMaster`) with tracking numbers (`DHADIYA-2026-001`, etc.)
   * Chronological lifecycle execution history and audit trails (`WorkflowTrackDet`)
   * Technician and sales executive task queues with SLA timers (`WorkflowTaskItem`)
   * Dynamic forms with sections, field components, and conditional business rules (`FormMaster`, `FormSection`, `FormComponent`, `FormRule`)

2. **High-Speed Showroom Floor Inventory DataGrids:**
   * Dynamic schema catalogs and table definitions (`DynamicGridDef`)
   * Full paginated querying, multi-field search, column filtering, and sorting (`DynamicGridRecord`)
   * **Automated Workflow Synchronization:** Vehicle PDI cases automatically synchronize their lifecycle state into backing inventory tables (`dhadiya_showroom_inventory`)!
   * Custom user filter presets and saved views (`DynamicGridView`)
   * One-click CSV and spreadsheet export

---

## 🚀 Pre-Seeded Showroom Examples Included

All services include pre-seeded global showroom management examples ready on startup:

### 1. Showroom Regions & Realms (dhadiya-tenants)
* **`dhadiya-showroom-global`**: Dhadiya Global Showrooms Network (`showrooms.dhadiya.com`)
* **`dhadiya-showroom-emea`**: Dhadiya Luxury Showrooms EMEA (`emea.showrooms.dhadiya.com`) (London Mayfair, Paris Champs-Élysées, Milan Montenapoleone, Dubai Downtown)
* **`dhadiya-showroom-americas`**: Dhadiya Flagship Showrooms Americas (`americas.showrooms.dhadiya.com`) (New York 5th Ave, Beverly Hills Rodeo Dr, Miami Brickell)
* **`dhadiya-showroom-apac`**: Dhadiya Prestige Showrooms APAC (`apac.showrooms.dhadiya.com`) (Tokyo Ginza, Singapore Marina Bay, Shanghai Bund)

### 2. Unified Middleware (dhadiya-flow)
* **eFormsPi Workflows:**
  * `WF_SHOWROOM_DELIVERY_HANDOVER`: 5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.
  * `WF_SHOWROOM_AUDIT`: 4-stage quarterly showroom floor presentation, visual merchandising, and VIP amenity compliance.
* **Live Cases & Tasks:**
  * `DHADIYA-2026-001` (Porsche 911 GT3 RS Weissach Package at Beverly Hills Rodeo Drive Flagship, Active in `TECHNICAL_PDI`), assigned to `chief.tech@americas.dhadiya.com`
  * `DHADIYA-2026-002` (Showroom Standards Audit at London Mayfair Flagship, Active in `FINDINGS_REVIEW`), assigned to `gm.mayfair@emea.dhadiya.com`
  * `DHADIYA-2026-003` (Lexus LFA Nürburgring Edition at Tokyo Ginza Flagship, `COMPLETED` Handover)
* **Dynamic Showroom DataGrids:**
  * `dhadiya_showroom_inventory`: Global Showroom Floor & Vehicle Inventory (VIN, Model, Trim, Location, MSRP, Floor Status)
  * `dhadiya_showroom_audits`: Global Flagship Brand Standards & Audit Registry
  * `dhadiya_customer_orders`: VIP Bespoke Orders & Client Allocations

---

## ⚡ Quickstart

### Local Development (Zero Database Setup Required)
Each Spring Boot service defaults to an embedded in-memory H2 database with automatic schema creation and data seeding:

* **dhadiya-tenants (Port 8081):**
  ```bash
  cd /Users/apple/zora/dhadiya/dhadiya-tenants && ./mvnw clean spring-boot:run
  ```
* **dhadiya-flow (Port 8082):**
  ```bash
  cd /Users/apple/zora/dhadiya/dhadiya-flow && ./mvnw clean spring-boot:run
  ```
* **dhadiya-portal (Port 4200):**
  ```bash
  cd /Users/apple/zora/dhadiya/dhadiya-portal && npm install && npm start
  ```
Open **`http://localhost:4200/`** to access the Dhadiya Showroom Portal.

---

### Docker Deployment
Run PostgreSQL and the 3 services with one command:
```bash
cd /Users/apple/zora/dhadiya && docker compose up --build
```

---

## 📖 Swagger / OpenAPI Endpoints
* **dhadiya-tenants (Showroom Territory Isolation):** `http://localhost:8081/swagger-ui.html`
* **dhadiya-flow (Showroom Workflows, Forms & DataGrids):** `http://localhost:8082/swagger-ui.html`
