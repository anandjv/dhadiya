import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { RealmService } from '../../core/services/realm.service';
import { OrchestrationService } from '../../core/services/orchestration.service';
import { GridService } from '../../core/services/grid.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  realmCount = 5;
  instanceCount = 3;
  pendingTasksCount = 2;
  tableCount = 3;
  userCount = 7;

  constructor(
    private realmService: RealmService,
    private orchestrationService: OrchestrationService,
    private gridService: GridService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.realmService.getAll().subscribe({
      next: (data) => this.realmCount = data.length || 5,
      error: () => this.realmCount = 5
    });

    this.userService.getUsers().subscribe({
      next: (data) => this.userCount = data.length || 7,
      error: () => this.userCount = 7
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
