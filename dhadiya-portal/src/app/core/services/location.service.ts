import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShowroomLocation, LocationCreateRequest } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private apiUrl = '/api/v1/locations';

  constructor(private http: HttpClient) {}

  getLocations(realmCode?: string): Observable<ShowroomLocation[]> {
    const url = realmCode && realmCode !== 'all' ? `${this.apiUrl}?realmCode=${realmCode}` : this.apiUrl;
    return this.http.get<ShowroomLocation[]>(url);
  }

  getLocationByCode(locationCode: string): Observable<ShowroomLocation> {
    return this.http.get<ShowroomLocation>(`${this.apiUrl}/${locationCode}`);
  }

  createLocation(payload: LocationCreateRequest): Observable<ShowroomLocation> {
    return this.http.post<ShowroomLocation>(this.apiUrl, payload);
  }

  deleteLocation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
