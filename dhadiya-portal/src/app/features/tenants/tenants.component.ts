import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RealmService } from '../../core/services/realm.service';
import { TenantRealm, TenantRealmRequest } from '../../core/models/realm.model';

@Component({
  selector: 'app-tenants',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tenants.component.html'
})
export class TenantsComponent implements OnInit {
  realms: TenantRealm[] = [];
  loading = false;
  showCreateModal = false;

  newRealm: TenantRealmRequest = {
    realmCode: '',
    organizationName: '',
    customDomain: '',
    settingsJson: '{"currency":"USD","brandStandard":"ISO-9001-LUXURY","pdiProtocol":"120-POINT"}'
  };

  constructor(private realmService: RealmService) {}

  ngOnInit(): void {
    this.loadRealms();
  }

  loadRealms(): void {
    this.loading = true;
    this.realmService.getAll().subscribe({
      next: (data) => {
        this.realms = data.length > 0 ? data : this.getDemoRealms();
        this.loading = false;
      },
      error: () => {
        this.realms = this.getDemoRealms();
        this.loading = false;
      }
    });
  }

  createRealm(): void {
    if (!this.newRealm.realmCode || !this.newRealm.organizationName) return;
    this.realmService.create(this.newRealm).subscribe({
      next: (created) => {
        this.realms.push(created);
        this.showCreateModal = false;
        this.resetForm();
      },
      error: () => {
        this.realms.push({
          ...this.newRealm,
          id: Date.now(),
          status: 'ACTIVE',
          createdAt: new Date().toISOString()
        });
        this.showCreateModal = false;
        this.resetForm();
      }
    });
  }

  toggleStatus(realm: TenantRealm): void {
    const nextStatus = realm.status === 'ACTIVE' ? 'SUSPENDED' : 'ACTIVE';
    this.realmService.updateStatus(realm.realmCode, nextStatus).subscribe({
      next: (updated) => realm.status = updated.status,
      error: () => realm.status = nextStatus
    });
  }

  private resetForm(): void {
    this.newRealm = {
      realmCode: '',
      organizationName: '',
      customDomain: '',
      settingsJson: '{"currency":"USD","brandStandard":"ISO-9001-LUXURY","pdiProtocol":"120-POINT"}'
    };
  }

  private getDemoRealms(): TenantRealm[] {
    return [
      {
        id: 1,
        realmCode: 'dhadiya-showroom-global',
        organizationName: 'Dhadiya Global Showrooms Network',
        customDomain: 'showrooms.dhadiya.com',
        status: 'ACTIVE',
        createdAt: '2026-01-15',
        settingsJson: '{"plan":"ENTERPRISE","theme":"luxury-dark","features":["workflows","datagrid","pdi","vip_portal"]}'
      },
      {
        id: 2,
        realmCode: 'dhadiya-showroom-emea',
        organizationName: 'Dhadiya Luxury Showrooms EMEA (London, Paris, Milan, Dubai)',
        customDomain: 'emea.showrooms.dhadiya.com',
        status: 'ACTIVE',
        createdAt: '2026-02-10',
        settingsJson: '{"currency":"EUR/GBP/AED","flagships":["London Mayfair","Paris Champs-Élysées","Milan Montenapoleone","Dubai Downtown"]}'
      },
      {
        id: 3,
        realmCode: 'dhadiya-showroom-americas',
        organizationName: 'Dhadiya Flagship Showrooms Americas (New York, Beverly Hills, Miami)',
        customDomain: 'americas.showrooms.dhadiya.com',
        status: 'ACTIVE',
        createdAt: '2026-03-01',
        settingsJson: '{"currency":"USD","flagships":["New York 5th Ave","Beverly Hills Rodeo Dr","Miami Brickell","Toronto Yorkville"]}'
      },
      {
        id: 4,
        realmCode: 'dhadiya-showroom-apac',
        organizationName: 'Dhadiya Prestige Showrooms APAC (Tokyo, Singapore, Shanghai)',
        customDomain: 'apac.showrooms.dhadiya.com',
        status: 'ACTIVE',
        createdAt: '2026-04-12',
        settingsJson: '{"currency":"JPY/SGD","flagships":["Tokyo Ginza","Singapore Marina Bay","Shanghai Bund","Sydney CBD"]}'
      }
    ];
  }
}
