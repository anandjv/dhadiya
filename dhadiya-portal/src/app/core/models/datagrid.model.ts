export interface DynamicSchemaTable {
  id?: number;
  realmCode: string;
  tableName: string;
  label: string;
  description?: string;
  columnSchemaJson: string;
}

export interface GridRecordEntry {
  id?: number;
  realmCode: string;
  tableName: string;
  processTrackingNumber?: string;
  dataJson: string;
  createdAt?: string;
}

export interface PageResult<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
