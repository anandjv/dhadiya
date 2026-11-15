# dhadiya-flow

**Dhadiya Unified Middleware Engine: eFormsPi Workflows, Dynamic Forms & DataGrids (dhadiya.com)**

`dhadiya-flow` is the core middleware engine for the Dhadiya Global Showroom Platform, uniting stateful eFormsPi workflow process orchestration with multi-section dynamic forms and high-speed showroom floor inventory data grid query execution.

## 🌟 Core Middleware Responsibilities

1. **eFormsPi Core Workflow Engine (WF):**
   - Multi-stage business process definitions (`WorkflowMaster`, `WorkflowStage`, `WorkflowTrigger`)
   - Case tracking numbers & lifecycle instances (`WorkflowTrackMaster`)
   - Complete chronological lifecycle audit trail (`WorkflowTrackDet`)
   - Role-based task queues and approval inboxes with SLA timers (`WorkflowTaskItem`)
   - Automated workflow execution and dynamic stage transitions

2. **eFormsPi Dynamic Forms Engine:**
   - Multi-section form master layouts (`FormMaster`, `FormSection`)
   - Typed field components (`FormComponent`: TEXT, TEXTAREA, NUMBER, SELECT, DATE, CHECKBOX)
   - Conditional business rules (`FormRule`: SHOW_HIDE, SET_REQUIRED based on trigger conditions)

3. **High-Speed Showroom Floor Inventory DataGrid Engine:**
   - Dynamic table schemas and column catalogs (`DynamicGridDef`)
   - Paginated record queries with multi-field search and column filters (`DynamicGridRecord`)
   - **Automated Workflow Synchronization:** Cases automatically update and synchronize their state into backing DataGrid inventory tables (`dhadiya_showroom_inventory`)!
   - User filter presets and custom grid views (`DynamicGridView`)
   - Direct CSV and spreadsheet exports

## 🚀 Pre-Seeded Showroom Examples Included

### Workflows & Cases
- **`WF_SHOWROOM_DELIVERY_HANDOVER`**: 5-stage lifecycle (`INTAKE_INSPECTION` &rarr; `TECHNICAL_PDI` &rarr; `LUXURY_DETAILING` &rarr; `VIP_FINANCIAL_CLEARANCE` &rarr; `FINAL_HANDOVER_CEREMONY`)
- **`WF_SHOWROOM_AUDIT`**: 4-stage quarterly brand audit (`SCHEDULED` &rarr; `ON_SITE_EVALUATION` &rarr; `FINDINGS_REVIEW` &rarr; `CERTIFICATION_SIGN_OFF`)
- **Live Cases**: `DHADIYA-2026-001` (Porsche 911 GT3 RS at Beverly Hills Flagship in `TECHNICAL_PDI`), `DHADIYA-2026-002` (`FINDINGS_REVIEW`), `DHADIYA-2026-003` (`COMPLETED`)

### DataGrids & Dynamic Tables
- **`dhadiya_showroom_inventory`**: Global Showroom Floor & Vehicle Inventory (VIN, Model, Trim, Location, MSRP, Status)
- **`dhadiya_showroom_audits`**: Global Flagship Brand Standards & Audit Registry
- **`dhadiya_customer_orders`**: VIP Bespoke Orders & Client Allocations

## 📖 Ports & Endpoints
- **Service Port:** `8082`
- **eFormsPi Workflow Engine API:** `/api/v1/engine/workflows`, `/api/v1/engine/cases`, `/api/v1/engine/tasks/pending`
- **eFormsPi Dynamic Forms API:** `/api/v1/dynamic-forms/masters`
- **Processes API:** `/api/v1/processes`
- **Tasks API:** `/api/v1/tasks`
- **Schemas / Table API:** `/api/v1/schemas` & `/api/v1/grid/definitions`
- **DataGrid Query API:** `/api/v1/grid/records/{gridKey}` & `/api/v1/grid/query`
- **Legacy Compatibility Endpoints:**
  - `/workflow/workflows`
  - `/workflowTrackMaster/workflowTrackMasters`
  - `/formMaster`
  - `/dynamicGrid/smart-grids`, `/dynamicGrid/grids/{gridKey}/data`
- **Swagger UI:** `http://localhost:8082/swagger-ui.html`
- **Health Check:** `http://localhost:8082/actuator/health`
- **H2 Console:** `http://localhost:8082/h2-console`

## ⚙️ Runtime & Technology Stack
- **Application:** `dhadiya-flow`
- **Spring Boot:** `3.5.x`
- **Java:** `25`
- **Spring Data JPA**
- **Databases:** H2 (dev) & PostgreSQL (prod)
