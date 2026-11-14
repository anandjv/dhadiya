package com.dhadiya.flow.service;

import com.dhadiya.flow.entity.FormMaster;
import com.dhadiya.flow.entity.FormRule;
import com.dhadiya.flow.repository.FormMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormEngineService {

    private final FormMasterRepository formRepository;

    @Transactional(readOnly = true)
    public List<FormMaster> listForms(String realmCode) {
        if (realmCode != null && !realmCode.isBlank()) {
            return formRepository.findByRealmCode(realmCode);
        }
        return formRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FormMaster getForm(String formCode) {
        return formRepository.findByFormCode(formCode)
                .orElseThrow(() -> new IllegalArgumentException("Form not found: " + formCode));
    }

    @Transactional
    public FormMaster saveForm(FormMaster form) {
        return formRepository.save(form);
    }

    @Transactional(readOnly = true)
    public List<FormRule> getRules(String formCode) {
        return formRepository.findByFormCode(formCode)
                .map(FormMaster::getRules)
                .orElse(Collections.emptyList());
    }
}
