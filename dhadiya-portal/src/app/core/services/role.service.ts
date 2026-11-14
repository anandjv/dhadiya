import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShowroomRole, RoleCreateRequest } from '../models/role.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiUrl = '/api/v1/roles';

  constructor(private http: HttpClient) {}

  getRoles(realmCode?: string): Observable<ShowroomRole[]> {
    const url = realmCode && realmCode !== 'all' ? `${this.apiUrl}?realmCode=${realmCode}` : this.apiUrl;
    return this.http.get<ShowroomRole[]>(url);
  }

  getRoleByCode(roleCode: string): Observable<ShowroomRole> {
    return this.http.get<ShowroomRole>(`${this.apiUrl}/${roleCode}`);
  }

  createRole(payload: RoleCreateRequest): Observable<ShowroomRole> {
    return this.http.post<ShowroomRole>(this.apiUrl, payload);
  }

  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
