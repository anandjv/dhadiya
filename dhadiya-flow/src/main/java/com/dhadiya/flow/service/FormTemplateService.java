package com.dhadiya.flow.service;

import com.dhadiya.flow.dto.FormTemplateRequest;
import com.dhadiya.flow.entity.DynamicFormTemplate;
import com.dhadiya.flow.repository.DynamicFormTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormTemplateService {

    private final DynamicFormTemplateRepository templateRepository;

    @Transactional(readOnly = true)
    public List<DynamicFormTemplate> getTemplatesByRealm(String realmCode) {
        return templateRepository.findByRealmCode(realmCode);
    }

    @Transactional(readOnly = true)
    public DynamicFormTemplate getTemplate(String realmCode, String templateKey) {
        return templateRepository.findByRealmCodeAndTemplateKey(realmCode, templateKey)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateKey));
    }

    @Transactional
    public DynamicFormTemplate saveTemplate(FormTemplateRequest request) {
        DynamicFormTemplate template = templateRepository.findByRealmCodeAndTemplateKey(
                request.getRealmCode(), request.getTemplateKey())
                .orElse(DynamicFormTemplate.builder()
                        .realmCode(request.getRealmCode())
                        .templateKey(request.getTemplateKey())
                        .build());

        template.setTitle(request.getTitle());
        template.setVersion(request.getVersion() != null ? request.getVersion() : 1);
        template.setSchemaJson(request.getSchemaJson());
        template.setStatus("PUBLISHED");

        return templateRepository.save(template);
    }
}
