import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RealmService } from './core/services/realm.service';
import { TenantRealm } from './core/models/realm.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'dhadiya-portal';
  realms: TenantRealm[] = [];
  selectedRealmCode = 'dhadiya-showroom-global';
  activeLocationName = 'Global Showrooms Network';

  constructor(private realmService: RealmService) {}

  ngOnInit(): void {
    this.realmService.getAll().subscribe({
      next: (data) => {
        this.realms = data;
        const current = this.realms.find(r => r.realmCode === this.selectedRealmCode);
        if (current) {
          this.activeLocationName = current.organizationName;
        }
      },
      error: (err) => console.error('Failed to load realms:', err)
    });
  }

  onRealmChange(newCode: string): void {
    this.selectedRealmCode = newCode;
    const current = this.realms.find(r => r.realmCode === newCode);
    if (current) {
      this.activeLocationName = current.organizationName;
    }
  }
}
