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
  activeTab: 'process' | 'tables' | 'forms' | 'workflows' | 'testrunner' | 'explorer' = 'process';
  notificationMessage = '';
  notificationType: 'success' | 'error' = 'success';
  isDeployingProcess = false;

  realms = [
    'dhadiya-showroom-global',
    'dhadiya-showroom-americas',
    'dhadiya-showroom-emea',
    'dhadiya-showroom-apac',
    'tvs-motors'
  ];

  // ==========================================
  // MASTER PROCESS DEFINITION
  // ==========================================
  currentProcess: {
    realmCode: string;
    processKey: string;
    name: string;
    category: string;
    industryType: string;
    description: string;
    tableName?: string;
  } = {
    realmCode: 'dhadiya-showroom-global',
    processKey: 'WF_SHOWROOM_DELIVERY_HANDOVER',
    name: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
    category: 'Showroom Operations',
    industryType: 'AUTOMOBILE',
    description: '5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.',
    tableName: 'dhadiya_showroom_inventory'
  };

  testCaseInitiator = 'admin@showroom.dhadiya.com';
  testCasePriority = 'HIGH';
  testRunResult: any = null;

  // ==========================================
  // 1. DYNAMIC TABLE (Inside Process)
  // ==========================================
  newTable = {
    realmCode: 'dhadiya-showroom-global',
    tableName: 'dhadiya_showroom_inventory',
    label: 'Global Showroom Floor & Vehicle Inventory',
    description: 'Dynamic schema for vehicle inventory with VIN, trim, location, MSRP, and floor status.',
    processKey: 'WF_SHOWROOM_DELIVERY_HANDOVER'
  };

  tableColumns: ColumnDefinitionItem[] = [
    { name: 'vinCode', label: 'Chassis / VIN Code', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'modelName', label: 'Vehicle Model & Make', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'trimLevel', label: 'Trim Package', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'exteriorColor', label: 'Exterior Color', type: 'text', required: true, filterable: true, sortable: true },
    { name: 'showroomLocation', label: 'Showroom Location', type: 'select', options: 'Beverly Hills, New York, London, Paris, Tokyo, Dubai', required: true, filterable: true, sortable: true },
    { name: 'msrpPrice', label: 'MSRP Price', type: 'currency', required: true, filterable: true, sortable: true },
    { name: 'vehicleStatus', label: 'Vehicle Status', type: 'select', options: 'PDI_IN_PROGRESS, ON_FLOOR_DISPLAY, RESERVED, DELIVERED', required: true, filterable: true, sortable: true }
  ];

  // ==========================================
  // 2. DYNAMIC FORM (Inside Process)
  // ==========================================
  newFormMaster: FormMaster = {
    realmCode: 'dhadiya-showroom-global',
    formCode: 'FORM_SHOWROOM_PDI',
    formName: 'Global Showroom Vehicle PDI & Handover Checklist',
    formDesc: 'Standardized multi-section inspection and digital handover protocol for luxury vehicle delivery.',
    category: 'Showroom Operations',
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
          { fieldKey: 'showroomLocation', fieldLabel: 'Showroom Facility', componentType: 'SELECT', optionsJson: '["Beverly Hills Rodeo Drive Flagship","New York 5th Ave Boutique","London Mayfair Gallery","Paris Champs-Élysées","Tokyo Ginza Flagship","Dubai Downtown Gallery"]', isRequired: true, fieldOrder: 5 }
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
          { fieldKey: 'technicianRemarks', fieldLabel: 'Technician Notes & Snag Details', componentType: 'TEXTAREA', placeholder: 'Detail any cosmetic micro-blemishes or diagnostic fault logs...', isRequired: false, fieldOrder: 3 }
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
  // 3. DYNAMIC WORKFLOW (Inside Process)
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

  // Assets list
  existingGridTables: DynamicSchemaTable[] = [];
  existingFormMasters: FormMaster[] = [];
  existingWorkflowMasters: WorkflowMaster[] = [];
  existingProcesses: any[] = [];

  constructor(
    private gridService: GridService,
    private orchestrationService: OrchestrationService
  ) {}

  ngOnInit(): void {
    this.loadAssets();
  }

  loadAssets(): void {
    // 1. Process definitions
    this.orchestrationService.getDefinitions(this.currentProcess.realmCode).subscribe({
      next: (defs) => this.existingProcesses = defs || [],
      error: () => this.existingProcesses = []
    });

    // 2. Dynamic Tables
    this.gridService.getTables(this.currentProcess.realmCode).subscribe({
      next: (tables) => this.existingGridTables = tables || [],
      error: () => this.existingGridTables = []
    });

    // 3. Form Masters
    this.orchestrationService.getFormMasters(this.currentProcess.realmCode).subscribe({
      next: (forms) => this.existingFormMasters = forms || [],
      error: () => this.existingFormMasters = []
    });

    // 4. Workflows
    this.orchestrationService.getWorkflows(this.currentProcess.realmCode).subscribe({
      next: (wfs) => this.existingWorkflowMasters = wfs || [],
      error: () => this.existingWorkflowMasters = []
    });
  }

  onRealmChange(realm: string): void {
    this.currentProcess.realmCode = realm;
    this.newTable.realmCode = realm;
    this.newFormMaster.realmCode = realm;
    this.newWorkflowMaster.realmCode = realm;
    this.loadAssets();
    this.notify(`Switched studio tenant isolation context to '${realm}'. All components now scoped to this realm.`, 'success');
  }

  // Save the entire process bundle atomically!
  saveEntireProcessBundle(): void {
    this.isDeployingProcess = true;
    const bundle = {
      realmCode: this.currentProcess.realmCode,
      processKey: this.currentProcess.processKey,
      name: this.currentProcess.name,
      category: this.currentProcess.category,
      industryType: this.currentProcess.industryType,
      description: this.currentProcess.description,
      tableName: this.newTable.tableName,
      tableLabel: this.newTable.label,
      tableDescription: this.newTable.description,
      tableColumns: this.tableColumns.map(c => c.name),
      tableSchemaJson: JSON.stringify(this.tableColumns),
      form: {
        ...this.newFormMaster,
        realmCode: this.currentProcess.realmCode,
        tableName: this.newTable.tableName
      },
      workflow: {
        ...this.newWorkflowMaster,
        realmCode: this.currentProcess.realmCode,
        workflowCode: this.currentProcess.processKey,
        tableName: this.newTable.tableName,
        formCode: this.newFormMaster.formCode
      }
    };

    this.orchestrationService.saveProcessBundle(bundle).subscribe({
      next: () => {
        this.isDeployingProcess = false;
        this.notify(`Process '${this.currentProcess.name}' and its Dynamic Table, Form & Workflow deployed successfully under tenant '${this.currentProcess.realmCode}'!`, 'success');
        this.loadAssets();
      },
      error: (err) => {
        this.isDeployingProcess = false;
        this.notify('Failed to deploy process bundle: ' + (err.message || 'Server error'), 'error');
      }
    });
  }

  // Industry Template Presets
  loadIndustryProcess(type: string): void {
    if (type === 'JEWELLERY') {
      this.currentProcess = {
        realmCode: this.currentProcess.realmCode,
        processKey: 'USMS_JEWELLERY_SALES',
        name: 'Fine Jewellery Hallmark & Custom Commissioning Process',
        category: 'Jewellery Showroom',
        industryType: 'JEWELLERY',
        description: 'Multi-stage jewellery intake, gold rate valuation, purity testing, wastage & making charges calculation, and BIS hallmark sign-off.',
        tableName: 'usms_jewellery_inventory'
      };
      this.newTable = {
        realmCode: this.currentProcess.realmCode,
        tableName: 'usms_jewellery_inventory',
        label: 'Precious Gold & Diamond Showroom Vault',
        description: 'Dynamic table for jewellery pieces with gross weight, net weight, purity karat, stone value, making charges, and hallmark cert.',
        processKey: 'USMS_JEWELLERY_SALES'
      };
      this.tableColumns = [
        { name: 'itemCode', label: 'Item / Tag Code', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'metalType', label: 'Precious Metal', type: 'select', options: '24K Gold, 22K Gold, 18K Rose Gold, 950 Platinum, 925 Sterling Silver', required: true, filterable: true, sortable: true },
        { name: 'grossWeightGrams', label: 'Gross Weight (g)', type: 'number', required: true, filterable: true, sortable: true },
        { name: 'netWeightGrams', label: 'Net Weight (g)', type: 'number', required: true, filterable: true, sortable: true },
        { name: 'stoneWeightCarats', label: 'Stone Weight (Carats)', type: 'number', required: false, filterable: true, sortable: true },
        { name: 'wastagePercentage', label: 'Wastage % (VA)', type: 'number', required: true, filterable: true, sortable: true },
        { name: 'makingChargesPerGram', label: 'Making Charges / gram (₹)', type: 'number', required: true, filterable: true, sortable: true },
        { name: 'hallmarkLicenseNo', label: 'BIS Hallmark Certificate No', type: 'text', required: true, filterable: true, sortable: true }
      ];
      this.newFormMaster = {
        realmCode: this.currentProcess.realmCode,
        formCode: 'FORM_JEWELLERY_VALUATION',
        formName: 'Jewellery Purity & Hallmark Certification Protocol',
        formDesc: 'Dynamic protocol for precision weight verification, purity testing, and gold rate valuation.',
        category: 'Jewellery Showroom',
        tableName: 'usms_jewellery_inventory',
        status: 'PUBLISHED',
        version: 1,
        sections: [
          {
            sectionKey: 'SEC_METAL_DETAILS',
            title: 'Gold / Precious Metal Specification',
            sectionOrder: 1,
            columnLayout: 2,
            components: [
              { fieldKey: 'itemCode', fieldLabel: 'Tag / Barcode Code', componentType: 'TEXT', placeholder: 'JWL-GOLD-2026-001', isRequired: true, fieldOrder: 1 },
              { fieldKey: 'metalType', fieldLabel: 'Precious Metal & Karat', componentType: 'SELECT', optionsJson: '["24K Pure Gold","22K Traditional Gold","18K Diamond Jewellery","950 Platinum"]', isRequired: true, fieldOrder: 2 },
              { fieldKey: 'grossWeightGrams', fieldLabel: 'Gross Weight (grams)', componentType: 'NUMBER', placeholder: '48.55', isRequired: true, fieldOrder: 3 },
              { fieldKey: 'netWeightGrams', fieldLabel: 'Net Gold Weight (grams)', componentType: 'NUMBER', placeholder: '42.10', isRequired: true, fieldOrder: 4 }
            ]
          },
          {
            sectionKey: 'SEC_CHARGES',
            title: 'Wastage, Making Charges & Hallmark Sign-off',
            sectionOrder: 2,
            columnLayout: 2,
            components: [
              { fieldKey: 'wastagePercentage', fieldLabel: 'Value Addition / Wastage %', componentType: 'NUMBER', placeholder: '12.5', isRequired: true, fieldOrder: 1 },
              { fieldKey: 'makingChargesPerGram', fieldLabel: 'Making Charges per gram (₹)', componentType: 'NUMBER', placeholder: '450', isRequired: true, fieldOrder: 2 },
              { fieldKey: 'hallmarkLicenseNo', fieldLabel: 'BIS Hallmark Certificate Number', componentType: 'TEXT', placeholder: 'BIS-HM-9981204', isRequired: true, fieldOrder: 3 }
            ]
          }
        ],
        rules: []
      };
      this.newWorkflowMaster = {
        realmCode: this.currentProcess.realmCode,
        workflowCode: 'USMS_JEWELLERY_SALES',
        workflowName: 'Fine Jewellery Hallmark & Custom Commissioning Workflow',
        workflowDesc: 'Multi-stage jewellery intake, gold rate valuation, purity testing, and hallmarking sign-off.',
        category: 'Jewellery Showroom',
        tableName: 'usms_jewellery_inventory',
        formCode: 'FORM_JEWELLERY_VALUATION',
        version: 1,
        active: true,
        stages: [
          { stageCode: 'INTAKE_WEIGHING', stageName: 'Raw Inward Precision Weighing', stageOrder: 1, stageType: 'START', slaHours: 4, allowedRole: 'APPRAISER', defaultAssignee: 'gold.appraiser@dhadiya.com', actionsAllowed: 'APPROVE_WEIGHT, REJECT_SAMPLE', stageColor: '#eab308' },
          { stageCode: 'PURITY_TESTING', stageName: 'Laser Assay Purity & Acid Test', stageOrder: 2, stageType: 'APPROVAL', slaHours: 6, allowedRole: 'QUALITY_EXAMINER', defaultAssignee: 'assay.lab@dhadiya.com', actionsAllowed: 'CERTIFY_PURITY, RETEST', stageColor: '#f59e0b' },
          { stageCode: 'BIS_HALLMARKING', stageName: 'BIS Laser Hallmark Inscription', stageOrder: 3, stageType: 'APPROVAL', slaHours: 8, allowedRole: 'HALLMARK_OFFICER', defaultAssignee: 'hallmark@dhadiya.com', actionsAllowed: 'INSCRIBE_AND_SEAL', stageColor: '#10b981' },
          { stageCode: 'VAULT_DISPLAY', stageName: 'Vault Stocking & Floor Display', stageOrder: 4, stageType: 'END', slaHours: 4, allowedRole: 'SHOWROOM_MANAGER', defaultAssignee: 'vault.manager@dhadiya.com', actionsAllowed: 'ACCEPT_TO_SHOWROOM', stageColor: '#059669' }
        ],
        triggers: [
          { triggerName: 'WeightToAssay', sourceStageCode: 'INTAKE_WEIGHING', actionName: 'APPROVE_WEIGHT', targetStageCode: 'PURITY_TESTING' },
          { triggerName: 'AssayToHallmark', sourceStageCode: 'PURITY_TESTING', actionName: 'CERTIFY_PURITY', targetStageCode: 'BIS_HALLMARKING' },
          { triggerName: 'HallmarkToVault', sourceStageCode: 'BIS_HALLMARKING', actionName: 'INSCRIBE_AND_SEAL', targetStageCode: 'VAULT_DISPLAY' }
        ]
      };
      this.notify('Loaded Jewellery Showroom Process Template with dynamic attributes (Purity, Making Charges, Hallmark)!', 'success');
    } else if (type === 'ELECTRONICS') {
      this.currentProcess = {
        realmCode: this.currentProcess.realmCode,
        processKey: 'USMS_ELECTRONICS_POS',
        name: 'Electronics & Mobile Device IMEI Serialization & Warranty Process',
        category: 'Electronics Showroom',
        industryType: 'ELECTRONICS',
        description: 'Serialized IMEI/MAC intake, unboxing diagnostics, warranty activation, and customer checkout dispatch.',
        tableName: 'usms_electronics_inventory'
      };
      this.newTable = {
        realmCode: this.currentProcess.realmCode,
        tableName: 'usms_electronics_inventory',
        label: 'Serialized Electronics & Mobile Inventory',
        description: 'Dynamic table for phones, laptops, and appliances with IMEI 1, IMEI 2, RAM, storage, warranty period, and unboxing inspection.',
        processKey: 'USMS_ELECTRONICS_POS'
      };
      this.tableColumns = [
        { name: 'imeiCode', label: 'IMEI 1 / Serial Number', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'modelName', label: 'Device Model', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'storageCapacity', label: 'Storage (GB/TB)', type: 'select', options: '128GB, 256GB, 512GB, 1TB', required: true, filterable: true, sortable: true },
        { name: 'ramSize', label: 'RAM Memory', type: 'select', options: '8GB, 12GB, 16GB, 32GB', required: true, filterable: true, sortable: true },
        { name: 'colorFinish', label: 'Device Color Finish', type: 'text', required: true, filterable: true, sortable: true },
        { name: 'warrantyMonths', label: 'Warranty (Months)', type: 'number', required: true, filterable: true, sortable: true },
        { name: 'showroomLocation', label: 'Store Branch', type: 'select', options: 'Main Store, Branch 1, Branch 2', required: true, filterable: true, sortable: true }
      ];
      this.newFormMaster = {
        realmCode: this.currentProcess.realmCode,
        formCode: 'FORM_ELECTRONICS_INTAKE',
        formName: 'Electronics & Mobile IMEI Verification Protocol',
        formDesc: 'Dynamic protocol for IMEI scanning, battery diagnostics, and warranty registration.',
        category: 'Electronics Showroom',
        tableName: 'usms_electronics_inventory',
        status: 'PUBLISHED',
        version: 1,
        sections: [
          {
            sectionKey: 'SEC_DEVICE_SPECS',
            title: 'Device Specs & Serial Identification',
            sectionOrder: 1,
            columnLayout: 2,
            components: [
              { fieldKey: 'imeiCode', fieldLabel: 'IMEI 1 / Primary Serial No', componentType: 'TEXT', placeholder: '354892019283741', isRequired: true, fieldOrder: 1 },
              { fieldKey: 'modelName', fieldLabel: 'Device Model & Brand', componentType: 'TEXT', placeholder: 'iPhone 16 Pro Max 256GB', isRequired: true, fieldOrder: 2 },
              { fieldKey: 'storageCapacity', fieldLabel: 'Storage Capacity', componentType: 'SELECT', optionsJson: '["128GB","256GB","512GB","1TB"]', isRequired: true, fieldOrder: 3 },
              { fieldKey: 'warrantyMonths', fieldLabel: 'Manufacturer Warranty (Months)', componentType: 'NUMBER', placeholder: '24', isRequired: true, fieldOrder: 4 }
            ]
          }
        ],
        rules: []
      };
      this.newWorkflowMaster = {
        realmCode: this.currentProcess.realmCode,
        workflowCode: 'USMS_ELECTRONICS_POS',
        workflowName: 'Electronics & Mobile Device Serialization & Warranty Lifecycle',
        workflowDesc: 'IMEI barcode intake, testing, warranty activation, and customer sale handover.',
        category: 'Electronics Showroom',
        tableName: 'usms_electronics_inventory',
        formCode: 'FORM_ELECTRONICS_INTAKE',
        version: 1,
        active: true,
        stages: [
          { stageCode: 'SCAN_INTAKE', stageName: 'Barcode / IMEI Inward Scanning', stageOrder: 1, stageType: 'START', slaHours: 2, allowedRole: 'INVENTORY_STAFF', defaultAssignee: 'inventory@electronics.dhadiya.com', actionsAllowed: 'PASS_BARCODE, RETEST_IMEI', stageColor: '#3b82f6' },
          { stageCode: 'DEVICE_TESTING', stageName: 'Screen & Power-on Diagnostics', stageOrder: 2, stageType: 'APPROVAL', slaHours: 4, allowedRole: 'DEVICE_TECH', defaultAssignee: 'tech@electronics.dhadiya.com', actionsAllowed: 'CERTIFY_DEVICE, DEFECT_RMA', stageColor: '#f59e0b' },
          { stageCode: 'WARRANTY_ACTIVATION', stageName: 'OEM Warranty Registration & POS Stage', stageOrder: 3, stageType: 'END', slaHours: 2, allowedRole: 'CASHIER', defaultAssignee: 'pos.cashier@dhadiya.com', actionsAllowed: 'ACTIVATE_AND_SELL', stageColor: '#10b981' }
        ],
        triggers: [
          { triggerName: 'ScanToTest', sourceStageCode: 'SCAN_INTAKE', actionName: 'PASS_BARCODE', targetStageCode: 'DEVICE_TESTING' },
          { triggerName: 'TestToWarranty', sourceStageCode: 'DEVICE_TESTING', actionName: 'CERTIFY_DEVICE', targetStageCode: 'WARRANTY_ACTIVATION' }
        ]
      };
      this.notify('Loaded Electronics & Mobile Device Process Template with IMEI attributes and warranty workflow!', 'success');
    } else {
      // Default Automobile
      this.currentProcess = {
        realmCode: this.currentProcess.realmCode,
        processKey: 'WF_SHOWROOM_DELIVERY_HANDOVER',
        name: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
        category: 'Showroom Operations',
        industryType: 'AUTOMOBILE',
        description: '5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.',
        tableName: 'dhadiya_showroom_inventory'
      };
      this.newTable = {
        realmCode: this.currentProcess.realmCode,
        tableName: 'dhadiya_showroom_inventory',
        label: 'Global Showroom Floor & Vehicle Inventory',
        description: 'Dynamic schema for vehicle inventory with VIN, trim, location, MSRP, and floor status.',
        processKey: 'WF_SHOWROOM_DELIVERY_HANDOVER'
      };
      this.notify('Loaded Luxury Automobile Showroom Process Template!', 'success');
    }
  }

  // Live Test Runner inside Process
  runTestCase(): void {
    const payload = {
      realmCode: this.currentProcess.realmCode,
      workflowCode: this.currentProcess.processKey,
      initiatedBy: this.testCaseInitiator,
      priority: this.testCasePriority,
      initialPayloadJson: JSON.stringify(this.previewFormData)
    };

    this.orchestrationService.startWorkflowCase(payload).subscribe({
      next: (res) => {
        this.testRunResult = res;
        this.notify(`Live Case ${res.caseNumber} initiated successfully under process '${this.currentProcess.name}'! Backing table row synchronized!`, 'success');
      },
      error: (err) => {
        this.notify('Error starting test case: ' + (err.message || 'Server error'), 'error');
      }
    });
  }

  // Column management
  addColumn(): void {
    this.tableColumns.push({
      name: 'field_' + (this.tableColumns.length + 1),
      label: 'New Field ' + (this.tableColumns.length + 1),
      type: 'text',
      required: false,
      filterable: true,
      sortable: true
    });
  }

  removeColumn(index: number): void {
    this.tableColumns.splice(index, 1);
  }

  // Section & component management
  addSection(): void {
    const num = this.newFormMaster.sections ? this.newFormMaster.sections.length + 1 : 1;
    if (!this.newFormMaster.sections) this.newFormMaster.sections = [];
    this.newFormMaster.sections.push({
      sectionKey: 'SEC_CUSTOM_' + num,
      title: 'New Section ' + num,
      sectionOrder: num,
      columnLayout: 2,
      components: []
    });
    this.selectedSectionIndex = this.newFormMaster.sections.length - 1;
  }

  addComponentToSelectedSection(): void {
    if (!this.newFormMaster.sections || !this.newFormMaster.sections[this.selectedSectionIndex]) return;
    const comps = this.newFormMaster.sections[this.selectedSectionIndex].components || [];
    const num = comps.length + 1;
    comps.push({
      fieldKey: 'customField_' + num,
      fieldLabel: 'Custom Field ' + num,
      componentType: 'TEXT',
      placeholder: 'Enter value...',
      isRequired: false,
      fieldOrder: num
    });
    this.newFormMaster.sections[this.selectedSectionIndex].components = comps;
  }

  removeComponent(compIndex: number): void {
    const sec = this.newFormMaster.sections ? this.newFormMaster.sections[this.selectedSectionIndex] : null;
    if (sec && sec.components) {
      sec.components.splice(compIndex, 1);
    }
  }

  // Stage management
  addWorkflowStage(): void {
    if (!this.newWorkflowMaster.stages) this.newWorkflowMaster.stages = [];
    const num = this.newWorkflowMaster.stages.length + 1;
    this.newWorkflowMaster.stages.push({
      stageCode: 'STAGE_' + num,
      stageName: 'Operational Stage ' + num,
      stageOrder: num,
      stageType: 'INTERMEDIATE',
      slaHours: 24,
      allowedRole: 'SHOWROOM_STAFF',
      actionsAllowed: 'APPROVE, REJECT',
      stageColor: '#0284c7'
    });
  }

  removeWorkflowStage(index: number): void {
    if (!this.newWorkflowMaster.stages) return;
    this.newWorkflowMaster.stages.splice(index, 1);
  }

  addTrigger(): void {
    if (!this.newWorkflowMaster.triggers) this.newWorkflowMaster.triggers = [];
    const num = this.newWorkflowMaster.triggers.length + 1;
    this.newWorkflowMaster.triggers.push({
      triggerName: 'Trigger_' + num,
      sourceStageCode: this.newWorkflowMaster.stages && this.newWorkflowMaster.stages.length > 0 ? this.newWorkflowMaster.stages[0].stageCode : '',
      actionName: 'ACTION_' + num,
      targetStageCode: this.newWorkflowMaster.stages && this.newWorkflowMaster.stages.length > 1 ? this.newWorkflowMaster.stages[1].stageCode : ''
    });
  }

  removeTrigger(index: number): void {
    if (!this.newWorkflowMaster.triggers) return;
    this.newWorkflowMaster.triggers.splice(index, 1);
  }

  selectSection(idx: number): void {
    this.selectedSectionIndex = idx;
  }

  removeSection(idx: number): void {
    if (!this.newFormMaster.sections) return;
    this.newFormMaster.sections.splice(idx, 1);
    if (this.selectedSectionIndex >= this.newFormMaster.sections.length) {
      this.selectedSectionIndex = Math.max(0, this.newFormMaster.sections.length - 1);
    }
  }

  parseOptions(optionsStr?: string): string[] {
    if (!optionsStr) return [];
    return optionsStr.split(',').map(s => s.trim()).filter(s => s.length > 0);
  }

  parseJsonOptions(jsonStr?: string): string[] {
    if (!jsonStr) return [];
    try {
      return JSON.parse(jsonStr);
    } catch {
      return [];
    }
  }

  notify(msg: string, type: 'success' | 'error' = 'success'): void {
    this.notificationMessage = msg;
    this.notificationType = type;
    setTimeout(() => {
      this.notificationMessage = '';
    }, 6000);
  }
}
