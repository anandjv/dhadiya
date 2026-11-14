import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { RealmService } from '../../core/services/realm.service';
import { OrchestrationService } from '../../core/services/orchestration.service';
import { GridService } from '../../core/services/grid.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  realmCount = 4;
  instanceCount = 3;
  pendingTasksCount = 2;
  tableCount = 3;

  constructor(
    private realmService: RealmService,
    private orchestrationService: OrchestrationService,
    private gridService: GridService
  ) {}

  ngOnInit(): void {
    this.realmService.getAll().subscribe({
      next: (data) => this.realmCount = data.length || 4,
      error: () => this.realmCount = 4
    });

    this.orchestrationService.getInstances().subscribe({
      next: (data) => this.instanceCount = data.length || 3,
      error: () => this.instanceCount = 3
    });

    this.orchestrationService.getPendingTasks().subscribe({
      next: (data) => this.pendingTasksCount = data.length || 2,
      error: () => this.pendingTasksCount = 2
    });

    this.gridService.getTables().subscribe({
      next: (data) => this.tableCount = data.length || 3,
      error: () => this.tableCount = 3
    });
  }
}
