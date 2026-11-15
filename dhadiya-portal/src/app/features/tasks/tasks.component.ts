import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { OrchestrationService } from '../../core/services/orchestration.service';
import { RealmService } from '../../core/services/realm.service';
import { WorkflowTaskItem, WorkflowTrackMaster } from '../../core/models/process.model';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './tasks.component.html'
})
export class TasksComponent implements OnInit {
  viewMode: 'kanban' | 'list' = 'kanban';
  kanbanBoard: any = null;
  pendingTasks: WorkflowTaskItem[] = [];
  cases: WorkflowTrackMaster[] = [];
  selectedRealm = 'dhadiya-showroom-global';
  selectedWorkflow = 'WF_SHOWROOM_DELIVERY_HANDOVER';
  searchTerm = '';
  loading = false;
  completingTaskId: number | null = null;
  activeActionModal: { task?: WorkflowTaskItem; caseItem?: any; actionName: string } | null = null;
  actionRemarks = '';

  constructor(
    private orchestrationService: OrchestrationService,
    private realmService: RealmService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.orchestrationService.getKanbanBoard(this.selectedWorkflow, this.selectedRealm).subscribe({
      next: (data) => {
        this.kanbanBoard = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

    this.orchestrationService.getPendingWorkflowTasks().subscribe({
      next: (tasks) => this.pendingTasks = tasks || [],
      error: () => this.pendingTasks = []
    });

    this.orchestrationService.getCases(this.selectedRealm).subscribe({
      next: (cases) => this.cases = cases || [],
      error: () => this.cases = []
    });
  }

  onWorkflowChange(wfCode: string): void {
    this.selectedWorkflow = wfCode;
    this.loadData();
  }

  openCompleteModal(task: WorkflowTaskItem): void {
    this.activeActionModal = { task, actionName: 'COMPLETE' };
    this.actionRemarks = '';
  }

  closeModal(): void {
    this.activeActionModal = null;
    this.actionRemarks = '';
  }

  confirmCompleteTask(): void {
    if (!this.activeActionModal?.task?.id) return;
    const taskId = this.activeActionModal.task.id;
    this.completingTaskId = taskId;
    this.orchestrationService.completeTaskItem(taskId, 'COMPLETED', this.actionRemarks).subscribe({
      next: () => {
        this.completingTaskId = null;
        this.closeModal();
        this.loadData();
      },
      error: () => {
        this.completingTaskId = null;
        this.closeModal();
        this.loadData();
      }
    });
  }

  parsePayload(jsonStr?: string): any {
    if (!jsonStr) return {};
    try {
      return JSON.parse(jsonStr);
    } catch {
      return {};
    }
  }

  get filteredTasks(): WorkflowTaskItem[] {
    if (!this.searchTerm.trim()) return this.pendingTasks;
    const term = this.searchTerm.toLowerCase();
    return this.pendingTasks.filter(t =>
      t.caseNumber?.toLowerCase().includes(term) ||
      t.taskTitle?.toLowerCase().includes(term) ||
      t.assignedTo?.toLowerCase().includes(term) ||
      t.stageCode?.toLowerCase().includes(term)
    );
  }
}
