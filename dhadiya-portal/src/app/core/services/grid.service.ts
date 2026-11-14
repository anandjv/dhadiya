import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DynamicSchemaTable, GridRecordEntry, PageResult } from '../models/datagrid.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GridService {
  private schemaUrl = environment.schemaApiUrl;
  private gridUrl = environment.gridApiUrl;

  constructor(private http: HttpClient) {}

  getTables(realmCode = 'dhadiya-hq'): Observable<DynamicSchemaTable[]> {
    return this.http.get<DynamicSchemaTable[]>(`${this.schemaUrl}?realmCode=${realmCode}`);
  }

  getTableSchema(tableName: string, realmCode = 'dhadiya-hq'): Observable<DynamicSchemaTable> {
    return this.http.get<DynamicSchemaTable>(`${this.schemaUrl}/${tableName}?realmCode=${realmCode}`);
  }

  createTable(table: { realmCode: string; tableName?: string; gridKey?: string; label?: string; gridName?: string; description?: string; processKey?: string; columnSchemaJson: string }): Observable<any> {
    return this.http.post<any>(this.schemaUrl, table);
  }

  getRecords(tableName: string, page = 0, size = 20, realmCode = 'dhadiya-hq'): Observable<PageResult<GridRecordEntry>> {
    return this.http.get<PageResult<GridRecordEntry>>(
      `${this.gridUrl}/records/${tableName}?realmCode=${realmCode}&page=${page}&size=${size}`
    );
  }

  insertRecord(record: { realmCode: string; tableName: string; processTrackingNumber?: string; dataJson: string }): Observable<GridRecordEntry> {
    return this.http.post<GridRecordEntry>(`${this.gridUrl}/records`, record);
  }
}
