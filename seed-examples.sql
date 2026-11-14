-- =========================================================================
-- DHADIYA.COM GLOBAL SHOWROOM MANAGEMENT PLATFORM - SEED DATA
-- =========================================================================

-- 1. TENANT REALMS (dhadiya-tenants)
INSERT INTO dhadiya_tenants (realm_code, organization_name, custom_domain, status, settings_json, created_at, updated_at)
VALUES 
('dhadiya-showroom-global', 'Dhadiya Global Showrooms Network', 'showrooms.dhadiya.com', 'ACTIVE', '{"plan":"ENTERPRISE","theme":"luxury-dark","features":["workflows","datagrid","pdi","vip_portal"]}', NOW(), NOW()),
('dhadiya-showroom-emea', 'Dhadiya Luxury Showrooms EMEA (London, Paris, Milan, Dubai)', 'emea.showrooms.dhadiya.com', 'ACTIVE', '{"currency":"EUR/GBP/AED","flagships":["London Mayfair","Paris Champs-Élysées","Milan Montenapoleone","Dubai Downtown"]}', NOW(), NOW()),
('dhadiya-showroom-americas', 'Dhadiya Flagship Showrooms Americas (New York, Beverly Hills, Miami)', 'americas.showrooms.dhadiya.com', 'ACTIVE', '{"currency":"USD","flagships":["New York 5th Ave","Beverly Hills Rodeo Dr","Miami Brickell"]}', NOW(), NOW()),
('dhadiya-showroom-apac', 'Dhadiya Prestige Showrooms APAC (Tokyo, Singapore, Shanghai)', 'apac.showrooms.dhadiya.com', 'ACTIVE', '{"currency":"JPY/SGD","flagships":["Tokyo Ginza","Singapore Marina Bay","Shanghai Bund"]}', NOW(), NOW());

-- 2. WORKFLOW PROCESS DEFINITIONS (dhadiya-flow middleware)
INSERT INTO dhadiya_process_definitions (realm_code, process_key, name, description, version, stages_json, default_form_key, status, created_at, updated_at)
VALUES
('dhadiya-showroom-global', 'WF_SHOWROOM_DELIVERY_HANDOVER', 'Global Showroom Vehicle PDI & VIP Handover Workflow', '5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.', 2, '["INTAKE_INSPECTION","TECHNICAL_PDI","LUXURY_DETAILING","VIP_FINANCIAL_CLEARANCE","FINAL_HANDOVER_CEREMONY"]', 'FORM_SHOWROOM_PDI', 'ACTIVE', NOW(), NOW()),
('dhadiya-showroom-global', 'WF_SHOWROOM_AUDIT', 'Global Flagship Showroom Standards & Brand Compliance Audit', 'Quarterly inspection of showroom floor presentation, visual merchandising, VIP customer amenities, and staff standards.', 1, '["SCHEDULED","ON_SITE_EVALUATION","FINDINGS_REVIEW","CERTIFICATION_SIGN_OFF"]', 'FORM_SHOWROOM_STANDARDS', 'ACTIVE', NOW(), NOW()),
('dhadiya-showroom-global', 'WF_VIP_CUSTOM_ORDER', 'VIP Bespoke Vehicle Configuration & Allocation Approval', 'Custom vehicle order underwriting, factory build slot reservation, and showroom arrival allocation.', 1, '["ORDER_INTAKE","CREDIT_VERIFICATION","ALLOCATION_CONFIRMED","DELIVERED"]', 'FORM_VIP_ORDER', 'ACTIVE', NOW(), NOW());

-- 3. PROCESS INSTANCES & CASES (dhadiya-flow middleware)
INSERT INTO dhadiya_process_instances (tracking_number, realm_code, process_key, current_stage, status, initiated_by, payload_json, started_at, updated_at)
VALUES
('DHADIYA-2026-001', 'dhadiya-showroom-americas', 'WF_SHOWROOM_DELIVERY_HANDOVER', 'TECHNICAL_PDI', 'IN_PROGRESS', 'logistics.lead@americas.dhadiya.com', '{"vinCode":"WBA33AY08PFS99120","modelName":"Porsche 911 GT3 RS","showroomLocation":"Beverly Hills Rodeo Drive Flagship","status":"PDI_IN_PROGRESS","msrp":"$318,500"}', NOW(), NOW()),
('DHADIYA-2026-002', 'dhadiya-showroom-emea', 'WF_SHOWROOM_AUDIT', 'FINDINGS_REVIEW', 'IN_PROGRESS', 'auditor.lead@showrooms.dhadiya.com', '{"showroomCode":"LON-MAYFAIR-01","inspector":"Arthur Pendelton","complianceScore":98.5}', NOW(), NOW()),
('DHADIYA-2026-003', 'dhadiya-showroom-apac', 'WF_VIP_CUSTOM_ORDER', 'DELIVERED', 'COMPLETED', 'vip.sales@apac.dhadiya.com', '{"clientName":"Kenji Takahashi","modelSelected":"Lexus LFA Nürburgring Edition","bookingStatus":"DELIVERED"}', NOW(), NOW());

-- 4. PENDING ACTION TASKS (dhadiya-flow middleware)
INSERT INTO dhadiya_process_tasks (instance_id, tracking_number, stage_name, title, assignee, status, created_at)
VALUES
(1, 'DHADIYA-2026-001', 'TECHNICAL_PDI', 'Perform 120-Point PDI & Diagnostics for Porsche 911 GT3 RS (DHADIYA-2026-001)', 'chief.tech@americas.dhadiya.com', 'PENDING', NOW()),
(2, 'DHADIYA-2026-002', 'FINDINGS_REVIEW', 'Showroom Standards Audit Sign-Off (London Mayfair Flagship)', 'gm.mayfair@emea.dhadiya.com', 'PENDING', NOW());

-- 5. DYNAMIC SCHEMA TABLES & DATAGRIDS (dhadiya-flow middleware)
INSERT INTO dhadiya_schema_tables (realm_code, table_name, label, description, column_schema_json, created_at, updated_at)
VALUES
('dhadiya-showroom-global', 'dhadiya_showroom_inventory', 'Global Showroom Floor & Vehicle Inventory', 'Master inventory tracking VINs, models, trims, MSRP, and showroom floor allocation status.', '["vinCode","modelName","trimLevel","exteriorColor","showroomLocation","vehicleStatus","msrpPrice","allocationDate","assignedAdvisor"]', NOW(), NOW()),
('dhadiya-showroom-global', 'dhadiya_showroom_audits', 'Global Flagship Brand Standards & Audit Registry', 'Showroom evaluation scorecard covering visual merchandising, amenities, and compliance scoring.', '["auditRef","showroomCode","cityLocation","auditDate","leadAuditor","complianceScore","criticalFindings","certificationStatus"]', NOW(), NOW()),
('dhadiya-showroom-global', 'dhadiya_customer_orders', 'VIP Bespoke Orders & Client Allocations', 'Global customer purchase agreements, VIP tier status, and vehicle handover milestones.', '["orderNumber","clientName","vipTier","showroomLocation","modelSelected","orderValue","deliveryStatus","salesLead"]', NOW(), NOW());

-- 6. DYNAMIC GRID RECORDS (dhadiya-flow middleware)
INSERT INTO dhadiya_grid_records (realm_code, table_name, process_tracking_number, data_json, created_at, updated_at)
VALUES
('dhadiya-showroom-americas', 'dhadiya_showroom_inventory', 'DHADIYA-2026-001', '{"vinCode":"WBA33AY08PFS99120","modelName":"Porsche 911 GT3 RS","trimLevel":"Weissach Package","exteriorColor":"Guards Red","showroomLocation":"Beverly Hills Rodeo Drive Flagship","vehicleStatus":"PDI_IN_PROGRESS","msrpPrice":"$318,500","allocationDate":"2026-11-10","assignedAdvisor":"Marco Rossi"}', NOW(), NOW()),
('dhadiya-showroom-emea', 'dhadiya_showroom_inventory', 'DHADIYA-2026-002', '{"vinCode":"ZHWUA1ZF5PLA09812","modelName":"Ferrari SF90 Stradale","trimLevel":"Assetto Fiorano","exteriorColor":"Rosso Corsa","showroomLocation":"London Mayfair Gallery","vehicleStatus":"ON_FLOOR_DISPLAY","msrpPrice":"£425,000","allocationDate":"2026-11-08","assignedAdvisor":"Arthur Pendelton"}', NOW(), NOW()),
('dhadiya-showroom-apac', 'dhadiya_showroom_inventory', 'DHADIYA-2026-003', '{"vinCode":"JTHHP5C13D5001290","modelName":"Lexus LFA Nürburgring","trimLevel":"Carbon Edition","exteriorColor":"Whitest White","showroomLocation":"Tokyo Ginza Flagship","vehicleStatus":"RESERVED","msrpPrice":"¥98,000,000","allocationDate":"2026-11-05","assignedAdvisor":"Kenji Takahashi"}', NOW(), NOW()),
('dhadiya-showroom-emea', 'dhadiya_showroom_inventory', 'DHADIYA-2026-004', '{"vinCode":"SCBFN9849LC109482","modelName":"Rolls-Royce Spectre","trimLevel":"Bespoke Luxury EV","exteriorColor":"Black Diamond & Mandarin","showroomLocation":"Dubai Downtown Gallery","vehicleStatus":"ON_FLOOR_DISPLAY","msrpPrice":"AED 1,950,000","allocationDate":"2026-11-12","assignedAdvisor":"Tariq Al-Fahim"}', NOW(), NOW()),
('dhadiya-showroom-americas', 'dhadiya_showroom_audits', 'DHADIYA-2026-002', '{"auditRef":"AUD-SHW-8801","showroomCode":"USA-BH-01","cityLocation":"Beverly Hills, CA","auditDate":"2026-11-05","leadAuditor":"Elena Rostova","complianceScore":"99.4%","criticalFindings":0,"certificationStatus":"CERTIFIED"}', NOW(), NOW()),
('dhadiya-showroom-emea', 'dhadiya_showroom_audits', 'DHADIYA-2026-005', '{"auditRef":"AUD-SHW-8802","showroomCode":"GBR-LON-02","cityLocation":"London Mayfair","auditDate":"2026-11-08","leadAuditor":"Arthur Pendelton","complianceScore":"98.2%","criticalFindings":0,"certificationStatus":"CERTIFIED"}', NOW(), NOW()),
('dhadiya-showroom-global', 'dhadiya_customer_orders', NULL, '{"orderNumber":"ORD-VIP-901","clientName":"Julian Sterling","vipTier":"DIAMOND_ELITE","showroomLocation":"Beverly Hills Rodeo Drive","modelSelected":"Porsche 911 GT3 RS","orderValue":"$318,500","deliveryStatus":"READY_FOR_DELIVERY","salesLead":"Marco Rossi"}', NOW(), NOW()),
('dhadiya-showroom-global', 'dhadiya_customer_orders', NULL, '{"orderNumber":"ORD-VIP-902","clientName":"Lady Charlotte Windsor","vipTier":"BLACK_CARD","showroomLocation":"London Mayfair","modelSelected":"Ferrari SF90 Stradale","orderValue":"£425,000","deliveryStatus":"ALLOCATED","salesLead":"Arthur Pendelton"}', NOW(), NOW());
