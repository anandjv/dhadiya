import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShowroomLocation, LocationCreateRequest } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private apiUrl = '/api/v1/locations';

  constructor(private http: HttpClient) {}

  private getHeaders(realmCode?: string): HttpHeaders {
    let headers = new HttpHeaders();
    if (realmCode && realmCode !== 'all') {
      headers = headers.set('X-Tenant-ID', realmCode);
    }
    return headers;
  }

  getLocations(realmCode?: string): Observable<ShowroomLocation[]> {
    const url = realmCode && realmCode !== 'all' ? `${this.apiUrl}?realmCode=${realmCode}` : this.apiUrl;
    return this.http.get<ShowroomLocation[]>(url, { headers: this.getHeaders(realmCode) });
  }

  getLocationByCode(locationCode: string, realmCode?: string): Observable<ShowroomLocation> {
    const url = realmCode ? `${this.apiUrl}/${locationCode}?realmCode=${realmCode}` : `${this.apiUrl}/${locationCode}`;
    return this.http.get<ShowroomLocation>(url, { headers: this.getHeaders(realmCode) });
  }

  createLocation(payload: LocationCreateRequest): Observable<ShowroomLocation> {
    const url = payload.realmCode ? `${this.apiUrl}?realmCode=${payload.realmCode}` : this.apiUrl;
    return this.http.post<ShowroomLocation>(url, payload, { headers: this.getHeaders(payload.realmCode) });
  }

  deleteLocation(id: number, realmCode?: string): Observable<void> {
    const url = realmCode ? `${this.apiUrl}/${id}?realmCode=${realmCode}` : `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url, { headers: this.getHeaders(realmCode) });
  }
}
