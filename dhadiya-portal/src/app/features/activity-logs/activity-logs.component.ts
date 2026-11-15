import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrchestrationService } from '../../core/services/orchestration.service';
import { RealmService } from '../../core/services/realm.service';

@Component({
  selector: 'app-activity-logs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './activity-logs.component.html'
})
export class ActivityLogsComponent implements OnInit {
  logs: any[] = [];
  selectedRealm = '';
  filterAction = '';
  searchTerm = '';
  loading = false;

  constructor(
    private orchestrationService: OrchestrationService,
    private realmService: RealmService
  ) {}

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    this.loading = true;
    this.orchestrationService.getActivityLogs(this.selectedRealm).subscribe({
      next: (data) => {
        this.logs = data || [];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  get filteredLogs(): any[] {
    return this.logs.filter(log => {
      const matchAction = !this.filterAction || log.actionType === this.filterAction;
      const term = this.searchTerm.toLowerCase();
      const matchSearch = !term ||
        log.userEmail?.toLowerCase().includes(term) ||
        log.summary?.toLowerCase().includes(term) ||
        log.entityId?.toLowerCase().includes(term);
      return matchAction && matchSearch;
    });
  }

  getActionBadgeClass(actionType: string): string {
    switch (actionType) {
      case 'WORKFLOW_TRANSITION': return 'badge-workflow';
      case 'LOGIN': return 'badge-login';
      case 'RECORD_INSERT': return 'badge-record';
      default: return 'badge-default';
    }
  }
}
