import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { TenantsComponent } from './features/tenants/tenants.component';
import { WorkflowsComponent } from './features/workflows/workflows.component';
import { DataGridComponent } from './features/datagrid/datagrid.component';
import { StudioComponent } from './features/studio/studio.component';
import { UsersComponent } from './features/users/users.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'users', component: UsersComponent },
  { path: 'tenants', component: TenantsComponent },
  { path: 'workflows', component: WorkflowsComponent },
  { path: 'datagrid', component: DataGridComponent },
  { path: 'studio', component: StudioComponent },
  { path: '**', redirectTo: 'dashboard' }
];
