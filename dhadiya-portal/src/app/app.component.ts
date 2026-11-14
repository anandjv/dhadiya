import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RealmService } from './core/services/realm.service';
import { LocationService } from './core/services/location.service';
import { TenantRealm } from './core/models/realm.model';
import { ShowroomLocation } from './core/models/location.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'dhadiya-portal';
  realms: TenantRealm[] = [];
  locations: ShowroomLocation[] = [];
  filteredLocations: ShowroomLocation[] = [];

  selectedRealmCode = 'tvs-motors';
  selectedLocationCode = 'all';
  activeLocationName = 'All Showroom Locations';

  constructor(
    private realmService: RealmService,
    private locationService: LocationService
  ) {}

  ngOnInit(): void {
    this.realmService.getAll().subscribe({
      next: (data) => {
        this.realms = data;
        this.loadLocations();
      },
      error: (err) => console.error('Failed to load realms:', err)
    });
  }

  loadLocations(): void {
    this.locationService.getLocations().subscribe({
      next: (data) => {
        this.locations = data;
        this.filterLocations();
      },
      error: (err) => console.error('Failed to load locations:', err)
    });
  }

  filterLocations(): void {
    if (this.selectedRealmCode === 'all') {
      this.filteredLocations = this.locations;
    } else {
      this.filteredLocations = this.locations.filter(l => l.realmCode === this.selectedRealmCode);
    }
  }

  onRealmChange(newCode: string): void {
    this.selectedRealmCode = newCode;
    this.selectedLocationCode = 'all';
    this.activeLocationName = 'All Showroom Locations';
    this.filterLocations();
  }

  onLocationChange(locCode: string): void {
    this.selectedLocationCode = locCode;
    if (locCode === 'all') {
      this.activeLocationName = 'All Showroom Locations';
    } else {
      const loc = this.locations.find(l => l.locationCode === locCode);
      if (loc) {
        this.activeLocationName = loc.locationName;
      }
    }
  }
}
