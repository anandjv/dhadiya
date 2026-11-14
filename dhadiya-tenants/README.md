# dhadiya-tenants

**Dhadiya Multi-Tenant Realm & Organization Management Service (dhadiya.com)**

`dhadiya-tenants` provides centralized multi-tenant lifecycle provisioning, realm domain resolution, and enterprise tenant configurations for the Dhadiya Platform.

## Pre-Seeded Example Realms
On boot, `dhadiya-tenants` automatically initializes with real enterprise tenant instances:
1. `dhadiya-showroom-global` - **Dhadiya Global Showrooms Network** (`showrooms.dhadiya.com`, ACTIVE)
2. `dhadiya-showroom-emea` - **Dhadiya Luxury Showrooms EMEA** (`emea.showrooms.dhadiya.com`, ACTIVE)
3. `dhadiya-showroom-americas` - **Dhadiya Flagship Showrooms Americas** (`americas.showrooms.dhadiya.com`, ACTIVE)
4. `dhadiya-showroom-apac` - **Dhadiya Prestige Showrooms APAC** (`apac.showrooms.dhadiya.com`, ACTIVE)

## Features
- Tenant realm provisioning, activation, suspension, and domain mapping
- Dynamic JSON settings storage for tenant customization
- Spring Boot 3.2.x, Spring Data JPA, H2 (dev) & PostgreSQL (prod)
- Interactive Swagger / OpenAPI documentation
- Actuator health metrics

## Ports & Endpoints
- **Service Port:** `8081`
- **Base API:** `/api/v1/realms`
- **Swagger UI:** `http://localhost:8081/swagger-ui.html`
- **Health Check:** `http://localhost:8081/actuator/health`
- **H2 Console:** `http://localhost:8081/h2-console`
