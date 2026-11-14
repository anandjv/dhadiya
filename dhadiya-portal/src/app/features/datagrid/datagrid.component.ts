import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GridService } from '../../core/services/grid.service';
import { DynamicSchemaTable, GridRecordEntry } from '../../core/models/datagrid.model';

@Component({
  selector: 'app-datagrid',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './datagrid.component.html'
})
export class DataGridComponent implements OnInit {
  tables: DynamicSchemaTable[] = [];
  selectedTable: string = 'dhadiya_showroom_inventory';
  records: any[] = [];
  columns: string[] = [];

  constructor(private gridService: GridService) {}

  ngOnInit(): void {
    this.loadTables();
  }

  loadTables(): void {
    this.gridService.getTables().subscribe({
      next: (data) => {
        this.tables = data.length > 0 ? data : this.getDemoTables();
        this.loadRecords();
      },
      error: () => {
        this.tables = this.getDemoTables();
        this.loadRecords();
      }
    });
  }

  loadRecords(): void {
    this.gridService.getRecords(this.selectedTable).subscribe({
      next: (page) => {
        if (page && page.content && page.content.length > 0) {
          this.parseRecords(page.content);
        } else {
          this.loadDemoRecords();
        }
      },
      error: () => this.loadDemoRecords()
    });
  }

  onTableChange(): void {
    this.loadRecords();
  }

  private parseRecords(rawList: GridRecordEntry[]): void {
    this.records = rawList.map(r => {
      try {
        return { id: r.id, caseNumber: r.processTrackingNumber, ...JSON.parse(r.dataJson) };
      } catch {
        return { id: r.id, raw: r.dataJson };
      }
    });
    if (this.records.length > 0) {
      this.columns = Object.keys(this.records[0]);
    }
  }

  private loadDemoRecords(): void {
    if (this.selectedTable === 'dhadiya_showroom_inventory') {
      this.records = [
        { id: 1, caseNumber: 'DHADIYA-2026-001', vinCode: 'WBA33AY08PFS99120', modelName: 'Porsche 911 GT3 RS', trimLevel: 'Weissach Package', exteriorColor: 'Guards Red', showroomLocation: 'Beverly Hills Rodeo Drive Flagship', vehicleStatus: 'PDI_IN_PROGRESS', msrpPrice: '$318,500', allocationDate: '2026-11-10', assignedAdvisor: 'Marco Rossi' },
        { id: 2, caseNumber: 'DHADIYA-2026-002', vinCode: 'ZHWUA1ZF5PLA09812', modelName: 'Ferrari SF90 Stradale', trimLevel: 'Assetto Fiorano', exteriorColor: 'Rosso Corsa', showroomLocation: 'London Mayfair Gallery', vehicleStatus: 'ON_FLOOR_DISPLAY', msrpPrice: '£425,000', allocationDate: '2026-11-08', assignedAdvisor: 'Arthur Pendelton' },
        { id: 3, caseNumber: 'DHADIYA-2026-003', vinCode: 'JTHHP5C13D5001290', modelName: 'Lexus LFA Nürburgring', trimLevel: 'Carbon Edition', exteriorColor: 'Whitest White', showroomLocation: 'Tokyo Ginza Flagship', vehicleStatus: 'RESERVED', msrpPrice: '¥98,000,000', allocationDate: '2026-11-05', assignedAdvisor: 'Kenji Takahashi' },
        { id: 4, caseNumber: 'DHADIYA-2026-004', vinCode: 'SCBFN9849LC109482', modelName: 'Rolls-Royce Spectre', trimLevel: 'Bespoke Luxury EV', exteriorColor: 'Black Diamond & Mandarin', showroomLocation: 'Dubai Downtown Gallery', vehicleStatus: 'ON_FLOOR_DISPLAY', msrpPrice: 'AED 1,950,000', allocationDate: '2026-11-12', assignedAdvisor: 'Tariq Al-Fahim' }
      ];
    } else if (this.selectedTable === 'dhadiya_showroom_audits') {
      this.records = [
        { id: 1, caseNumber: 'DHADIYA-2026-005', auditRef: 'AUD-SHW-8801', showroomCode: 'USA-BH-01', cityLocation: 'Beverly Hills, CA', auditDate: '2026-11-05', leadAuditor: 'Elena Rostova', complianceScore: '99.4%', criticalFindings: 0, certificationStatus: 'CERTIFIED' },
        { id: 2, caseNumber: 'DHADIYA-2026-006', auditRef: 'AUD-SHW-8802', showroomCode: 'GBR-LON-02', cityLocation: 'London Mayfair', auditDate: '2026-11-08', leadAuditor: 'Arthur Pendelton', complianceScore: '98.2%', criticalFindings: 0, certificationStatus: 'CERTIFIED' }
      ];
    } else {
      this.records = [
        { id: 1, orderNumber: 'ORD-VIP-901', clientName: 'Julian Sterling', vipTier: 'DIAMOND_ELITE', showroomLocation: 'Beverly Hills Rodeo Drive', modelSelected: 'Porsche 911 GT3 RS', orderValue: '$318,500', deliveryStatus: 'READY_FOR_DELIVERY', salesLead: 'Marco Rossi' },
        { id: 2, orderNumber: 'ORD-VIP-902', clientName: 'Lady Charlotte Windsor', vipTier: 'BLACK_CARD', showroomLocation: 'London Mayfair', modelSelected: 'Ferrari SF90 Stradale', orderValue: '£425,000', deliveryStatus: 'ALLOCATED', salesLead: 'Arthur Pendelton' }
      ];
    }
    this.columns = Object.keys(this.records[0]);
  }

  private getDemoTables(): DynamicSchemaTable[] {
    return [
      { id: 1, realmCode: 'dhadiya-showroom-global', tableName: 'dhadiya_showroom_inventory', label: 'Global Showroom Floor & Vehicle Inventory', description: 'Master inventory tracking VINs, models, trims, MSRP, and showroom floor allocation status.', columnSchemaJson: '[]' },
      { id: 2, realmCode: 'dhadiya-showroom-global', tableName: 'dhadiya_showroom_audits', label: 'Global Flagship Brand Standards & Audit Registry', description: 'Showroom evaluation scorecard covering visual merchandising, amenities, and compliance scoring.', columnSchemaJson: '[]' },
      { id: 3, realmCode: 'dhadiya-showroom-global', tableName: 'dhadiya_customer_orders', label: 'VIP Bespoke Orders & Client Allocations', description: 'Global customer purchase agreements, VIP tier status, and vehicle handover milestones.', columnSchemaJson: '[]' }
    ];
  }
}
