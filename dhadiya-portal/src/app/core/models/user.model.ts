export interface ShowroomUser {
  id?: number;
  realmCode: string;
  email: string;
  fullName: string;
  role: 'SHOWROOM_DIRECTOR' | 'PDI_TECHNICIAN' | 'DETAILING_SPECIALIST' | 'FINANCE_MANAGER' | 'SALES_ADVISOR' | 'BRAND_AUDITOR' | string;
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
  showroomLocation: string;
  phoneNumber?: string;
  status?: string;
}
