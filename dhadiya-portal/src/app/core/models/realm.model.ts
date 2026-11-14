export interface TenantRealm {
  id?: number;
  realmCode: string;
  organizationName: string;
  customDomain?: string;
  status: 'ACTIVE' | 'SUSPENDED' | 'PROVISIONING';
  settingsJson?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface TenantRealmRequest {
  realmCode: string;
  organizationName: string;
  customDomain?: string;
  settingsJson?: string;
}
