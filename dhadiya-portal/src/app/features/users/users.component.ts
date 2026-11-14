import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { RealmService } from '../../core/services/realm.service';
import { ShowroomUser, UserInviteRequest } from '../../core/models/user.model';
import { TenantRealm } from '../../core/models/realm.model';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.component.html'
})
export class UsersComponent implements OnInit {
  users: ShowroomUser[] = [];
  filteredUsers: ShowroomUser[] = [];
  realms: TenantRealm[] = [];
  loading = true;

  // Filters
  searchTerm = '';
  selectedRealm = 'all';
  selectedRole = 'all';
  selectedStatus = 'all';

  // Modal State
  isModalOpen = false;
  newUser: UserInviteRequest = {
    fullName: '',
    email: '',
    realmCode: 'dhadiya-showroom-americas',
    role: 'SALES_ADVISOR',
    showroomLocation: 'Beverly Hills Rodeo Drive Flagship',
    phoneNumber: '',
    status: 'ACTIVE'
  };

  roles = [
    { key: 'SHOWROOM_DIRECTOR', label: 'Showroom Director', color: '#9333ea', bg: '#f3e8ff' },
    { key: 'PDI_TECHNICIAN', label: 'Master PDI Technician', color: '#d97706', bg: '#fef3c7' },
    { key: 'DETAILING_SPECIALIST', label: 'Bespoke Detailing Lead', color: '#4f46e5', bg: '#e0e7ff' },
    { key: 'FINANCE_MANAGER', label: 'Client Finance Manager', color: '#059669', bg: '#d1fae5' },
    { key: 'SALES_ADVISOR', label: 'VIP Sales Advisor', color: '#0284c7', bg: '#e0f2fe' },
    { key: 'BRAND_AUDITOR', label: 'Brand Compliance Auditor', color: '#e11d48', bg: '#ffe4e6' }
  ];

  constructor(
    private userService: UserService,
    private realmService: RealmService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.realmService.getRealms().subscribe({
      next: (data) => {
        this.realms = data;
      },
      error: (err) => console.error('Error fetching realms:', err)
    });

    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching users:', err);
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    let result = [...this.users];

    if (this.selectedRealm !== 'all') {
      result = result.filter(u => u.realmCode === this.selectedRealm);
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
        (u.showroomLocation && u.showroomLocation.toLowerCase().includes(term))
      );
    }

    this.filteredUsers = result;
  }

  getInitials(name: string): string {
    if (!name) return 'U';
    const parts = name.split(' ');
    if (parts.length >= 2) {
      return (parts[0][0] + parts[1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  }

  getRoleConfig(roleKey: string) {
    return this.roles.find(r => r.key === roleKey) || {
      key: roleKey,
      label: roleKey,
      color: '#475569',
      bg: '#f1f5f9'
    };
  }

  toggleStatus(user: ShowroomUser): void {
    if (!user.id) return;
    const newStatus = user.status === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
    this.userService.updateUserStatus(user.id, newStatus).subscribe({
      next: (updated) => {
        user.status = updated.status;
        this.applyFilters();
      },
      error: (err) => console.error('Error updating status:', err)
    });
  }

  openInviteModal(): void {
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  submitNewUser(): void {
    if (!this.newUser.fullName || !this.newUser.email) {
      alert('Please provide staff name and work email.');
      return;
    }

    this.userService.createUser(this.newUser).subscribe({
      next: (created) => {
        this.users.unshift(created);
        this.applyFilters();
        this.closeModal();
        this.newUser = {
          fullName: '',
          email: '',
          realmCode: 'dhadiya-showroom-americas',
          role: 'SALES_ADVISOR',
          showroomLocation: 'Beverly Hills Rodeo Drive Flagship',
          phoneNumber: '',
          status: 'ACTIVE'
        };
      },
      error: (err) => {
        console.error('Error creating user:', err);
        alert('Failed to provision user: ' + (err.error?.message || err.message));
      }
    });
  }

  get totalUsers(): number {
    return this.users.length;
  }

  get directorsCount(): number {
    return this.users.filter(u => u.role === 'SHOWROOM_DIRECTOR').length;
  }

  get techCount(): number {
    return this.users.filter(u => u.role === 'PDI_TECHNICIAN' || u.role === 'DETAILING_SPECIALIST').length;
  }

  get salesCount(): number {
    return this.users.filter(u => u.role === 'SALES_ADVISOR' || u.role === 'FINANCE_MANAGER').length;
  }
}
