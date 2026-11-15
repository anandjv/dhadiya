import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { TenantsComponent } from './features/tenants/tenants.component';
import { WorkflowsComponent } from './features/workflows/workflows.component';
import { DataGridComponent } from './features/datagrid/datagrid.component';
import { StudioComponent } from './features/studio/studio.component';
import { UsersComponent } from './features/users/users.component';
import { TasksComponent } from './features/tasks/tasks.component';
import { ReportsComponent } from './features/reports/reports.component';
import { ActivityLogsComponent } from './features/activity-logs/activity-logs.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'users', component: UsersComponent },
  { path: 'tenants', component: TenantsComponent },
  { path: 'workflows', component: WorkflowsComponent },
  { path: 'tasks', component: TasksComponent },
  { path: 'datagrid', component: DataGridComponent },
  { path: 'studio', component: StudioComponent },
  { path: 'reports', component: ReportsComponent },
  { path: 'activity-logs', component: ActivityLogsComponent },
  { path: '**', redirectTo: 'dashboard' }
];
