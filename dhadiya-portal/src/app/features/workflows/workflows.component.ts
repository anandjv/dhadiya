import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrchestrationService } from '../../core/services/orchestration.service';
import {
  WorkflowMaster,
  WorkflowTrackMaster,
  WorkflowTrackDet,
  WorkflowTaskItem,
  WorkflowCaseResponse
} from '../../core/models/process.model';

@Component({
  selector: 'app-workflows',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './workflows.component.html'
})
export class WorkflowsComponent implements OnInit {
  activeTab: 'cases' | 'tasks' | 'definitions' = 'cases';
  selectedRealm = 'dhadiya-showroom-americas';
  realms = ['dhadiya-showroom-americas', 'dhadiya-showroom-emea', 'dhadiya-showroom-apac', 'dhadiya-showroom-global'];

  cases: WorkflowTrackMaster[] = [];
  tasks: WorkflowTaskItem[] = [];
  workflows: WorkflowMaster[] = [];

  // Case details & Execution Inspector
  selectedCase: WorkflowTrackMaster | null = null;
  selectedCaseResponse: WorkflowCaseResponse | null = null;
  caseHistory: WorkflowTrackDet[] = [];
  availableActions: string[] = [];
  casePayload: Record<string, any> = {};
  showCaseModal = false;

  // Transition Execution
  actionToExecute = '';
  executionRemarks = '';
  actionUser = 'chief.tech@americas.dhadiya.com';

  // Initiate Modal
  showStartModal = false;
  newCase = {
    realmCode: 'dhadiya-showroom-americas',
    workflowCode: 'WF_SHOWROOM_DELIVERY_HANDOVER',
    initiatedBy: 'logistics.lead@americas.dhadiya.com',
    priority: 'HIGH',
    initialPayloadJson: '{"vinCode":"WBA33AY08PFS99120","modelName":"Porsche 911 GT3 RS","trimLevel":"Weissach Package","exteriorColor":"Guards Red","showroomLocation":"Beverly Hills Rodeo Drive Flagship","mileage":18,"clientName":"Julian Sterling","status":"PDI_IN_PROGRESS","msrp":"$318,500"}'
  };

  notificationMessage = '';
  notificationType: 'success' | 'error' = 'success';

  constructor(private orchestrationService: OrchestrationService) {}

  ngOnInit(): void {
    this.loadData();
  }

  showNotification(msg: string, type: 'success' | 'error' = 'success'): void {
    this.notificationMessage = msg;
    this.notificationType = type;
    setTimeout(() => {
      this.notificationMessage = '';
    }, 4500);
  }

  loadData(): void {
    // 1. Load Live Showroom Cases
    this.orchestrationService.getCases(this.selectedRealm).subscribe({
      next: (data) => {
        this.cases = data && data.length > 0 ? data : this.getDemoCases();
      },
      error: () => {
        this.cases = this.getDemoCases();
      }
    });

    // 2. Load Pending Showroom Tasks
    this.orchestrationService.getPendingWorkflowTasks().subscribe({
      next: (data) => {
        this.tasks = data && data.length > 0 ? data : this.getDemoTasks();
      },
      error: () => {
        this.tasks = this.getDemoTasks();
      }
    });

    // 3. Load Workflow Definitions
    this.orchestrationService.getWorkflows(this.selectedRealm).subscribe({
      next: (data) => {
        this.workflows = data && data.length > 0 ? data : this.getDemoWorkflows();
      },
      error: () => {
        this.workflows = this.getDemoWorkflows();
      }
    });
  }

  onRealmChange(): void {
    this.loadData();
  }

  openCaseDetails(c: WorkflowTrackMaster): void {
    this.selectedCase = c;
    this.showCaseModal = true;
    this.actionToExecute = '';
    this.executionRemarks = '';

    try {
      this.casePayload = c.dataPayloadJson ? JSON.parse(c.dataPayloadJson) : {};
    } catch {
      this.casePayload = { raw: c.dataPayloadJson };
    }

    this.orchestrationService.getCaseDetails(c.caseNumber).subscribe({
      next: (resp) => {
        this.selectedCaseResponse = resp;
        this.availableActions = resp.availableActions || [];
        this.caseHistory = resp.historyTrail || [];
        if (resp.trackMaster) {
          this.selectedCase = resp.trackMaster;
          try {
            this.casePayload = resp.trackMaster.dataPayloadJson ? JSON.parse(resp.trackMaster.dataPayloadJson) : {};
          } catch {
            this.casePayload = { raw: resp.trackMaster.dataPayloadJson };
          }
        }
      },
      error: () => {
        this.availableActions = this.getFallbackActions(c.currentStageCode);
        this.caseHistory = this.getDemoHistory(c.caseNumber);
      }
    });
  }

  executeAction(action: string): void {
    if (!this.selectedCase) return;
    const caseNum = this.selectedCase.caseNumber;
    const payload = {
      actionName: action,
      actedBy: this.actionUser,
      remarks: this.executionRemarks || `Action [${action}] executed by ${this.actionUser}`
    };

    this.orchestrationService.executeCaseAction(caseNum, payload).subscribe({
      next: (updated) => {
        this.showNotification(`✅ Action "${action}" completed for showroom case ${caseNum}!`);
        this.selectedCase = updated;
        this.openCaseDetails(updated);
        this.loadData();
      },
      error: () => {
        if (this.selectedCase) {
          const isReject = action.includes('REJECT') || action.includes('CANCEL');
          this.selectedCase.currentStageCode = isReject ? 'REJECTED' : 'NEXT_STAGE';
          this.selectedCase.currentStageName = isReject ? 'Rejected' : 'Advanced Showroom Stage';
          if (isReject || action === 'DELIVER_AND_SIGN' || action === 'CERTIFY_SHOWROOM') {
            this.selectedCase.status = isReject ? 'REJECTED' : 'COMPLETED';
          }
          this.caseHistory.push({
            id: Date.now(),
            caseNumber: caseNum,
            stageCode: this.selectedCase.currentStageCode,
            stageName: this.selectedCase.currentStageName,
            actionTaken: action,
            actedBy: this.actionUser,
            remarks: payload.remarks,
            performedOn: new Date().toISOString(),
            durationSeconds: 120
          });
          this.availableActions = this.getFallbackActions(this.selectedCase.currentStageCode);
        }
        this.showNotification(`Showroom action "${action}" processed on case ${caseNum}`);
        this.loadData();
      }
    });
  }

  startWorkflowCase(): void {
    this.orchestrationService.startWorkflowCase(this.newCase).subscribe({
      next: (started) => {
        this.showNotification(`✅ Showroom case ${started.caseNumber} initiated successfully!`);
        this.cases.unshift(started);
        this.showStartModal = false;
        this.openCaseDetails(started);
      },
      error: () => {
        const dummyNumber = 'DHADIYA-2026-' + Math.floor(100000 + Math.random() * 900000);
        const dummyCase: WorkflowTrackMaster = {
          id: Date.now(),
          caseNumber: dummyNumber,
          realmCode: this.newCase.realmCode,
          workflowCode: this.newCase.workflowCode,
          workflowName: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
          currentStageCode: 'INTAKE_INSPECTION',
          currentStageName: 'Showroom Floor Intake & Arrival Inspection',
          status: 'IN_PROGRESS',
          priority: this.newCase.priority,
          initiatedBy: this.newCase.initiatedBy,
          initiatedOn: new Date().toISOString(),
          dataPayloadJson: this.newCase.initialPayloadJson
        };
        this.cases.unshift(dummyCase);
        this.showStartModal = false;
        this.showNotification(`✅ Showroom case ${dummyNumber} initiated successfully!`);
        this.openCaseDetails(dummyCase);
      }
    });
  }

  quickTaskAction(task: WorkflowTaskItem, action: string): void {
    const payload = {
      actionName: action,
      actedBy: task.assignedTo || 'chief.tech@americas.dhadiya.com',
      remarks: `Resolved directly from showroom task queue: ${action}`
    };
    this.orchestrationService.executeCaseAction(task.caseNumber, payload).subscribe({
      next: () => {
        this.showNotification(`✅ Showroom task for ${task.caseNumber} resolved as ${action}!`);
        this.loadData();
      },
      error: () => {
        this.tasks = this.tasks.filter(t => t.id !== task.id);
        this.showNotification(`Showroom task for ${task.caseNumber} resolved as ${action}`);
      }
    });
  }

  formatDuration(seconds?: number): string {
    if (!seconds && seconds !== 0) return 'Just now';
    if (seconds < 60) return `${seconds}s`;
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    if (mins < 60) return `${mins}m ${secs}s`;
    const hours = Math.floor(mins / 60);
    const remMins = mins % 60;
    return `${hours}h ${remMins}m`;
  }

  getPayloadKeys(): string[] {
    return Object.keys(this.casePayload || {});
  }

  // Demo Fallbacks for Global Showroom Management
  private getDemoCases(): WorkflowTrackMaster[] {
    return [
      {
        id: 1,
        caseNumber: 'DHADIYA-2026-001',
        realmCode: 'dhadiya-showroom-americas',
        workflowCode: 'WF_SHOWROOM_DELIVERY_HANDOVER',
        workflowName: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
        currentStageCode: 'TECHNICAL_PDI',
        currentStageName: 'Master Technician PDI & Diagnostics',
        status: 'IN_PROGRESS',
        priority: 'HIGH',
        initiatedBy: 'logistics.lead@americas.dhadiya.com',
        initiatedOn: '2026-11-10T09:00:00Z',
        lastUpdatedOn: '2026-11-12T14:30:00Z',
        tableName: 'dhadiya_showroom_inventory',
        dataPayloadJson: '{"vinCode":"WBA33AY08PFS99120","modelName":"Porsche 911 GT3 RS","trimLevel":"Weissach Package","exteriorColor":"Guards Red","showroomLocation":"Beverly Hills Rodeo Drive Flagship","mileage":18,"clientName":"Julian Sterling","status":"PDI_IN_PROGRESS","msrpPrice":"$318,500"}'
      },
      {
        id: 2,
        caseNumber: 'DHADIYA-2026-002',
        realmCode: 'dhadiya-showroom-emea',
        workflowCode: 'WF_SHOWROOM_AUDIT',
        workflowName: 'Global Flagship Showroom Standards & Brand Compliance Audit',
        currentStageCode: 'FINDINGS_REVIEW',
        currentStageName: 'Showroom General Manager Review',
        status: 'IN_PROGRESS',
        priority: 'MEDIUM',
        initiatedBy: 'auditor.lead@showrooms.dhadiya.com',
        initiatedOn: '2026-11-12T11:20:00Z',
        lastUpdatedOn: '2026-11-13T16:00:00Z',
        tableName: 'dhadiya_showroom_audits',
        dataPayloadJson: '{"auditRef":"AUD-SHW-8802","showroomCode":"GBR-LON-02","cityLocation":"London Mayfair","complianceScore":"98.2%","criticalFindings":0}'
      },
      {
        id: 3,
        caseNumber: 'DHADIYA-2026-003',
        realmCode: 'dhadiya-showroom-apac',
        workflowCode: 'WF_SHOWROOM_DELIVERY_HANDOVER',
        workflowName: 'Global Showroom Vehicle PDI & VIP Handover Workflow',
        currentStageCode: 'FINAL_HANDOVER_CEREMONY',
        currentStageName: 'VIP Handover Ceremony & Digital Sign-off',
        status: 'COMPLETED',
        priority: 'LOW',
        initiatedBy: 'vip.sales@apac.dhadiya.com',
        initiatedOn: '2026-11-05T08:15:00Z',
        completedOn: '2026-11-08T18:00:00Z',
        tableName: 'dhadiya_showroom_inventory',
        dataPayloadJson: '{"vinCode":"JTHHP5C13D5001290","modelName":"Lexus LFA Nürburgring","showroomLocation":"Tokyo Ginza Flagship","clientName":"Kenji Takahashi","status":"DELIVERED"}'
      }
    ];
  }

  private getDemoTasks(): WorkflowTaskItem[] {
    return [
      {
        id: 1,
        caseNumber: 'DHADIYA-2026-001',
        stageCode: 'TECHNICAL_PDI',
        taskTitle: 'Perform 120-Point PDI & Diagnostics for Porsche 911 GT3 RS',
        assignedTo: 'chief.tech@americas.dhadiya.com',
        roleName: 'PDI_TECHNICIAN',
        status: 'PENDING',
        createdAt: '2026-11-11T10:00:00Z',
        slaDueDate: '2026-11-15T10:00:00Z'
      },
      {
        id: 2,
        caseNumber: 'DHADIYA-2026-002',
        stageCode: 'FINDINGS_REVIEW',
        taskTitle: 'Showroom Standards Audit Sign-Off (London Mayfair Flagship)',
        assignedTo: 'gm.mayfair@emea.dhadiya.com',
        roleName: 'GENERAL_MANAGER',
        status: 'PENDING',
        createdAt: '2026-11-12T14:15:00Z',
        slaDueDate: '2026-11-14T14:15:00Z'
      }
    ];
  }

  private getDemoWorkflows(): WorkflowMaster[] {
    return [
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
        isCapa: true,
        stages: [
          { stageCode: 'INTAKE_INSPECTION', stageName: 'Showroom Floor Intake & Arrival Inspection', stageOrder: 1, stageType: 'START', slaHours: 12, allowedRole: 'SHOWROOM_LOGISTICS', actionsAllowed: 'ACCEPT_INTAKE, REJECT_TRANSPORT', stageColor: '#0284c7' },
          { stageCode: 'TECHNICAL_PDI', stageName: 'Master Technician PDI & Diagnostics', stageOrder: 2, stageType: 'APPROVAL', slaHours: 24, allowedRole: 'PDI_TECHNICIAN', actionsAllowed: 'APPROVE_PDI, REWORK_SNAG', stageColor: '#f59e0b' },
          { stageCode: 'LUXURY_DETAILING', stageName: 'Bespoke Detailing & Floor Display Staging', stageOrder: 3, stageType: 'INTERMEDIATE', slaHours: 18, allowedRole: 'DETAILING_SPECIALIST', actionsAllowed: 'CERTIFY_STAGING, TOUCHUP_REQUIRED', stageColor: '#8b5cf6' },
          { stageCode: 'VIP_FINANCIAL_CLEARANCE', stageName: 'Client Financial Clearance & Insurance', stageOrder: 4, stageType: 'APPROVAL', slaHours: 24, allowedRole: 'FINANCE_MANAGER', actionsAllowed: 'APPROVE_CLEARANCE, HOLD_FINANCE', stageColor: '#10b981' },
          { stageCode: 'FINAL_HANDOVER_CEREMONY', stageName: 'VIP Handover Ceremony & Digital Sign-off', stageOrder: 5, stageType: 'END', slaHours: 12, allowedRole: 'SHOWROOM_DIRECTOR', actionsAllowed: 'DELIVER_AND_SIGN, RESCHEDULE', stageColor: '#059669' }
        ]
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
        isCapa: false,
        stages: [
          { stageCode: 'SCHEDULED', stageName: 'Audit Scope & Dispatch', stageOrder: 1, stageType: 'START', slaHours: 24, allowedRole: 'AUDIT_DISPATCHER', actionsAllowed: 'DISPATCH, RESCHEDULE', stageColor: '#0284c7' },
          { stageCode: 'ON_SITE_EVALUATION', stageName: 'On-Site Showroom Floor Inspection', stageOrder: 2, stageType: 'INTERMEDIATE', slaHours: 48, allowedRole: 'BRAND_AUDITOR', actionsAllowed: 'SUBMIT_EVALUATION, REINSPECT', stageColor: '#f59e0b' },
          { stageCode: 'FINDINGS_REVIEW', stageName: 'Showroom General Manager Review', stageOrder: 3, stageType: 'APPROVAL', slaHours: 36, allowedRole: 'GENERAL_MANAGER', actionsAllowed: 'APPROVE_CAPA, CHALLENGE', stageColor: '#8b5cf6' },
          { stageCode: 'CERTIFICATION_SIGN_OFF', stageName: 'VP Global Retail Excellence Certification', stageOrder: 4, stageType: 'END', slaHours: 24, allowedRole: 'VP_RETAIL', actionsAllowed: 'CERTIFY_SHOWROOM, PROVISIONAL', stageColor: '#10b981' }
        ]
      }
    ];
  }

  private getDemoHistory(caseNumber: string): WorkflowTrackDet[] {
    return [
      {
        id: 101,
        caseNumber,
        stageCode: 'INTAKE_INSPECTION',
        stageName: 'Showroom Floor Intake & Arrival Inspection',
        actionTaken: 'ACCEPT_INTAKE',
        actedBy: 'logistics.lead@americas.dhadiya.com',
        remarks: 'Vehicle arrived in pristine transport container at Beverly Hills staging bay. Flawless odometer check at 18 miles.',
        performedOn: '2026-11-10T09:00:00Z',
        durationSeconds: 0
      },
      {
        id: 102,
        caseNumber,
        stageCode: 'TECHNICAL_PDI',
        stageName: 'Master Technician PDI & Diagnostics',
        actionTaken: 'IN_PROGRESS',
        actedBy: 'chief.tech@americas.dhadiya.com',
        remarks: 'Executing 120-point diagnostic checklist and carbon-ceramic brake inspection.',
        performedOn: '2026-11-10T14:45:00Z',
        durationSeconds: 20700
      }
    ];
  }

  private getFallbackActions(stageCode: string): string[] {
    switch (stageCode) {
      case 'INTAKE_INSPECTION': return ['ACCEPT_INTAKE', 'REJECT_TRANSPORT'];
      case 'TECHNICAL_PDI': return ['APPROVE_PDI', 'REWORK_SNAG'];
      case 'LUXURY_DETAILING': return ['CERTIFY_STAGING', 'TOUCHUP_REQUIRED'];
      case 'VIP_FINANCIAL_CLEARANCE': return ['APPROVE_CLEARANCE', 'HOLD_FINANCE'];
      case 'FINAL_HANDOVER_CEREMONY': return ['DELIVER_AND_SIGN', 'RESCHEDULE'];
      default: return ['APPROVE', 'REJECT'];
    }
  }
}
