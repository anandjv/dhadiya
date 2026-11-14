export interface ShowroomLocation {
  id?: number;
  realmCode: string;
  locationCode: string;
  locationName: string;
  city?: string;
  stateProvince?: string;
  country?: string;
  address?: string;
  phoneNumber?: string;
  managerEmail?: string;
  status: 'ACTIVE' | 'UNDER_RENOVATION' | 'OPENING_SOON' | string;
  createdAt?: string;
  updatedAt?: string;
}

export interface LocationCreateRequest {
  realmCode: string;
  locationCode: string;
  locationName: string;
  city?: string;
  stateProvince?: string;
  country?: string;
  address?: string;
  phoneNumber?: string;
  managerEmail?: string;
  status?: string;
}
