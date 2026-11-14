package com.dhadiya.flow.config;

import com.dhadiya.flow.entity.*;
import com.dhadiya.flow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProcessDefinitionRepository definitionRepository;
    private final DynamicFormTemplateRepository formRepository;
    private final ProcessInstanceRepository instanceRepository;
    private final ProcessTaskRepository taskRepository;
    private final DynamicGridDefRepository gridDefRepository;
    private final DynamicGridRecordRepository gridRecordRepository;
    private final DynamicGridViewRepository gridViewRepository;

    // eFormsPi Core Repositories
    private final WorkflowMasterRepository workflowMasterRepository;
    private final WorkflowTrackMasterRepository trackMasterRepository;
    private final WorkflowTrackDetRepository trackDetRepository;
    private final WorkflowTaskItemRepository taskItemRepository;
    private final FormMasterRepository formMasterRepository;

    @Override
    public void run(String... args) {
        initLegacyCompatibility();
        initCoreWorkflowEngine();
        initCoreDynamicForms();
    }

    private void initCoreWorkflowEngine() {
        if (workflowMasterRepository.count() == 0) {
            log.info("Initializing Global Showroom Management eFormsPi Workflow Definitions...");

            // 1. Vehicle PDI & VIP Handover Workflow
            WorkflowMaster handoverWf = WorkflowMaster.builder()
                    .realmCode("dhadiya-showroom-global")
                    .workflowCode("WF_SHOWROOM_DELIVERY_HANDOVER")
                    .workflowName("Global Showroom Vehicle PDI & VIP Handover Workflow")
                    .workflowDesc("5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.")
                    .category("Showroom Operations")
                    .tableName("dhadiya_showroom_inventory")
                    .formCode("FORM_SHOWROOM_PDI")
                    .version(2)
                    .active(true)
                    .isCapa(true)
                    .stages(new ArrayList<>(List.of(
                            WorkflowStage.builder()
                                    .stageCode("INTAKE_INSPECTION")
                                    .stageName("Showroom Floor Intake & Arrival Inspection")
                                    .stageOrder(1)
                                    .stageType("START")
                                    .slaHours(12)
                                    .allowedRole("SHOWROOM_LOGISTICS")
                                    .defaultAssignee("logistics.lead@americas.dhadiya.com")
                                    .actionsAllowed("ACCEPT_INTAKE, REJECT_TRANSPORT")
                                    .stageColor("#0284c7")
                                    .build(),
                            WorkflowStage.builder()
                                    .stageCode("TECHNICAL_PDI")
                                    .stageName("Master Technician PDI & Diagnostics")
                                    .stageOrder(2)
                                    .stageType("APPROVAL")
                                    .slaHours(24)
                                    .allowedRole("PDI_TECHNICIAN")
                                    .defaultAssignee("chief.tech@americas.dhadiya.com")
                                    .actionsAllowed("APPROVE_PDI, REWORK_SNAG")
                                    .stageColor("#f59e0b")
                                    .build(),
                            WorkflowStage.builder()
                                    .stageCode("LUXURY_DETAILING")
                                    .stageName("Bespoke Detailing & Floor Display Staging")
                                    .stageOrder(3)
                                    .stageType("INTERMEDIATE")
                                    .slaHours(18)
                                    .allowedRole("DETAILING_SPECIALIST")
                                    .defaultAssignee("detailing.lead@americas.dhadiya.com")
                                    .actionsAllowed("CERTIFY_STAGING, TOUCHUP_REQUIRED")
                                    .stageColor("#8b5cf6")
                                    .build(),
                            WorkflowStage.builder()
                                    .stageCode("VIP_FINANCIAL_CLEARANCE")
                                    .stageName("Client Financial Clearance & Insurance")
                                    .stageOrder(4)
                                    .stageType("APPROVAL")
                                    .slaHours(24)
                                    .allowedRole("FINANCE_MANAGER")
                                    .defaultAssignee("finance.manager@americas.dhadiya.com")
                                    .actionsAllowed("APPROVE_CLEARANCE, HOLD_FINANCE")
                                    .stageColor("#10b981")
                                    .build(),
                            WorkflowStage.builder()
                                    .stageCode("FINAL_HANDOVER_CEREMONY")
                                    .stageName("VIP Handover Ceremony & Digital Sign-off")
                                    .stageOrder(5)
                                    .stageType("END")
                                    .slaHours(12)
                                    .allowedRole("SHOWROOM_DIRECTOR")
                                    .defaultAssignee("general.manager@americas.dhadiya.com")
                                    .actionsAllowed("DELIVER_AND_SIGN, RESCHEDULE")
                                    .stageColor("#059669")
                                    .build()
                    )))
                    .triggers(new ArrayList<>(List.of(
                            WorkflowTrigger.builder().triggerName("AcceptToIntake").sourceStageCode("INTAKE_INSPECTION").targetStageCode("TECHNICAL_PDI").actionName("ACCEPT_INTAKE").build(),
                            WorkflowTrigger.builder().triggerName("PdiToDetailing").sourceStageCode("TECHNICAL_PDI").targetStageCode("LUXURY_DETAILING").actionName("APPROVE_PDI").build(),
                            WorkflowTrigger.builder().triggerName("PdiRework").sourceStageCode("TECHNICAL_PDI").targetStageCode("INTAKE_INSPECTION").actionName("REWORK_SNAG").build(),
                            WorkflowTrigger.builder().triggerName("DetailingToClearance").sourceStageCode("LUXURY_DETAILING").targetStageCode("VIP_FINANCIAL_CLEARANCE").actionName("CERTIFY_STAGING").build(),
                            WorkflowTrigger.builder().triggerName("ClearanceToHandover").sourceStageCode("VIP_FINANCIAL_CLEARANCE").targetStageCode("FINAL_HANDOVER_CEREMONY").actionName("APPROVE_CLEARANCE").build(),
                            WorkflowTrigger.builder().triggerName("FinalHandoverSign").sourceStageCode("FINAL_HANDOVER_CEREMONY").targetStageCode("COMPLETED").actionName("DELIVER_AND_SIGN").build(),
                            WorkflowTrigger.builder().triggerName("RejectTransport").sourceStageCode("INTAKE_INSPECTION").targetStageCode("REJECTED").actionName("REJECT_TRANSPORT").build()
                    )))
                    .build();
            workflowMasterRepository.save(handoverWf);

            // 2. Global Flagship Showroom Standards & Brand Compliance Workflow
            WorkflowMaster auditWf = WorkflowMaster.builder()
                    .realmCode("dhadiya-showroom-global")
                    .workflowCode("WF_SHOWROOM_AUDIT")
                    .workflowName("Global Flagship Showroom Standards & Brand Compliance Audit")
                    .workflowDesc("Quarterly inspection of showroom floor presentation, visual merchandising, VIP customer amenities, and staff standards.")
                    .category("Retail Excellence")
                    .tableName("dhadiya_showroom_audits")
                    .formCode("FORM_SHOWROOM_STANDARDS")
                    .version(1)
                    .active(true)
                    .isCapa(true)
                    .stages(new ArrayList<>(List.of(
                            WorkflowStage.builder().stageCode("SCHEDULED").stageName("Audit Scope & Dispatch").stageOrder(1).stageType("START").slaHours(24).allowedRole("AUDIT_DISPATCHER").defaultAssignee("compliance@showrooms.dhadiya.com").actionsAllowed("DISPATCH, RESCHEDULE").stageColor("#0284c7").build(),
                            WorkflowStage.builder().stageCode("ON_SITE_EVALUATION").stageName("On-Site Showroom Floor Inspection").stageOrder(2).stageType("INTERMEDIATE").slaHours(48).allowedRole("BRAND_AUDITOR").defaultAssignee("lead.auditor@showrooms.dhadiya.com").actionsAllowed("SUBMIT_EVALUATION, REINSPECT").stageColor("#f59e0b").build(),
                            WorkflowStage.builder().stageCode("FINDINGS_REVIEW").stageName("Showroom General Manager Review").stageOrder(3).stageType("APPROVAL").slaHours(36).allowedRole("GENERAL_MANAGER").defaultAssignee("gm.beverlyhills@americas.dhadiya.com").actionsAllowed("APPROVE_CAPA, CHALLENGE").stageColor("#8b5cf6").build(),
                            WorkflowStage.builder().stageCode("CERTIFICATION_SIGN_OFF").stageName("VP Global Retail Excellence Certification").stageOrder(4).stageType("END").slaHours(24).allowedRole("VP_RETAIL").defaultAssignee("vp.retail@showrooms.dhadiya.com").actionsAllowed("CERTIFY_SHOWROOM, PROVISIONAL").stageColor("#10b981").build()
                    )))
                    .triggers(new ArrayList<>(List.of(
                            WorkflowTrigger.builder().triggerName("DispatchAudit").sourceStageCode("SCHEDULED").targetStageCode("ON_SITE_EVALUATION").actionName("DISPATCH").build(),
                            WorkflowTrigger.builder().triggerName("SubmitEval").sourceStageCode("ON_SITE_EVALUATION").targetStageCode("FINDINGS_REVIEW").actionName("SUBMIT_EVALUATION").build(),
                            WorkflowTrigger.builder().triggerName("ApproveCapa").sourceStageCode("FINDINGS_REVIEW").targetStageCode("CERTIFICATION_SIGN_OFF").actionName("APPROVE_CAPA").build(),
                            WorkflowTrigger.builder().triggerName("CertifyFinal").sourceStageCode("CERTIFICATION_SIGN_OFF").targetStageCode("COMPLETED").actionName("CERTIFY_SHOWROOM").build()
                    )))
                    .build();
            workflowMasterRepository.save(auditWf);

            // 3. Pre-load Live Running Showroom Cases
            WorkflowTrackMaster case1 = trackMasterRepository.save(WorkflowTrackMaster.builder()
                    .caseNumber("DHADIYA-2026-001")
                    .realmCode("dhadiya-showroom-americas")
                    .workflowCode("WF_SHOWROOM_DELIVERY_HANDOVER")
                    .workflowName("Global Showroom Vehicle PDI & VIP Handover Workflow")
                    .currentStageCode("TECHNICAL_PDI")
                    .currentStageName("Master Technician PDI & Diagnostics")
                    .status("IN_PROGRESS")
                    .priority("HIGH")
                    .initiatedBy("logistics.lead@americas.dhadiya.com")
                    .initiatedOn(LocalDateTime.now().minusDays(2))
                    .lastUpdatedOn(LocalDateTime.now().minusHours(3))
                    .tableName("dhadiya_showroom_inventory")
                    .dataPayloadJson("{\"vinCode\":\"WBA33AY08PFS99120\",\"modelName\":\"Porsche 911 GT3 RS\",\"trimLevel\":\"Weissach Package\",\"exteriorColor\":\"Guards Red\",\"showroomLocation\":\"Beverly Hills Rodeo Drive Flagship\",\"mileage\":18,\"clientName\":\"Julian Sterling\",\"status\":\"PDI_IN_PROGRESS\",\"msrp\":318500}")
                    .build());

            trackDetRepository.saveAll(List.of(
                    WorkflowTrackDet.builder()
                            .caseNumber("DHADIYA-2026-001")
                            .stageCode("INTAKE_INSPECTION")
                            .stageName("Showroom Floor Intake & Arrival Inspection")
                            .actionTaken("ACCEPT_INTAKE")
                            .actedBy("logistics.lead@americas.dhadiya.com")
                            .remarks("Vehicle arrived in pristine transport container at Beverly Hills staging bay. Flawless odometer check at 18 miles.")
                            .performedOn(LocalDateTime.now().minusDays(2))
                            .durationSeconds(0L)
                            .snapshotDataJson(case1.getDataPayloadJson())
                            .build(),
                    WorkflowTrackDet.builder()
                            .caseNumber("DHADIYA-2026-001")
                            .stageCode("TECHNICAL_PDI")
                            .stageName("Master Technician PDI & Diagnostics")
                            .actionTaken("IN_PROGRESS")
                            .actedBy("chief.tech@americas.dhadiya.com")
                            .remarks("Executing 120-point diagnostic checklist and carbon-ceramic brake inspection.")
                            .performedOn(LocalDateTime.now().minusDays(1))
                            .durationSeconds(86400L)
                            .previousStageCode("INTAKE_INSPECTION")
                            .snapshotDataJson(case1.getDataPayloadJson())
                            .build()
            ));

            taskItemRepository.save(WorkflowTaskItem.builder()
                    .caseNumber("DHADIYA-2026-001")
                    .stageCode("TECHNICAL_PDI")
                    .taskTitle("Complete 120-Point PDI & Diagnostics for Porsche 911 GT3 RS (DHADIYA-2026-001)")
                    .assignedTo("chief.tech@americas.dhadiya.com")
                    .roleName("PDI_TECHNICIAN")
                    .status("PENDING")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .slaDueDate(LocalDateTime.now().plusHours(24))
                    .build());
        }
    }

    private void initCoreDynamicForms() {
        if (formMasterRepository.count() == 0) {
            log.info("Initializing Showroom Management Core Dynamic Form Definitions...");

            FormMaster pdiForm = FormMaster.builder()
                    .realmCode("dhadiya-showroom-global")
                    .formCode("FORM_SHOWROOM_PDI")
                    .formName("Global Showroom Vehicle PDI & Handover Checklist")
                    .formDesc("Standardized multi-section inspection and digital handover protocol for luxury vehicle delivery.")
                    .tableName("dhadiya_showroom_inventory")
                    .category("Showroom Quality & Operations")
                    .status("PUBLISHED")
                    .version(1)
                    .sections(new ArrayList<>(List.of(
                            FormSection.builder()
                                    .sectionKey("SEC_VEHICLE_INFO")
                                    .title("Vehicle Specification & Showroom Intake")
                                    .sectionOrder(1)
                                    .columnLayout(2)
                                    .components(new ArrayList<>(List.of(
                                            FormComponent.builder().fieldKey("vinCode").fieldLabel("Chassis / VIN Code").componentType("TEXT").placeholder("WBA33AY08PFS99120").isRequired(true).fieldOrder(1).build(),
                                            FormComponent.builder().fieldKey("modelName").fieldLabel("Vehicle Model & Make").componentType("TEXT").placeholder("Porsche 911 GT3 RS").isRequired(true).fieldOrder(2).build(),
                                            FormComponent.builder().fieldKey("trimLevel").fieldLabel("Trim Package").componentType("TEXT").placeholder("Weissach Package").isRequired(true).fieldOrder(3).build(),
                                            FormComponent.builder().fieldKey("exteriorColor").fieldLabel("Exterior Finish / Color").componentType("TEXT").placeholder("Guards Red").isRequired(true).fieldOrder(4).build(),
                                            FormComponent.builder().fieldKey("showroomLocation").fieldLabel("Showroom Facility").componentType("SELECT").optionsJson("[\"Beverly Hills Rodeo Drive Flagship\",\"New York 5th Ave Boutique\",\"London Mayfair Gallery\",\"Paris Champs-Élysées\",\"Tokyo Ginza Flagship\",\"Dubai Downtown Gallery\"]").isRequired(true).fieldOrder(5).build(),
                                            FormComponent.builder().fieldKey("deliveryMileage").fieldLabel("Arrival Odometer (miles)").componentType("NUMBER").placeholder("18").isRequired(true).fieldOrder(6).build()
                                    )))
                                    .build(),
                            FormSection.builder()
                                    .sectionKey("SEC_PDI_INSPECTION")
                                    .title("PDI Diagnostics & Staging Checklist")
                                    .sectionOrder(2)
                                    .columnLayout(2)
                                    .components(new ArrayList<>(List.of(
                                            FormComponent.builder().fieldKey("paintFinishFlawless").fieldLabel("Paint Finish 100% Flawless?").componentType("SELECT").optionsJson("[\"YES\",\"NO\"]").isRequired(true).fieldOrder(1).build(),
                                            FormComponent.builder().fieldKey("carbonCeramicBrakesPassed").fieldLabel("Braking & Chassis Diagnostics").componentType("SELECT").optionsJson("[\"PASS\",\"FAIL\",\"NOT_APPLICABLE\"]").isRequired(true).fieldOrder(2).build(),
                                            FormComponent.builder().fieldKey("cockpitElectronicsVerified").fieldLabel("Cockpit Infotainment & Audio Tested").componentType("SELECT").optionsJson("[\"PASS\",\"FAIL\"]").isRequired(true).fieldOrder(3).build(),
                                            FormComponent.builder().fieldKey("technicianRemarks").fieldLabel("Technician Notes & Snag Details").componentType("TEXTAREA").placeholder("Detail any cosmetic micro-blemishes or diagnostic fault logs...").isRequired(false).fieldOrder(4).build()
                                    )))
                                    .build(),
                            FormSection.builder()
                                    .sectionKey("SEC_VIP_HANDOVER")
                                    .title("VIP Client Delivery & Acceptance")
                                    .sectionOrder(3)
                                    .columnLayout(2)
                                    .components(new ArrayList<>(List.of(
                                            FormComponent.builder().fieldKey("clientName").fieldLabel("Client Full Name").componentType("TEXT").placeholder("Julian Sterling").isRequired(true).fieldOrder(1).build(),
                                            FormComponent.builder().fieldKey("vipHandoverDate").fieldLabel("Delivery Ceremony Date").componentType("DATE").isRequired(true).fieldOrder(2).build(),
                                            FormComponent.builder().fieldKey("giftPackPresented").fieldLabel("Champagne & Welcome Gift Delivered?").componentType("SELECT").optionsJson("[\"YES\",\"NO\"]").isRequired(true).fieldOrder(3).build(),
                                            FormComponent.builder().fieldKey("clientSignature").fieldLabel("Client Acceptance Acknowledgement").componentType("TEXT").placeholder("Enter client full signature string").isRequired(true).fieldOrder(4).build()
                                    )))
                                    .build()
                    )))
                    .rules(new ArrayList<>(List.of(
                            FormRule.builder()
                                    .ruleName("RequireSnagNotesOnPaintDefect")
                                    .triggerField("paintFinishFlawless")
                                    .ruleType("SET_REQUIRED")
                                    .conditionOperator("EQUALS")
                                    .conditionValue("NO")
                                    .targetFieldsJson("[\"technicianRemarks\"]")
                                    .build()
                    )))
                    .build();

            formMasterRepository.save(pdiForm);
        }
    }

    private void initLegacyCompatibility() {
        if (definitionRepository.count() == 0) {
            definitionRepository.saveAll(List.of(
                    ProcessDefinition.builder()
                            .realmCode("dhadiya-showroom-global")
                            .processKey("WF_SHOWROOM_DELIVERY_HANDOVER")
                            .name("Global Showroom Vehicle PDI & VIP Handover Workflow")
                            .description("5-stage luxury vehicle intake, technical PDI diagnostics, detailing, financial clearance, and VIP handover sign-off.")
                            .version(2)
                            .stagesJson("[\"INTAKE_INSPECTION\",\"TECHNICAL_PDI\",\"LUXURY_DETAILING\",\"VIP_FINANCIAL_CLEARANCE\",\"FINAL_HANDOVER_CEREMONY\"]")
                            .defaultFormKey("FORM_SHOWROOM_PDI")
                            .status("ACTIVE")
                            .build()
            ));

            formRepository.save(DynamicFormTemplate.builder()
                    .realmCode("dhadiya-showroom-global")
                    .templateKey("FORM_SHOWROOM_PDI")
                    .title("Global Showroom Vehicle PDI & Handover Checklist")
                    .version(1)
                    .schemaJson("{\"fields\":[{\"name\":\"vinCode\",\"type\":\"text\",\"required\":true},{\"name\":\"modelName\",\"type\":\"text\",\"required\":true},{\"name\":\"showroomLocation\",\"type\":\"select\",\"options\":[\"Beverly Hills\",\"New York\",\"London\",\"Paris\",\"Tokyo\",\"Dubai\"]}]}")
                    .status("PUBLISHED")
                    .build());

            ProcessInstance inst = instanceRepository.save(ProcessInstance.builder()
                    .trackingNumber("DHADIYA-2026-001")
                    .realmCode("dhadiya-showroom-americas")
                    .processKey("WF_SHOWROOM_DELIVERY_HANDOVER")
                    .currentStage("TECHNICAL_PDI")
                    .status("IN_PROGRESS")
                    .initiatedBy("logistics.lead@americas.dhadiya.com")
                    .payloadJson("{\"vinCode\":\"WBA33AY08PFS99120\",\"modelName\":\"Porsche 911 GT3 RS\",\"showroomLocation\":\"Beverly Hills Rodeo Drive Flagship\"}")
                    .build());

            taskRepository.save(ProcessTask.builder()
                    .instanceId(inst.getId())
                    .trackingNumber("DHADIYA-2026-001")
                    .stageName("TECHNICAL_PDI")
                    .title("Perform 120-Point PDI & Diagnostics for Porsche 911 GT3 RS (DHADIYA-2026-001)")
                    .assignee("chief.tech@americas.dhadiya.com")
                    .status("PENDING")
                    .build());
        }

        if (gridDefRepository.count() == 0) {
            gridDefRepository.save(DynamicGridDef.builder()
                    .realmCode("dhadiya-showroom-global")
                    .gridKey("dhadiya_showroom_inventory")
                    .gridName("Global Showroom Floor & Vehicle Inventory")
                    .processKey("WF_SHOWROOM_DELIVERY_HANDOVER")
                    .columnSchemaJson("[\"vinCode\",\"modelName\",\"trimLevel\",\"exteriorColor\",\"showroomLocation\",\"vehicleStatus\",\"msrpPrice\",\"allocationDate\",\"assignedAdvisor\"]")
                    .build());

            gridRecordRepository.saveAll(List.of(
                    DynamicGridRecord.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .gridKey("dhadiya_showroom_inventory")
                            .processTrackingNumber("DHADIYA-2026-001")
                            .dataJson("{\"vinCode\":\"WBA33AY08PFS99120\",\"modelName\":\"Porsche 911 GT3 RS\",\"trimLevel\":\"Weissach Package\",\"exteriorColor\":\"Guards Red\",\"showroomLocation\":\"Beverly Hills Rodeo Drive Flagship\",\"vehicleStatus\":\"PDI_IN_PROGRESS\",\"msrpPrice\":\"$318,500\",\"allocationDate\":\"2026-11-10\",\"assignedAdvisor\":\"Marco Rossi\"}")
                            .build(),
                    DynamicGridRecord.builder()
                            .realmCode("dhadiya-showroom-emea")
                            .gridKey("dhadiya_showroom_inventory")
                            .processTrackingNumber("DHADIYA-2026-002")
                            .dataJson("{\"vinCode\":\"ZHWUA1ZF5PLA09812\",\"modelName\":\"Ferrari SF90 Stradale\",\"trimLevel\":\"Assetto Fiorano\",\"exteriorColor\":\"Rosso Corsa\",\"showroomLocation\":\"London Mayfair Gallery\",\"vehicleStatus\":\"ON_FLOOR_DISPLAY\",\"msrpPrice\":\"£425,000\",\"allocationDate\":\"2026-11-08\",\"assignedAdvisor\":\"Arthur Pendelton\"}")
                            .build(),
                    DynamicGridRecord.builder()
                            .realmCode("dhadiya-showroom-apac")
                            .gridKey("dhadiya_showroom_inventory")
                            .processTrackingNumber("DHADIYA-2026-003")
                            .dataJson("{\"vinCode\":\"JTHHP5C13D5001290\",\"modelName\":\"Lexus LFA Nürburgring\",\"trimLevel\":\"Carbon Edition\",\"exteriorColor\":\"Whitest White\",\"showroomLocation\":\"Tokyo Ginza Flagship\",\"vehicleStatus\":\"RESERVED\",\"msrpPrice\":\"¥98,000,000\",\"allocationDate\":\"2026-11-05\",\"assignedAdvisor\":\"Kenji Takahashi\"}")
                            .build()
            ));
        }
    }
}
