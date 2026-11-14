package com.dhadiya.tenants.config;

import com.dhadiya.tenants.entity.TenantRealm;
import com.dhadiya.tenants.repository.TenantRealmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TenantRealmRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-global")
                            .organizationName("Dhadiya Global Showrooms Network")
                            .customDomain("showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"theme\":\"luxury-dark\",\"locale\":\"en-US\",\"plan\":\"ENTERPRISE\",\"features\":[\"workflows\",\"datagrid\",\"pdi_inspection\",\"vip_lounge\"]}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-emea")
                            .organizationName("Dhadiya Luxury Showrooms EMEA (London, Paris, Milan, Dubai)")
                            .customDomain("emea.showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"currency\":\"EUR\",\"flagshipLocations\":[\"London Mayfair\",\"Paris Champs-Élysées\",\"Milan Montenapoleone\",\"Dubai Downtown\"],\"brandComplianceStandard\":\"ISO-9001-LUXURY\"}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .organizationName("Dhadiya Flagship Showrooms Americas (New York, Beverly Hills, Miami)")
                            .customDomain("americas.showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"currency\":\"USD\",\"flagshipLocations\":[\"New York 5th Ave\",\"Beverly Hills Rodeo Dr\",\"Miami Brickell\",\"Toronto Yorkville\"],\"salesCadence\":\"DAILY\"}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-apac")
                            .organizationName("Dhadiya Prestige Showrooms APAC (Tokyo, Singapore, Shanghai)")
                            .customDomain("apac.showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"currency\":\"JPY/SGD\",\"flagshipLocations\":[\"Tokyo Ginza\",\"Singapore Marina Bay\",\"Shanghai Bund\",\"Sydney CBD\"],\"vipAllocation\":true}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-hq")
                            .organizationName("Dhadiya Global Operations")
                            .customDomain("hq.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"theme\":\"corporate\",\"locale\":\"en-US\",\"plan\":\"GLOBAL\"}")
                            .build()
            ));
        }
    }
}
