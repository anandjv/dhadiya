package com.dhadiya.flow.controller;

import com.dhadiya.flow.dto.FormTemplateRequest;
import com.dhadiya.flow.entity.DynamicFormTemplate;
import com.dhadiya.flow.service.FormTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forms")
@RequiredArgsConstructor
@Tag(name = "Dhadiya Dynamic Forms API", description = "Dynamic form schemas and field templates for dhadiya.com")
public class FormTemplateController {

    private final FormTemplateService formTemplateService;

    @GetMapping
    @Operation(summary = "List form templates for a realm")
    public ResponseEntity<List<DynamicFormTemplate>> getTemplates(
            @RequestParam(defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(formTemplateService.getTemplatesByRealm(realmCode));
    }

    @GetMapping("/{templateKey}")
    @Operation(summary = "Get form template by key")
    public ResponseEntity<DynamicFormTemplate> getTemplate(
            @PathVariable String templateKey,
            @RequestParam(defaultValue = "dhadiya-showroom-global") String realmCode) {
        return ResponseEntity.ok(formTemplateService.getTemplate(realmCode, templateKey));
    }

    @PostMapping
    @Operation(summary = "Save or update dynamic form schema")
    public ResponseEntity<DynamicFormTemplate> saveTemplate(@Valid @RequestBody FormTemplateRequest request) {
        return ResponseEntity.ok(formTemplateService.saveTemplate(request));
    }
}
