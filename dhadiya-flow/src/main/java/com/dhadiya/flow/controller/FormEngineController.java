package com.dhadiya.flow.controller;

import com.dhadiya.flow.entity.FormMaster;
import com.dhadiya.flow.entity.FormRule;
import com.dhadiya.flow.service.FormEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Dhadiya eFormsPi Dynamic Forms Engine", description = "Dynamic form layouts, sections, components, and conditional business rules")
public class FormEngineController {

    private final FormEngineService formService;

    @GetMapping({"/api/v1/dynamic-forms/masters", "/formMaster"})
    @Operation(summary = "List all dynamic form master definitions")
    public ResponseEntity<List<FormMaster>> listForms(
            @RequestParam(required = false, defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(formService.listForms(realmCode));
    }

    @GetMapping({"/api/v1/dynamic-forms/masters/{formCode}", "/formMaster/{formCode}"})
    @Operation(summary = "Get complete form layout with sections, components, and rules")
    public ResponseEntity<FormMaster> getForm(@PathVariable String formCode) {
        return ResponseEntity.ok(formService.getForm(formCode));
    }

    @PostMapping({"/api/v1/dynamic-forms/masters", "/formMaster"})
    @Operation(summary = "Save or update a dynamic form definition")
    public ResponseEntity<FormMaster> saveForm(@RequestBody FormMaster form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formService.saveForm(form));
    }

    @GetMapping("/api/v1/dynamic-forms/masters/{formCode}/rules")
    @Operation(summary = "Get dynamic conditional rules for a form")
    public ResponseEntity<List<FormRule>> getRules(@PathVariable String formCode) {
        return ResponseEntity.ok(formService.getRules(formCode));
    }
}
