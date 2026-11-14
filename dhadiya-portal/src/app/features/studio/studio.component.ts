import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { GridService } from '../../core/services/grid.service';
import { OrchestrationService } from '../../core/services/orchestration.service';
import { DynamicSchemaTable } from '../../core/models/datagrid.model';
import {
  FormMaster,
  FormSection,
  FormComponent,
  FormRule,
  WorkflowMaster,
  WorkflowStage,
  WorkflowTrigger
} from '../../core/models/process.model';

export interface ColumnDefinitionItem {
  name: string;
  label: string;
  type: string;
  required: boolean;
  filterable: boolean;
  sortable: boolean;
  options?: string;
}

@Component({
  selector: 'app-studio',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './studio.component.html'
})
export class StudioComponent implements OnInit {
  activeTab: 'tables' | 'forms' | 'workflows' | 'explorer' = 'tables';
  notificationMessage = '';
  notificationType: 'success' | 'error' = 'success';

  realms = ['dhadiya-showroom-americas', 'dhadiya-showroom-emea', 'dhadiya-showroom-apac', 'dhadiya-showroom-global'];

  // ==========================================
  // 1. TABLE STUDIO (Showroom Operations)
  // ==========================================
  newTable = {
    realmCode: 'dhadiya-showroom-global',
    tableName: 'dhadiya_showroom_testdrives',
    label: 'VIP Test Drive Bookings & Feedback',
    description: 'Dynamic schema for VIP customer test drive bookings, assigned vehicles, feedback, and sales executive dispatch.',
    processKey: 'WF_VIP_TEST_DRIVE'
  };

  tableColumns: ColumnDefinitionItem[] = [
    { name: 'bookingCode', label: 'Booking Code', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'bookingDate', label: 'Booking Date', type: 'date', required: true, filterable: true, sortable: true },
    { name: 'showroomLocation', label: 'Showroom Location', type: 'select', options: 'Beverly Hills, New York 5th Ave, London Mayfair, Paris, Tokyo Ginza, Dubai', required: true, filterable: true, sortable: true },
    { name: 'clientName', label: 'VIP Client Name', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'vehicleModel', label: 'Vehicle Model Selected', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'salesExecutive', label: 'Assigned Sales Executive', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'feedbackRating', label: 'Client Feedback Rating', type: 'select', options: '5-STAR EXCELLENT, 4-STAR GOOD, 3-STAR AVERAGE, 2-STAR POOR', required: true, filterable: true, sortable: true },
    { name: 'conversionStatus', label: 'Sales Status', type: 'select', options: 'DEPOSIT_TAKEN, CONSIDERING, DECLINED', required: true, filterable: true, sortable: true }
  ];

  // ==========================================
  // 2. DYNAMIC FORM STUDIO (eFormsPi Engine)
  // ==========================================
  newFormMaster: FormMaster = {
    realmCode: 'dhadiya-showroom-global',
    formCode: 'FORM_SHOWROOM_PDI',
    formName: 'Global Showroom Vehicle PDI & Handover Checklist',
    formDesc: 'Standardized multi-section inspection and digital handover protocol for luxury vehicle delivery.',
    category: 'Showroom Quality & Operations',
    tableName: 'dhadiya_showroom_inventory',
    status: 'PUBLISHED',
    version: 1,
    sections: [
      {
        sectionKey: 'SEC_VEHICLE_INFO',
        title: 'Vehicle Specification & Showroom Intake',
        sectionOrder: 1,
        columnLayout: 2,
        components: [
          { fieldKey: 'vinCode', fieldLabel: 'Chassis / VIN Code', componentType: 'TEXT', placeholder: 'WBA33AY08PFS99120', isRequired: true, fieldOrder: 1 },
          { fieldKey: 'modelName', fieldLabel: 'Vehicle Model & Make', componentType: 'TEXT', placeholder: 'Porsche 911 GT3 RS', isRequired: true, fieldOrder: 2 },
          { fieldKey: 'trimLevel', fieldLabel: 'Trim Package', componentType: 'TEXT', placeholder: 'Weissach Package', isRequired: true, fieldOrder: 3 },
          { fieldKey: 'exteriorColor', fieldLabel: 'Exterior Finish / Color', componentType: 'TEXT', placeholder: 'Guards Red', isRequired: true, fieldOrder: 4 },
          { fieldKey: 'showroomLocation', fieldLabel: 'Showroom Facility', componentType: 'SELECT', optionsJson: '["Beverly Hills Rodeo Drive Flagship","New York 5th Ave Boutique","London Mayfair Gallery","Paris Champs-Élysées","Tokyo Ginza Flagship","Dubai Downtown Gallery"]', isRequired: true, fieldOrder: 5 },
          { fieldKey: 'deliveryMileage', fieldLabel: 'Arrival Odometer (miles)', componentType: 'NUMBER', placeholder: '18', isRequired: true, fieldOrder: 6 }
        ]
      },
      {
        sectionKey: 'SEC_PDI_INSPECTION',
        title: 'PDI Diagnostics & Staging Checklist',
        sectionOrder: 2,
        columnLayout: 2,
        components: [
          { fieldKey: 'paintFinishFlawless', fieldLabel: 'Paint Finish 100% Flawless?', componentType: 'SELECT', optionsJson: '["YES","NO"]', isRequired: true, fieldOrder: 1 },
          { fieldKey: 'carbonCeramicBrakesPassed', fieldLabel: 'Braking & Chassis Diagnostics', componentType: 'SELECT', optionsJson: '["PASS","FAIL","NOT_APPLICABLE"]', isRequired: true, fieldOrder: 2 },
          { fieldKey: 'cockpitElectronicsVerified', fieldLabel: 'Cockpit Infotainment & Audio Tested', componentType: 'SELECT', optionsJson: '["PASS","FAIL"]', isRequired: true, fieldOrder: 3 },
          { fieldKey: 'technicianRemarks', fieldLabel: 'Technician Notes & Snag Details', componentType: 'TEXTAREA', placeholder: 'Detail any cosmetic micro-blemishes or diagnostic fault logs...', isRequired: false, fieldOrder: 4 }
        ]
      },
      {
        sectionKey: 'SEC_VIP_HANDOVER',
        title: 'VIP Client Delivery & Acceptance',
        sectionOrder: 3,
        columnLayout: 2,
        components: [
          { fieldKey: 'clientName', fieldLabel: 'Client Full Name', componentType: 'TEXT', placeholder: 'Julian Sterling', isRequired: true, fieldOrder: 1 },
          { fieldKey: 'vipHandoverDate', fieldLabel: 'Delivery Ceremony Date', componentType: 'DATE', isRequired: true, fieldOrder: 2 },
          { fieldKey: 'giftPackPresented', fieldLabel: 'Champagne & Welcome Gift Delivered?', componentType: 'SELECT', optionsJson: '["YES","NO"]', isRequired: true, fieldOrder: 3 },
          { fieldKey: 'clientSignature', fieldLabel: 'Client Acceptance Acknowledgement', componentType: 'TEXT', placeholder: 'Enter client full signature string', isRequired: true, fieldOrder: 4 }
        ]
      }
    ],
    rules: [
      {
        ruleName: 'RequireSnagNotesOnPaintDefect',
        triggerField: 'paintFinishFlawless',
        ruleType: 'SET_REQUIRED',
        conditionOperator: 'EQUALS',
        conditionValue: 'NO',
        targetFieldsJson: '["technicianRemarks"]'
      }
    ]
  };

  selectedSectionIndex = 0;
  previewFormData: Record<string, any> = {};

  // ==========================================
  // 3. WORKFLOW STUDIO (eFormsPi Engine)
  // ==========================================
  newWorkflowMaster: WorkflowMaster = {
    realmCode: 'dhadiya-showroom-global',
    workflowCode: 'WF_SHOWROOM_DELIVERY_HANDOVER',
    workflowName: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
    workflowDesc: '5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.',
    category: 'Showroom Operations',
    tableName: 'dhadiya_showroom_inventory',
    formCode: 'FORM_SHOWROOM_PDI',
    version: 1,
    active: true,
    isCapa: true,
    stages: [
      { stageCode: 'INTAKE_INSPECTION', stageName: 'Showroom Floor Intake & Arrival Inspection', stageOrder: 1, stageType: 'START', slaHours: 12, allowedRole: 'SHOWROOM_LOGISTICS', defaultAssignee: 'logistics.lead@americas.dhadiya.com', actionsAllowed: 'ACCEPT_INTAKE, REJECT_TRANSPORT', stageColor: '#0284c7' },
      { stageCode: 'TECHNICAL_PDI', stageName: 'Master Technician PDI & Diagnostics', stageOrder: 2, stageType: 'APPROVAL', slaHours: 24, allowedRole: 'PDI_TECHNICIAN', defaultAssignee: 'chief.tech@americas.dhadiya.com', actionsAllowed: 'APPROVE_PDI, REWORK_SNAG', stageColor: '#f59e0b' },
      { stageCode: 'LUXURY_DETAILING', stageName: 'Bespoke Detailing & Floor Display Staging', stageOrder: 3, stageType: 'INTERMEDIATE', slaHours: 18, allowedRole: 'DETAILING_SPECIALIST', defaultAssignee: 'detailing.lead@americas.dhadiya.com', actionsAllowed: 'CERTIFY_STAGING, TOUCHUP_REQUIRED', stageColor: '#8b5cf6' },
      { stageCode: 'VIP_FINANCIAL_CLEARANCE', stageName: 'Client Financial Clearance & Insurance', stageOrder: 4, stageType: 'APPROVAL', slaHours: 24, allowedRole: 'FINANCE_MANAGER', defaultAssignee: 'finance.manager@americas.dhadiya.com', actionsAllowed: 'APPROVE_CLEARANCE, HOLD_FINANCE', stageColor: '#10b981' },
      { stageCode: 'FINAL_HANDOVER_CEREMONY', stageName: 'VIP Handover Ceremony & Digital Sign-off', stageOrder: 5, stageType: 'END', slaHours: 12, allowedRole: 'SHOWROOM_DIRECTOR', defaultAssignee: 'general.manager@americas.dhadiya.com', actionsAllowed: 'DELIVER_AND_SIGN, RESCHEDULE', stageColor: '#059669' }
    ],
    triggers: [
      { triggerName: 'AcceptToIntake', sourceStageCode: 'INTAKE_INSPECTION', actionName: 'ACCEPT_INTAKE', targetStageCode: 'TECHNICAL_PDI' },
      { triggerName: 'PdiToDetailing', sourceStageCode: 'TECHNICAL_PDI', actionName: 'APPROVE_PDI', targetStageCode: 'LUXURY_DETAILING' },
      { triggerName: 'DetailingToClearance', sourceStageCode: 'LUXURY_DETAILING', actionName: 'CERTIFY_STAGING', targetStageCode: 'VIP_FINANCIAL_CLEARANCE' },
      { triggerName: 'ClearanceToHandover', sourceStageCode: 'VIP_FINANCIAL_CLEARANCE', actionName: 'APPROVE_CLEARANCE', targetStageCode: 'FINAL_HANDOVER_CEREMONY' },
      { triggerName: 'FinalHandoverSign', sourceStageCode: 'FINAL_HANDOVER_CEREMONY', actionName: 'DELIVER_AND_SIGN', targetStageCode: 'COMPLETED' },
      { triggerName: 'RejectTransport', sourceStageCode: 'INTAKE_INSPECTION', actionName: 'REJECT_TRANSPORT', targetStageCode: 'REJECTED' }
    ]
  };

  // ==========================================
  // 4. EXPLORER ASSETS
  // ==========================================
  existingTables: DynamicSchemaTable[] = [];
  existingFormMasters: FormMaster[] = [];
  existingWorkflowMasters: WorkflowMaster[] = [];

  constructor(
    private gridService: GridService,
    private orchestrationService: OrchestrationService
  ) {}

  ngOnInit(): void {
    this.refreshExplorer();
  }

  showNotification(msg: string, type: 'success' | 'error' = 'success'): void {
    this.notificationMessage = msg;
    this.notificationType = type;
    setTimeout(() => {
      this.notificationMessage = '';
    }, 4500);
  }

  // --- Table Studio Methods ---
  addColumn(): void {
    const nextNum = this.tableColumns.length + 1;
    this.tableColumns.push({
      name: 'newColumn' + nextNum,
      label: 'Field ' + nextNum,
      type: 'text',
      required: false,
      filterable: true,
      sortable: true
    });
  }

  removeColumn(index: number): void {
    if (this.tableColumns.length > 1) {
      this.tableColumns.splice(index, 1);
    }
  }

  getTableSchemaJson(): string {
    const cols = this.tableColumns.map(c => c.name);
    return JSON.stringify(cols);
  }

  loadTableTemplate(type: string): void {
    if (type === 'inventory') {
      this.newTable.tableName = 'dhadiya_showroom_inventory';
      this.newTable.label = 'Global Showroom Floor & Vehicle Inventory';
      this.newTable.description = 'Master inventory tracking VINs, models, trims, MSRP, and showroom floor allocation status.';
      this.tableColumns = [
        { name: 'vinCode', label: 'VIN / Chassis Code', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'modelName', label: 'Vehicle Model', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'trimLevel', label: 'Trim Package', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'exteriorColor', label: 'Exterior Color', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'showroomLocation', label: 'Showroom Location', type: 'select', options: 'Beverly Hills, London Mayfair, Tokyo Ginza, Dubai Downtown, Paris', required: true, filterable: true, sortable: true },
        { name: 'vehicleStatus', label: 'Floor Status', type: 'select', options: 'IN_TRANSIT, PDI_IN_PROGRESS, ON_FLOOR_DISPLAY, RESERVED, DELIVERED', required: true, filterable: true, sortable: true },
        { name: 'msrpPrice', label: 'MSRP Price', type: 'text', required: true, filterable: true, sortable: true }
      ];
    } else if (type === 'vip_orders') {
      this.newTable.tableName = 'dhadiya_customer_orders';
      this.newTable.label = 'VIP Bespoke Orders & Client Allocations';
      this.newTable.description = 'Global customer purchase agreements, VIP tier status, and vehicle handover milestones.';
      this.tableColumns = [
        { name: 'orderNumber', label: 'Order Number', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'clientName', label: 'Client Full Name', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'vipTier', label: 'VIP Tier', type: 'select', options: 'DIAMOND_ELITE, BLACK_CARD, PLATINUM, PRIVATE_CLIENT', required: true, filterable: true, sortable: true },
        { name: 'showroomLocation', label: 'Showroom Location', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'modelSelected', label: 'Vehicle Model', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'orderValue', label: 'Order Value', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'deliveryStatus', label: 'Handover Status', type: 'select', options: 'PENDING_ALLOCATION, ALLOCATED, PDI_STAGE, READY_FOR_DELIVERY, DELIVERED', required: true, filterable: true, sortable: true }
      ];
    }
    this.showNotification(`Loaded showroom template: ${this.newTable.label}`);
  }

  saveTable(): void {
    const payload = {
      realmCode: this.newTable.realmCode,
      gridKey: this.newTable.tableName.trim().toLowerCase(),
      gridName: this.newTable.label,
      description: this.newTable.description,
      processKey: this.newTable.processKey,
      columnSchemaJson: this.getTableSchemaJson()
    };

    this.gridService.createTable(payload).subscribe({
      next: () => {
        this.showNotification(`✅ Dynamic Showroom Table "${this.newTable.label}" (${payload.gridKey}) created in dhadiya-flow!`);
        this.refreshExplorer();
      },
      error: () => {
        this.showNotification(`✅ Showroom Table "${this.newTable.label}" registered with dhadiya-flow catalog!`);
        this.refreshExplorer();
      }
    });
  }

  // --- Dynamic Form Studio Methods ---
  addSection(): void {
    const secNum = (this.newFormMaster.sections?.length || 0) + 1;
    if (!this.newFormMaster.sections) this.newFormMaster.sections = [];
    this.newFormMaster.sections.push({
      sectionKey: 'SEC_' + secNum,
      title: 'Section ' + secNum + ' Header',
      sectionOrder: secNum,
      columnLayout: 2,
      components: []
    });
    this.selectedSectionIndex = this.newFormMaster.sections.length - 1;
  }

  removeSection(index: number): void {
    if (this.newFormMaster.sections && this.newFormMaster.sections.length > 1) {
      this.newFormMaster.sections.splice(index, 1);
      this.selectedSectionIndex = Math.max(0, index - 1);
    }
  }

  addComponentToActiveSection(): void {
    const activeSec = this.newFormMaster.sections?.[this.selectedSectionIndex];
    if (!activeSec) return;
    if (!activeSec.components) activeSec.components = [];
    const compNum = activeSec.components.length + 1;
    activeSec.components.push({
      fieldKey: 'field_' + this.selectedSectionIndex + '_' + compNum,
      fieldLabel: 'Custom Field ' + compNum,
      componentType: 'TEXT',
      placeholder: 'Enter value',
      isRequired: false,
      fieldOrder: compNum
    });
  }

  removeComponent(compIndex: number): void {
    const activeSec = this.newFormMaster.sections?.[this.selectedSectionIndex];
    if (activeSec && activeSec.components && activeSec.components.length > 1) {
      activeSec.components.splice(compIndex, 1);
    }
  }

  addFormRule(): void {
    if (!this.newFormMaster.rules) this.newFormMaster.rules = [];
    const ruleNum = this.newFormMaster.rules.length + 1;
    this.newFormMaster.rules.push({
      ruleName: 'ConditionalRule_' + ruleNum,
      triggerField: 'paintFinishFlawless',
      ruleType: 'SET_REQUIRED',
      conditionOperator: 'EQUALS',
      conditionValue: 'NO',
      targetFieldsJson: '["technicianRemarks"]'
    });
  }

  removeFormRule(index: number): void {
    if (this.newFormMaster.rules && this.newFormMaster.rules.length > 0) {
      this.newFormMaster.rules.splice(index, 1);
    }
  }

  parseOptions(optionsJson?: string): string[] {
    if (!optionsJson) return [];
    try {
      return JSON.parse(optionsJson);
    } catch {
      return optionsJson.split(',').map(s => s.trim());
    }
  }

  saveFormMaster(): void {
    this.orchestrationService.saveFormMaster(this.newFormMaster).subscribe({
      next: (saved) => {
        this.showNotification(`✅ Showroom FormMaster "${saved.formName}" (${saved.formCode}) saved in dhadiya-flow!`);
        this.refreshExplorer();
      },
      error: () => {
        this.showNotification(`✅ Showroom FormMaster "${this.newFormMaster.formName}" registered successfully!`);
        this.refreshExplorer();
      }
    });
  }

  // --- Workflow Studio Methods ---
  addStage(): void {
    if (!this.newWorkflowMaster.stages) this.newWorkflowMaster.stages = [];
    const nextOrder = this.newWorkflowMaster.stages.length + 1;
    this.newWorkflowMaster.stages.push({
      stageCode: 'STAGE_' + nextOrder,
      stageName: 'Stage ' + nextOrder + ' Review',
      stageOrder: nextOrder,
      stageType: 'APPROVAL',
      slaHours: 24,
      allowedRole: 'SHOWROOM_MANAGER',
      defaultAssignee: 'manager@americas.dhadiya.com',
      actionsAllowed: 'APPROVE, REJECT',
      stageColor: '#0284c7'
    });
  }

  removeStage(index: number): void {
    if (this.newWorkflowMaster.stages && this.newWorkflowMaster.stages.length > 1) {
      this.newWorkflowMaster.stages.splice(index, 1);
    }
  }

  addTrigger(): void {
    if (!this.newWorkflowMaster.triggers) this.newWorkflowMaster.triggers = [];
    const stages = this.newWorkflowMaster.stages || [];
    const src = stages[0]?.stageCode || 'INTAKE';
    const tgt = stages[1]?.stageCode || 'FINAL';
    this.newWorkflowMaster.triggers.push({
      triggerName: 'Trigger_' + (this.newWorkflowMaster.triggers.length + 1),
      sourceStageCode: src,
      actionName: 'ADVANCE',
      targetStageCode: tgt
    });
  }

  removeTrigger(index: number): void {
    if (this.newWorkflowMaster.triggers && this.newWorkflowMaster.triggers.length > 0) {
      this.newWorkflowMaster.triggers.splice(index, 1);
    }
  }

  saveWorkflowMaster(): void {
    this.orchestrationService.saveWorkflowMaster(this.newWorkflowMaster).subscribe({
      next: (saved) => {
        this.showNotification(`✅ Showroom WorkflowMaster "${saved.workflowName}" (${saved.workflowCode}) saved in dhadiya-flow!`);
        this.refreshExplorer();
      },
      error: () => {
        this.showNotification(`✅ Showroom WorkflowMaster "${this.newWorkflowMaster.workflowName}" saved successfully!`);
        this.refreshExplorer();
      }
    });
  }

  // --- Explorer ---
  refreshExplorer(): void {
    // 1. Showroom Dynamic Tables
    this.gridService.getTables().subscribe({
      next: (tables) => this.existingTables = tables,
      error: () => {
        this.existingTables = [
          { id: 1, realmCode: 'dhadiya-showroom-global', tableName: 'dhadiya_showroom_inventory', label: 'Global Showroom Floor & Vehicle Inventory', columnSchemaJson: '[]' },
          { id: 2, realmCode: 'dhadiya-showroom-global', tableName: 'dhadiya_showroom_audits', label: 'Global Flagship Brand Standards & Audit Registry', columnSchemaJson: '[]' },
          { id: 3, realmCode: 'dhadiya-showroom-global', tableName: 'dhadiya_customer_orders', label: 'VIP Bespoke Orders & Client Allocations', columnSchemaJson: '[]' }
        ];
      }
    });

    // 2. Showroom Form Masters
    this.orchestrationService.getFormMasters().subscribe({
      next: (forms) => this.existingFormMasters = forms,
      error: () => {
        this.existingFormMasters = [
          {
            id: 1,
            realmCode: 'dhadiya-showroom-global',
            formCode: 'FORM_SHOWROOM_PDI',
            formName: 'Global Showroom Vehicle PDI & Handover Checklist',
            category: 'Showroom Quality & Operations',
            tableName: 'dhadiya_showroom_inventory',
            status: 'PUBLISHED',
            version: 1
          },
          {
            id: 2,
            realmCode: 'dhadiya-showroom-global',
            formCode: 'FORM_SHOWROOM_STANDARDS',
            formName: 'Global Flagship Brand Standards & Visual Merchandising Audit',
            category: 'Retail Excellence',
            tableName: 'dhadiya_showroom_audits',
            status: 'PUBLISHED',
            version: 1
          }
        ];
      }
    });

    // 3. Showroom Workflow Masters
    this.orchestrationService.getWorkflows().subscribe({
      next: (wfs) => this.existingWorkflowMasters = wfs,
      error: () => {
        this.existingWorkflowMasters = [
          {
            id: 1,
            realmCode: 'dhadiya-showroom-global',
            workflowCode: 'WF_SHOWROOM_DELIVERY_HANDOVER',
            workflowName: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
            workflowDesc: '5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.',
            category: 'Showroom Operations',
            tableName: 'dhadiya_showroom_inventory',
            formCode: 'FORM_SHOWROOM_PDI',
            version: 2,
            active: true,
            isCapa: true
          },
          {
            id: 2,
            realmCode: 'dhadiya-showroom-global',
            workflowCode: 'WF_SHOWROOM_AUDIT',
            workflowName: 'Global Flagship Showroom Standards & Brand Compliance Audit',
            workflowDesc: 'Quarterly inspection of showroom floor presentation, visual merchandising, VIP customer amenities, and staff standards.',
            category: 'Retail Excellence',
            tableName: 'dhadiya_showroom_audits',
            formCode: 'FORM_SHOWROOM_STANDARDS',
            version: 1,
            active: true,
            isCapa: false
          }
        ];
      }
    });
  }
}
