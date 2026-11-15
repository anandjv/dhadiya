import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShowroomRole, RoleCreateRequest } from '../models/role.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiUrl = '/api/v1/roles';

  constructor(private http: HttpClient) {}

  private getHeaders(realmCode?: string): HttpHeaders {
    let headers = new HttpHeaders();
    if (realmCode && realmCode !== 'all') {
      headers = headers.set('X-Tenant-ID', realmCode);
    }
    return headers;
  }

  getRoles(realmCode?: string): Observable<ShowroomRole[]> {
    const url = realmCode && realmCode !== 'all' ? `${this.apiUrl}?realmCode=${realmCode}` : this.apiUrl;
    return this.http.get<ShowroomRole[]>(url, { headers: this.getHeaders(realmCode) });
  }

  getRoleByCode(roleCode: string, realmCode?: string): Observable<ShowroomRole> {
    const url = realmCode ? `${this.apiUrl}/${roleCode}?realmCode=${realmCode}` : `${this.apiUrl}/${roleCode}`;
    return this.http.get<ShowroomRole>(url, { headers: this.getHeaders(realmCode) });
  }

  createRole(payload: RoleCreateRequest): Observable<ShowroomRole> {
    const url = payload.realmCode ? `${this.apiUrl}?realmCode=${payload.realmCode}` : this.apiUrl;
    return this.http.post<ShowroomRole>(url, payload, { headers: this.getHeaders(payload.realmCode) });
  }

  deleteRole(id: number, realmCode?: string): Observable<void> {
    const url = realmCode ? `${this.apiUrl}/${id}?realmCode=${realmCode}` : `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url, { headers: this.getHeaders(realmCode) });
  }
}
