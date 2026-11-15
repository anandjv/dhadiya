import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShowroomUser, UserInviteRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/v1/users';

  constructor(private http: HttpClient) {}

  private getHeaders(realmCode?: string): HttpHeaders {
    let headers = new HttpHeaders();
    if (realmCode && realmCode !== 'all') {
      headers = headers.set('X-Tenant-ID', realmCode);
    }
    return headers;
  }

  getUsers(realmCode?: string): Observable<ShowroomUser[]> {
    const url = realmCode && realmCode !== 'all' ? `${this.apiUrl}?realmCode=${realmCode}` : this.apiUrl;
    return this.http.get<ShowroomUser[]>(url, { headers: this.getHeaders(realmCode) });
  }

  getUserById(id: number, realmCode?: string): Observable<ShowroomUser> {
    const url = realmCode ? `${this.apiUrl}/${id}?realmCode=${realmCode}` : `${this.apiUrl}/${id}`;
    return this.http.get<ShowroomUser>(url, { headers: this.getHeaders(realmCode) });
  }

  createUser(payload: UserInviteRequest): Observable<ShowroomUser> {
    const url = payload.realmCode ? `${this.apiUrl}?realmCode=${payload.realmCode}` : this.apiUrl;
    return this.http.post<ShowroomUser>(url, payload, { headers: this.getHeaders(payload.realmCode) });
  }

  updateUserStatus(id: number, status: string, realmCode?: string): Observable<ShowroomUser> {
    const url = realmCode ? `${this.apiUrl}/${id}/status?realmCode=${realmCode}` : `${this.apiUrl}/${id}/status`;
    return this.http.patch<ShowroomUser>(url, { status }, { headers: this.getHeaders(realmCode) });
  }

  deleteUser(id: number, realmCode?: string): Observable<void> {
    const url = realmCode ? `${this.apiUrl}/${id}?realmCode=${realmCode}` : `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url, { headers: this.getHeaders(realmCode) });
  }
}
