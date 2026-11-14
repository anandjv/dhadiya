package com.dhadiya.flow.service;

import com.dhadiya.flow.dto.GridDataResponseDto;
import com.dhadiya.flow.dto.GridQueryDto;
import com.dhadiya.flow.dto.GridRecordDto;
import com.dhadiya.flow.entity.DynamicGridDef;
import com.dhadiya.flow.entity.DynamicGridRecord;
import com.dhadiya.flow.repository.DynamicGridDefRepository;
import com.dhadiya.flow.repository.DynamicGridRecordRepository;
import com.dhadiya.flow.repository.DynamicGridViewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamicGridServiceTest {

    @Mock
    private DynamicGridDefRepository defRepository;

    @Mock
    private DynamicGridRecordRepository recordRepository;

    @Mock
    private DynamicGridViewRepository viewRepository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private DynamicGridService gridService;

    private DynamicGridDef testDef;
    private DynamicGridRecord testRecord;

    @BeforeEach
    void setUp() {
        testDef = DynamicGridDef.builder()
                .id(1L)
                .realmCode("dhadiya-showroom-global")
                .gridKey("dhadiya_showroom_inventory")
                .gridName("Global Showroom Floor & Vehicle Inventory")
                .processKey("WF_SHOWROOM_DELIVERY_HANDOVER")
                .defaultPageSize(20)
                .build();

        testRecord = DynamicGridRecord.builder()
                .id(101L)
                .realmCode("dhadiya-showroom-global")
                .gridKey("dhadiya_showroom_inventory")
                .processTrackingNumber("DHADIYA-2026-001")
                .dataJson("{\"vinCode\":\"WBA33AY08PFS99120\",\"modelName\":\"Porsche 911 GT3 RS\",\"showroomLocation\":\"Beverly Hills\"}")
                .build();
    }

    @Test
    void testListGridDefinitions() {
        when(defRepository.findByRealmCode("dhadiya-showroom-global")).thenReturn(List.of(testDef));

        List<DynamicGridDef> list = gridService.listGridDefinitions("dhadiya-showroom-global");
        assertEquals(1, list.size());
        assertEquals("dhadiya_showroom_inventory", list.get(0).getGridKey());
    }

    @Test
    void testQueryGrid() {
        when(defRepository.findByGridKey("dhadiya_showroom_inventory")).thenReturn(Optional.of(testDef));
        when(recordRepository.findByRealmCodeAndGridKey("dhadiya-showroom-global", "dhadiya_showroom_inventory")).thenReturn(List.of(testRecord));

        GridQueryDto query = GridQueryDto.builder()
                .realmCode("dhadiya-showroom-global")
                .gridKey("dhadiya_showroom_inventory")
                .page(0)
                .size(10)
                .build();

        GridDataResponseDto response = gridService.queryGrid(query);
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getRows().size());
        assertEquals("WBA33AY08PFS99120", response.getRows().get(0).get("vinCode"));
    }

    @Test
    void testInsertRecord() {
        when(recordRepository.save(any(DynamicGridRecord.class))).thenReturn(testRecord);

        GridRecordDto dto = GridRecordDto.builder()
                .realmCode("dhadiya-showroom-global")
                .gridKey("dhadiya_showroom_inventory")
                .processTrackingNumber("DHADIYA-2026-001")
                .dataJson("{\"vinCode\":\"WBA33AY08PFS99120\"}")
                .build();

        DynamicGridRecord created = gridService.insertRecord(dto);
        assertNotNull(created);
        assertEquals("dhadiya_showroom_inventory", created.getGridKey());
    }
}
