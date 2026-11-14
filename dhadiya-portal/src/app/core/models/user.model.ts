export interface ShowroomUser {
  id?: number;
  realmCode: string;
  email: string;
  fullName: string;
  role: string;
  locationCode?: string;
  showroomLocation: string;
  phoneNumber?: string;
  status: 'ACTIVE' | 'INVITED' | 'SUSPENDED' | string;
  createdAt?: string;
  updatedAt?: string;
  lastLoginAt?: string;
}

export interface UserInviteRequest {
  realmCode: string;
  email: string;
  fullName: string;
  role: string;
  locationCode?: string;
  showroomLocation: string;
  phoneNumber?: string;
  status?: string;
}
