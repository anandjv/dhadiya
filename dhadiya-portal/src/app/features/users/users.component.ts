import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { RealmService } from '../../core/services/realm.service';
import { LocationService } from '../../core/services/location.service';
import { RoleService } from '../../core/services/role.service';
import { ShowroomUser, UserInviteRequest } from '../../core/models/user.model';
import { TenantRealm } from '../../core/models/realm.model';
import { ShowroomLocation, LocationCreateRequest } from '../../core/models/location.model';
import { ShowroomRole, RoleCreateRequest } from '../../core/models/role.model';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {
  activeTab: 'staff' | 'locations' | 'roles' = 'staff';

  users: ShowroomUser[] = [];
  filteredUsers: ShowroomUser[] = [];
  realms: TenantRealm[] = [];
  locations: ShowroomLocation[] = [];
  filteredLocations: ShowroomLocation[] = [];
  roles: ShowroomRole[] = [];
  filteredRoles: ShowroomRole[] = [];

  loading = true;

  // Filters
  searchTerm = '';
  selectedRealm = 'all';
  selectedLocation = 'all';
  selectedRole = 'all';
  selectedStatus = 'all';

  // Modals
  isUserModalOpen = false;
  isLocationModalOpen = false;
  isRoleModalOpen = false;

  // New User Form State
  newUser: UserInviteRequest = {
    fullName: '',
    email: '',
    realmCode: 'tvs-motors',
    role: 'SALES_ADVISOR',
    locationCode: 'TVS-BLR-01',
    showroomLocation: 'Bangalore Indiranagar Flagship Experience',
    phoneNumber: '',
    status: 'ACTIVE'
  };

  // New Location Form State
  newLocation: LocationCreateRequest = {
    realmCode: 'tvs-motors',
    locationCode: '',
    locationName: '',
    city: '',
    stateProvince: '',
    country: 'India',
    address: '',
    phoneNumber: '',
    managerEmail: '',
    status: 'ACTIVE'
  };

  // New Dynamic Role Form State
  newRole: RoleCreateRequest = {
    realmCode: 'tvs-motors',
    roleCode: '',
    roleName: '',
    description: '',
    locationScope: 'ALL',
    permissions: ['VIEW_DASHBOARD', 'VIEW_INVENTORY'],
    colorCode: '#0284c7'
  };

  availablePermissions = [
    { key: 'VIEW_DASHBOARD', label: 'View Dashboard & KPIs' },
    { key: 'VIEW_INVENTORY', label: 'Inspect Floor Inventory' },
    { key: 'ALLOCATE_VEHICLE', label: 'Allocate Vehicle to VIP Client' },
    { key: 'PERFORM_PDI', label: 'Execute 120-Point PDI Checklist' },
    { key: 'APPROVE_PDI', label: 'Approve PDI & Sign-off Snags' },
    { key: 'DETAILING_CLEARANCE', label: 'Luxury Detailing & Staging Sign-off' },
    { key: 'APPROVE_FINANCE', label: 'Financial & Insurance Binding' },
    { key: 'HANDOVER_CEREMONY', label: 'VIP Handover Ceremony Authorization' },
    { key: 'MANAGE_STAFF', label: 'Provision / Suspend Staff' },
    { key: 'MANAGE_LOCATIONS', label: 'Create & Manage Dealership Locations' },
    { key: 'BATTERY_HEALTH_CERTIFY', label: 'EV High-Voltage Battery Certification' }
  ];

  constructor(
    private userService: UserService,
    private realmService: RealmService,
    private locationService: LocationService,
    private roleService: RoleService
  ) {}

  ngOnInit(): void {
    this.loadAllData();
  }

  loadAllData(): void {
    this.loading = true;

    this.realmService.getAll().subscribe({
      next: (data) => {
        this.realms = data;
      },
      error: (err) => console.error('Error fetching realms:', err)
    });

    this.locationService.getLocations().subscribe({
      next: (locs) => {
        this.locations = locs;
        this.applyLocationFilters();
      },
      error: (err) => console.error('Error fetching locations:', err)
    });

    this.roleService.getRoles().subscribe({
      next: (rList) => {
        this.roles = rList.map(r => {
          let pList: string[] = [];
          if (r.permissionsJson) {
            try {
              pList = JSON.parse(r.permissionsJson);
            } catch (e) {
              pList = ['VIEW_DASHBOARD'];
            }
          }
          return { ...r, permissionsList: pList };
        });
        this.applyRoleFilters();
      },
      error: (err) => console.error('Error fetching roles:', err)
    });

    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.applyUserFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        this.loading = false;
      }
    });
  }

  // Filter Staff
  applyUserFilters(): void {
    let result = [...this.users];

    if (this.selectedRealm !== 'all') {
      result = result.filter(u => u.realmCode === this.selectedRealm);
    }

    if (this.selectedLocation !== 'all') {
      result = result.filter(u => u.locationCode === this.selectedLocation || (u.showroomLocation && u.showroomLocation.includes(this.selectedLocation)));
    }

    if (this.selectedRole !== 'all') {
      result = result.filter(u => u.role === this.selectedRole);
    }

    if (this.selectedStatus !== 'all') {
      result = result.filter(u => u.status === this.selectedStatus);
    }

    if (this.searchTerm.trim() !== '') {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(u =>
        u.fullName.toLowerCase().includes(term) ||
        u.email.toLowerCase().includes(term) ||
        (u.showroomLocation && u.showroomLocation.toLowerCase().includes(term)) ||
        (u.locationCode && u.locationCode.toLowerCase().includes(term))
      );
    }

    this.filteredUsers = result;
  }

  // Filter Locations
  applyLocationFilters(): void {
    let result = [...this.locations];
    if (this.selectedRealm !== 'all') {
      result = result.filter(l => l.realmCode === this.selectedRealm);
    }
    this.filteredLocations = result;
  }

  // Filter Roles
  applyRoleFilters(): void {
    let result = [...this.roles];
    if (this.selectedRealm !== 'all') {
      result = result.filter(r => r.realmCode === this.selectedRealm || r.realmCode === 'GLOBAL');
    }
    this.filteredRoles = result;
  }

  onRealmFilterChange(): void {
    this.applyUserFilters();
    this.applyLocationFilters();
    this.applyRoleFilters();
  }

  getInitials(name: string): string {
    if (!name) return 'U';
    const parts = name.split(' ');
    if (parts.length >= 2) {
      return (parts[0][0] + parts[1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  }

  getRoleConfig(roleCode: string): ShowroomRole {
    const found = this.roles.find(r => r.roleCode === roleCode);
    if (found) return found;
    return {
      realmCode: 'GLOBAL',
      roleCode: roleCode,
      roleName: roleCode.replaceAll('_', ' '),
      colorCode: '#475569',
      locationScope: 'ALL'
    };
  }

  togglePermission(permKey: string): void {
    const idx = this.newRole.permissions.indexOf(permKey);
    if (idx > -1) {
      this.newRole.permissions.splice(idx, 1);
    } else {
      this.newRole.permissions.push(permKey);
    }
  }

  isPermissionSelected(permKey: string): boolean {
    return this.newRole.permissions.includes(permKey);
  }

  toggleUserStatus(user: ShowroomUser): void {
    if (!user.id) return;
    const newStatus = user.status === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
    this.userService.updateUserStatus(user.id, newStatus).subscribe({
      next: (updated) => {
        user.status = updated.status;
        this.applyUserFilters();
      },
      error: (err) => console.error('Error updating status:', err)
    });
  }

  // Modals Open/Close
  openUserModal(): void {
    if (this.locations.length > 0) {
      this.newUser.locationCode = this.locations[0].locationCode;
      this.newUser.showroomLocation = this.locations[0].locationName;
    }
    this.isUserModalOpen = true;
  }

  closeUserModal(): void {
    this.isUserModalOpen = false;
  }

  openLocationModal(): void {
    this.isLocationModalOpen = true;
  }

  closeLocationModal(): void {
    this.isLocationModalOpen = false;
  }

  openRoleModal(): void {
    this.isRoleModalOpen = true;
  }

  closeRoleModal(): void {
    this.isRoleModalOpen = false;
  }

  onUserLocationSelect(locCode: string): void {
    const loc = this.locations.find(l => l.locationCode === locCode);
    if (loc) {
      this.newUser.locationCode = loc.locationCode;
      this.newUser.showroomLocation = loc.locationName;
      this.newUser.realmCode = loc.realmCode;
    }
  }

  submitNewUser(): void {
    if (!this.newUser.fullName || !this.newUser.email) {
      alert('Please provide staff name and work email.');
      return;
    }

    this.userService.createUser(this.newUser).subscribe({
      next: (created) => {
        this.users.unshift(created);
        this.applyUserFilters();
        this.closeUserModal();
        this.newUser = {
          fullName: '',
          email: '',
          realmCode: 'tvs-motors',
          role: this.roles.length > 0 ? this.roles[0].roleCode : 'SALES_ADVISOR',
          locationCode: this.locations.length > 0 ? this.locations[0].locationCode : 'TVS-BLR-01',
          showroomLocation: this.locations.length > 0 ? this.locations[0].locationName : 'Bangalore Indiranagar',
          phoneNumber: '',
          status: 'ACTIVE'
        };
      },
      error: (err) => {
        alert('Failed to provision user: ' + (err.error?.message || err.message));
      }
    });
  }

  submitNewLocation(): void {
    if (!this.newLocation.locationCode || !this.newLocation.locationName) {
      alert('Location code and location name are required.');
      return;
    }

    this.locationService.createLocation(this.newLocation).subscribe({
      next: (created) => {
        this.locations.unshift(created);
        this.applyLocationFilters();
        this.closeLocationModal();
        this.newLocation = {
          realmCode: 'tvs-motors',
          locationCode: '',
          locationName: '',
          city: '',
          stateProvince: '',
          country: 'India',
          address: '',
          phoneNumber: '',
          managerEmail: '',
          status: 'ACTIVE'
        };
      },
      error: (err) => {
        alert('Failed to create location: ' + (err.error?.message || err.message));
      }
    });
  }

  submitNewRole(): void {
    if (!this.newRole.roleCode || !this.newRole.roleName) {
      alert('Role code and role name are required.');
      return;
    }

    this.roleService.createRole(this.newRole).subscribe({
      next: (created) => {
        const withList = { ...created, permissionsList: this.newRole.permissions };
        this.roles.push(withList);
        this.applyRoleFilters();
        this.closeRoleModal();
        this.newRole = {
          realmCode: 'tvs-motors',
          roleCode: '',
          roleName: '',
          description: '',
          locationScope: 'ALL',
          permissions: ['VIEW_DASHBOARD', 'VIEW_INVENTORY'],
          colorCode: '#0284c7'
        };
      },
      error: (err) => {
        alert('Failed to create role: ' + (err.error?.message || err.message));
      }
    });
  }

  get totalUsers(): number {
    return this.users.length;
  }

  get totalLocations(): number {
    return this.locations.length;
  }

  get totalRoles(): number {
    return this.roles.length;
  }
}
