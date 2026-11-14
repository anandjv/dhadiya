import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TenantRealm, TenantRealmRequest } from '../models/realm.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RealmService {
  private baseUrl = environment.realmApiUrl;

  constructor(private http: HttpClient) {}

  getAll(): Observable<TenantRealm[]> {
    return this.http.get<TenantRealm[]>(this.baseUrl);
  }

  getByCode(code: string): Observable<TenantRealm> {
    return this.http.get<TenantRealm>(`${this.baseUrl}/${code}`);
  }

  create(request: TenantRealmRequest): Observable<TenantRealm> {
    return this.http.post<TenantRealm>(this.baseUrl, request);
  }

  updateStatus(code: string, status: string): Observable<TenantRealm> {
    return this.http.patch<TenantRealm>(`${this.baseUrl}/${code}/status?status=${status}`, {});
  }
}
