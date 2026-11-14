export interface ShowroomRole {
  id?: number;
  realmCode: string;
  roleCode: string;
  roleName: string;
  description?: string;
  locationScope: string; // "ALL" or specific locationCode
  permissionsJson?: string;
  permissionsList?: string[];
  colorCode?: string;
  isSystemRole?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface RoleCreateRequest {
  realmCode: string;
  roleCode: string;
  roleName: string;
  description?: string;
  locationScope?: string;
  permissions: string[];
  colorCode?: string;
}
