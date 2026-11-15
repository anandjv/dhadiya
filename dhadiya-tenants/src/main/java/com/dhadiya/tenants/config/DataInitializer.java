package com.dhadiya.tenants.config;

import com.dhadiya.tenants.entity.*;
import com.dhadiya.tenants.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TenantRealmRepository repository;
    private final TenantRepository tenantRepository;

    @Override
    public void run(String... args) {
        // Initialize unified single-table Tenant and DBData in dhadiya_global
        if (tenantRepository.count() == 0) {
            String defaultUser = System.getenv().getOrDefault("DB_USER", "postgres");
            String defaultPass = System.getenv().getOrDefault("DB_PASS", "postgres");

            List<Tenant> tenants = List.of(
                    Tenant.builder()
                            .tenantId("default")
                            .tenantName("Default Dhadiya Platform Showroom")
                            .address("Silicon Oasis")
                            .city("Dubai")
                            .country("UAE")
                            .zipCode("00000")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_default")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build(),
                    Tenant.builder()
                            .tenantId("presentation")
                            .tenantName("Dhadiya Presentation Showroom")
                            .address("Downtown Dubai")
                            .city("Dubai")
                            .country("UAE")
                            .zipCode("00000")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_presentation")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build(),
                    Tenant.builder()
                            .tenantId("tvs-motors")
                            .tenantName("TVS Motor Company - Global Dealership Network")
                            .address("100ft Road, HAL 2nd Stage, Indiranagar")
                            .city("Bangalore")
                            .country("India")
                            .zipCode("560038")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_tvs")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build(),
                    Tenant.builder()
                            .tenantId("dhadiya-showroom-americas")
                            .tenantName("Dhadiya Flagship Showrooms Americas")
                            .address("Rodeo Drive")
                            .city("Beverly Hills")
                            .country("USA")
                            .zipCode("90210")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_americas")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build(),
                    Tenant.builder()
                            .tenantId("dhadiya-showroom-emea")
                            .tenantName("Dhadiya Luxury Showrooms EMEA")
                            .address("London Mayfair Gallery")
                            .city("London")
                            .country("UK")
                            .zipCode("W1J 8AJ")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_emea")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build(),
                    Tenant.builder()
                            .tenantId("dhadiya-showroom-apac")
                            .tenantName("Dhadiya Prestige Showrooms APAC")
                            .address("Tokyo Ginza Flagship")
                            .city("Tokyo")
                            .country("Japan")
                            .zipCode("104-0061")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_apac")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build(),
                    Tenant.builder()
                            .tenantId("dhadiya-showroom-global")
                            .tenantName("Dhadiya Global Showrooms Network")
                            .address("Silicon Oasis")
                            .city("Dubai")
                            .country("UAE")
                            .zipCode("00000")
                            .driverName("org.postgresql.Driver")
                            .url("jdbc:postgresql://localhost:5432/dhadiya_default")
                            .userName(defaultUser)
                            .password(defaultPass)
                            .status("ACTIVE")
                            .build()
            );

            tenantRepository.saveAll(tenants);
        }

        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    TenantRealm.builder()
                            .realmCode("tvs-motors")
                            .organizationName("TVS Motor Company - Global Dealership Network")
                            .customDomain("dealers.tvs.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"theme\":\"executive-slate\",\"currency\":\"INR\",\"plan\":\"ENTERPRISE\",\"features\":[\"workflows\",\"datagrid\",\"pdi_inspection\",\"ev_diagnostics\"]}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .organizationName("Dhadiya Flagship Showrooms Americas (New York, Beverly Hills, Miami)")
                            .customDomain("americas.showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"currency\":\"USD\",\"flagshipLocations\":[\"New York 5th Ave\",\"Beverly Hills Rodeo Dr\",\"Miami Brickell\"],\"salesCadence\":\"DAILY\"}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-emea")
                            .organizationName("Dhadiya Luxury Showrooms EMEA (London, Paris, Milan, Dubai)")
                            .customDomain("emea.showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"currency\":\"EUR\",\"flagshipLocations\":[\"London Mayfair\",\"Paris Champs-Élysées\",\"Dubai Downtown\"],\"brandComplianceStandard\":\"ISO-9001-LUXURY\"}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-apac")
                            .organizationName("Dhadiya Prestige Showrooms APAC (Tokyo, Singapore, Shanghai)")
                            .customDomain("apac.showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"currency\":\"JPY/SGD\",\"flagshipLocations\":[\"Tokyo Ginza\",\"Singapore Marina Bay\"],\"vipAllocation\":true}")
                            .build(),
                    TenantRealm.builder()
                            .realmCode("dhadiya-showroom-global")
                            .organizationName("Dhadiya Global Showrooms Network")
                            .customDomain("showrooms.dhadiya.com")
                            .status("ACTIVE")
                            .settingsJson("{\"theme\":\"luxury-dark\",\"locale\":\"en-US\",\"plan\":\"ENTERPRISE\",\"features\":[\"workflows\",\"datagrid\",\"pdi_inspection\",\"vip_lounge\"]}")
                            .build()
            ));
        }
    }
}
