import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { OrchestrationService } from '../../core/services/orchestration.service';
import { RealmService } from '../../core/services/realm.service';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './reports.component.html'
})
export class ReportsComponent implements OnInit {
  analytics: any = null;
  reportDefs: any[] = [];
  selectedRealm = 'dhadiya-showroom-global';
  activeReportResult: any = null;
  loading = false;
  runningKey: string | null = null;

  constructor(
    private orchestrationService: OrchestrationService,
    private realmService: RealmService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.orchestrationService.getAnalytics(this.selectedRealm).subscribe({
      next: (data) => {
        this.analytics = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });

    this.orchestrationService.getReportDefinitions(this.selectedRealm).subscribe({
      next: (defs) => this.reportDefs = defs || [],
      error: () => this.reportDefs = []
    });
  }

  runReport(reportKey: string): void {
    this.runningKey = reportKey;
    this.orchestrationService.runReport(reportKey, this.selectedRealm).subscribe({
      next: (res) => {
        this.activeReportResult = res;
        this.runningKey = null;
      },
      error: () => {
        this.runningKey = null;
      }
    });
  }

  closeReportModal(): void {
    this.activeReportResult = null;
  }

  getStageKeys(): string[] {
    return this.analytics?.stageBreakdown ? Object.keys(this.analytics.stageBreakdown) : [];
  }

  getPriorityKeys(): string[] {
    return this.analytics?.priorityBreakdown ? Object.keys(this.analytics.priorityBreakdown) : [];
  }
}
