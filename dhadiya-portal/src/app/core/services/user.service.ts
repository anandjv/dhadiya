import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShowroomUser, UserInviteRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/v1/users';

  constructor(private http: HttpClient) {}

  getUsers(realmCode?: string): Observable<ShowroomUser[]> {
    const url = realmCode && realmCode !== 'all' ? `${this.apiUrl}?realmCode=${realmCode}` : this.apiUrl;
    return this.http.get<ShowroomUser[]>(url);
  }

  getUserById(id: number): Observable<ShowroomUser> {
    return this.http.get<ShowroomUser>(`${this.apiUrl}/${id}`);
  }

  createUser(payload: UserInviteRequest): Observable<ShowroomUser> {
    return this.http.post<ShowroomUser>(this.apiUrl, payload);
  }

  updateUserStatus(id: number, status: string): Observable<ShowroomUser> {
    return this.http.patch<ShowroomUser>(`${this.apiUrl}/${id}/status`, { status });
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
