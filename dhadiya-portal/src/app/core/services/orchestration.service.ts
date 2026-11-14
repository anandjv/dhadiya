import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  DynamicFormTemplate,
  FormMaster,
  ProcessDefinition,
  ProcessInstance,
  ProcessTask,
  WorkflowCaseResponse,
  WorkflowMaster,
  WorkflowTaskItem,
  WorkflowTrackDet,
  WorkflowTrackMaster
} from '../models/process.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrchestrationService {
  private processUrl = environment.processApiUrl;
  private formUrl = environment.formsApiUrl;
  private taskUrl = environment.tasksApiUrl;
  private engineUrl = '/api/v1/engine';
  private dynamicFormUrl = '/api/v1/dynamic-forms';

  constructor(private http: HttpClient) {}

  // ==========================================
  // eFormsPi CORE WORKFLOW ENGINE APIS
  // ==========================================

  getWorkflows(realmCode = 'dhadiya-showroom-global'): Observable<WorkflowMaster[]> {
    return this.http.get<WorkflowMaster[]>(`${this.engineUrl}/workflows?realmCode=${realmCode}`);
  }

  getWorkflow(workflowCode: string): Observable<WorkflowMaster> {
    return this.http.get<WorkflowMaster>(`${this.engineUrl}/workflows/${workflowCode}`);
  }

  saveWorkflowMaster(workflow: WorkflowMaster): Observable<WorkflowMaster> {
    return this.http.post<WorkflowMaster>(`${this.engineUrl}/workflows`, workflow);
  }

  startWorkflowCase(payload: { realmCode: string; workflowCode: string; initiatedBy: string; priority?: string; initialPayloadJson?: string }): Observable<WorkflowTrackMaster> {
    return this.http.post<WorkflowTrackMaster>(`${this.engineUrl}/cases/start`, payload);
  }

  getCases(realmCode = 'dhadiya-showroom-global'): Observable<WorkflowTrackMaster[]> {
    return this.http.get<WorkflowTrackMaster[]>(`${this.engineUrl}/cases?realmCode=${realmCode}`);
  }

  getCaseDetails(caseNumber: string): Observable<WorkflowCaseResponse> {
    return this.http.get<WorkflowCaseResponse>(`${this.engineUrl}/cases/${caseNumber}`);
  }

  getCaseHistory(caseNumber: string): Observable<WorkflowTrackDet[]> {
    return this.http.get<WorkflowTrackDet[]>(`${this.engineUrl}/cases/${caseNumber}/history`);
  }

  executeCaseAction(caseNumber: string, action: { actionName: string; actedBy: string; remarks?: string; targetStageCode?: string; updatedPayloadJson?: string }): Observable<WorkflowTrackMaster> {
    return this.http.post<WorkflowTrackMaster>(`${this.engineUrl}/cases/${caseNumber}/action`, action);
  }

  getPendingWorkflowTasks(assignedTo?: string, roleName?: string): Observable<WorkflowTaskItem[]> {
    let url = `${this.engineUrl}/tasks/pending`;
    const params: string[] = [];
    if (assignedTo) params.push(`assignedTo=${encodeURIComponent(assignedTo)}`);
    if (roleName) params.push(`roleName=${encodeURIComponent(roleName)}`);
    if (params.length > 0) url += `?${params.join('&')}`;
    return this.http.get<WorkflowTaskItem[]>(url);
  }

  // ==========================================
  // eFormsPi CORE DYNAMIC FORMS APIS
  // ==========================================

  getFormMasters(realmCode = 'dhadiya-showroom-global'): Observable<FormMaster[]> {
    return this.http.get<FormMaster[]>(`${this.dynamicFormUrl}/masters?realmCode=${realmCode}`);
  }

  getFormMaster(formCode: string): Observable<FormMaster> {
    return this.http.get<FormMaster>(`${this.dynamicFormUrl}/masters/${formCode}`);
  }

  saveFormMaster(form: FormMaster): Observable<FormMaster> {
    return this.http.post<FormMaster>(`${this.dynamicFormUrl}/masters`, form);
  }

  // ==========================================
  // LEGACY COMPATIBILITY METHODS
  // ==========================================

  getDefinitions(realmCode = 'dhadiya-showroom-global'): Observable<ProcessDefinition[]> {
    return this.http.get<ProcessDefinition[]>(`${this.processUrl}/definitions?realmCode=${realmCode}`);
  }

  saveDefinition(definition: ProcessDefinition): Observable<ProcessDefinition> {
    return this.http.post<ProcessDefinition>(`${this.processUrl}/definitions`, definition);
  }

  getInstances(realmCode = 'dhadiya-showroom-global'): Observable<ProcessInstance[]> {
    return this.http.get<ProcessInstance[]>(`${this.processUrl}/instances?realmCode=${realmCode}`);
  }

  startProcess(payload: { realmCode: string; processKey: string; initiatedBy: string; payloadJson?: string }): Observable<ProcessInstance> {
    return this.http.post<ProcessInstance>(`${this.processUrl}/instances/start`, payload);
  }

  transition(trackingNumber: string, transition: { targetStage: string; action: string; remarks?: string }): Observable<ProcessInstance> {
    return this.http.post<ProcessInstance>(`${this.processUrl}/instances/${trackingNumber}/transition`, transition);
  }

  getPendingTasks(assignee?: string): Observable<ProcessTask[]> {
    const url = assignee ? `${this.taskUrl}/pending?assignee=${assignee}` : `${this.taskUrl}/pending`;
    return this.http.get<ProcessTask[]>(url);
  }

  completeTask(taskId: number, action: { status: string; remarks?: string }): Observable<ProcessTask> {
    return this.http.post<ProcessTask>(`${this.taskUrl}/${taskId}/action`, action);
  }

  getTemplates(realmCode = 'dhadiya-showroom-global'): Observable<DynamicFormTemplate[]> {
    return this.http.get<DynamicFormTemplate[]>(`${this.formUrl}?realmCode=${realmCode}`);
  }

  saveTemplate(template: { realmCode: string; templateKey: string; title: string; schemaJson: string; status?: string }): Observable<DynamicFormTemplate> {
    return this.http.post<DynamicFormTemplate>(this.formUrl, template);
  }
}
