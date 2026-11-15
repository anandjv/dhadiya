// ========================================================
// eFormsPi CORE WORKFLOW ENGINE & DYNAMIC FORMS MODELS
// ========================================================

/**
 * Core Workflow Definition (eFormsPi WorkflowMaster)
 */
export interface WorkflowMaster {
  id?: number;
  realmCode: string;
  workflowCode: string;
  workflowName: string;
  workflowDesc?: string;
  category?: string;
  tableName?: string;
  formCode?: string;
  active?: boolean;
  version?: number;
  isCapa?: boolean;
  stages?: WorkflowStage[];
  triggers?: WorkflowTrigger[];
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Workflow Stage Definition (eFormsPi WorkflowStage)
 */
export interface WorkflowStage {
  id?: number;
  stageCode: string;
  stageName: string;
  stageOrder: number;
  stageType: 'START' | 'INTERMEDIATE' | 'APPROVAL' | 'REWORK' | 'END';
  slaHours?: number;
  allowedRole?: string;
  defaultAssignee?: string;
  isMandatory?: boolean;
  actionsAllowed?: string; // Comma-separated: "APPROVE, REJECT, REQUEST_CHANGES"
  stageColor?: string;
}

/**
 * State Transition Trigger & Condition (eFormsPi WorkflowTrigger)
 */
export interface WorkflowTrigger {
  id?: number;
  triggerName: string;
  sourceStageCode: string;
  targetStageCode: string;
  actionName: string;
  triggerType?: 'ACTION_CLICK' | 'AUTO_EVALUATE' | 'TIMEOUT';
  conditionExpression?: string;
  notificationEmail?: string;
}

/**
 * Active Workflow Case Tracker (eFormsPi WorkflowTrackMaster)
 */
export interface WorkflowTrackMaster {
  id?: number;
  caseNumber: string;
  realmCode: string;
  workflowCode: string;
  workflowName: string;
  currentStageCode: string;
  currentStageName: string;
  status: 'IN_PROGRESS' | 'COMPLETED' | 'REJECTED' | 'ON_HOLD';
  priority?: string;
  initiatedBy?: string;
  initiatedOn?: string;
  completedOn?: string;
  lastUpdatedOn?: string;
  tableName?: string;
  dataPayloadJson?: string;
}

/**
 * Workflow Lifecycle Execution History / Audit Trail (eFormsPi WorkflowTrackDet)
 */
export interface WorkflowTrackDet {
  id?: number;
  caseNumber: string;
  stageCode: string;
  stageName: string;
  actionTaken: string;
  actedBy?: string;
  remarks?: string;
  performedOn?: string;
  durationSeconds?: number;
  previousStageCode?: string;
  snapshotDataJson?: string;
}

/**
 * Actionable User Task Item (eFormsPi WorkflowTask)
 */
export interface WorkflowTaskItem {
  id?: number;
  caseNumber: string;
  stageCode: string;
  taskTitle: string;
  assignedTo?: string;
  roleName?: string;
  priority?: string;
  status: 'PENDING' | 'COMPLETED' | 'OVERDUE';
  createdAt?: string;
  slaDueDate?: string;
  completedAt?: string;
  actionTaken?: string;
  remarks?: string;
}

/**
 * Comprehensive Case Response with Trail & Available Actions
 */
export interface WorkflowCaseResponse {
  trackMaster: WorkflowTrackMaster;
  workflowStages: WorkflowStage[];
  availableActions: string[];
  historyTrail: WorkflowTrackDet[];
}

// ========================================================
// eFormsPi DYNAMIC FORMS ENGINE MODELS
// ========================================================

/**
 * Dynamic Form Master Definition (eFormsPi FormMaster)
 */
export interface FormMaster {
  id?: number;
  realmCode: string;
  formCode: string;
  formName: string;
  formDesc?: string;
  tableName?: string;
  category?: string;
  status: 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';
  version?: number;
  sections?: FormSection[];
  rules?: FormRule[];
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Dynamic Form Section (eFormsPi FormDesign / FormTabGroup)
 */
export interface FormSection {
  id?: number;
  sectionKey: string;
  title: string;
  sectionOrder: number;
  columnLayout: number; // 1, 2, 3, or 4 columns
  isRepeatable?: boolean;
  components?: FormComponent[];
}

/**
 * Dynamic Form Field Component (eFormsPi FormComponent)
 */
export interface FormComponent {
  id?: number;
  fieldKey: string;
  fieldLabel: string;
  componentType: 'TEXT' | 'TEXTAREA' | 'NUMBER' | 'SELECT' | 'RADIO' | 'CHECKBOX' | 'DATE' | 'FILE' | 'SIGNATURE' | 'FORMULA';
  placeholder?: string;
  defaultValue?: string;
  isRequired?: boolean;
  isReadOnly?: boolean;
  optionsJson?: string; // Serialized string array for dropdowns / options
  validationRegex?: string;
  fieldOrder?: number;
}

/**
 * Dynamic Form Conditional Rule (eFormsPi FormRule)
 */
export interface FormRule {
  id?: number;
  formCode?: string;
  ruleName: string;
  triggerField: string;
  ruleType: 'SHOW_HIDE' | 'ENABLE_DISABLE' | 'SET_REQUIRED' | 'CALCULATE';
  conditionOperator: 'EQUALS' | 'NOT_EQUALS' | 'GREATER_THAN' | 'CONTAINS';
  conditionValue: string;
  targetFieldsJson: string; // e.g. '["fiveWhyNarrative"]'
  actionValue?: string;
}

// ========================================================
// LEGACY COMPATIBILITY INTERFACES
// ========================================================

export interface ProcessDefinition {
  id?: number;
  realmCode: string;
  processKey: string;
  name: string;
  description?: string;
  version?: number;
  stagesJson?: string;
  defaultFormKey?: string;
  status: string;
}

export interface ProcessInstance {
  id?: number;
  trackingNumber: string;
  realmCode: string;
  processKey: string;
  currentStage: string;
  status: 'IN_PROGRESS' | 'COMPLETED' | 'REJECTED' | 'TERMINATED';
  initiatedBy?: string;
  payloadJson?: string;
  startedAt?: string;
  updatedAt?: string;
  completedAt?: string;
}

export interface ProcessTask {
  id: number;
  instanceId?: number;
  trackingNumber: string;
  stageName: string;
  title: string;
  assignee?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'COMPLETED';
  remarks?: string;
  createdAt: string;
  completedAt?: string;
}

export interface DynamicFormTemplate {
  id?: number;
  realmCode: string;
  templateKey: string;
  title: string;
  version?: number;
  schemaJson: string;
  status: string;
}
