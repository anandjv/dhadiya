package com.dhadiya.tenants.config;

import com.dhadiya.tenants.entity.TenantRealm;
import com.dhadiya.tenants.entity.TenantUser;
import com.dhadiya.tenants.repository.TenantRealmRepository;
import com.dhadiya.tenants.repository.TenantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TenantRealmRepository repository;
    private final TenantUserRepository userRepository;

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

        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .email("general.manager@americas.dhadiya.com")
                            .fullName("Sebastian Vance")
                            .role("SHOWROOM_DIRECTOR")
                            .showroomLocation("Beverly Hills Rodeo Drive Flagship")
                            .phoneNumber("+1 (310) 555-0192")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(2))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .email("chief.tech@americas.dhadiya.com")
                            .fullName("Marcus Rossi")
                            .role("PDI_TECHNICIAN")
                            .showroomLocation("Beverly Hills Rodeo Drive Flagship")
                            .phoneNumber("+1 (310) 555-0144")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(1))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .email("detailing.lead@americas.dhadiya.com")
                            .fullName("Elena Rostova")
                            .role("DETAILING_SPECIALIST")
                            .showroomLocation("Beverly Hills Rodeo Drive Flagship")
                            .phoneNumber("+1 (310) 555-0188")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusDays(1))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .email("finance.manager@americas.dhadiya.com")
                            .fullName("Harrison Sterling")
                            .role("FINANCE_MANAGER")
                            .showroomLocation("New York 5th Ave Boutique")
                            .phoneNumber("+1 (212) 555-0177")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(4))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-emea")
                            .email("arthur.pendelton@emea.dhadiya.com")
                            .fullName("Arthur Pendelton")
                            .role("SALES_ADVISOR")
                            .showroomLocation("London Mayfair Gallery")
                            .phoneNumber("+44 20 7946 0912")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusDays(2))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-apac")
                            .email("kenji.takahashi@apac.dhadiya.com")
                            .fullName("Kenji Takahashi")
                            .role("SHOWROOM_DIRECTOR")
                            .showroomLocation("Tokyo Ginza Flagship")
                            .phoneNumber("+81 3 5555 0122")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(6))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-global")
                            .email("compliance@showrooms.dhadiya.com")
                            .fullName("Dr. Charlotte Sinclair")
                            .role("BRAND_AUDITOR")
                            .showroomLocation("Global HQ & Inspection Audit Division")
                            .phoneNumber("+44 20 7946 0199")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusMinutes(45))
                            .build()
            ));
        }
    }
}
