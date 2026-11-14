package com.dhadiya.tenants.config;

import com.dhadiya.tenants.entity.TenantLocation;
import com.dhadiya.tenants.entity.TenantRealm;
import com.dhadiya.tenants.entity.TenantRole;
import com.dhadiya.tenants.entity.TenantUser;
import com.dhadiya.tenants.repository.TenantLocationRepository;
import com.dhadiya.tenants.repository.TenantRealmRepository;
import com.dhadiya.tenants.repository.TenantRoleRepository;
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
    private final TenantLocationRepository locationRepository;
    private final TenantRoleRepository roleRepository;
    private final TenantUserRepository userRepository;

    @Override
    public void run(String... args) {
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

        if (locationRepository.count() == 0) {
            locationRepository.saveAll(List.of(
                    // TVS Customer Showroom Locations
                    TenantLocation.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-BLR-01")
                            .locationName("Bangalore Indiranagar Flagship Experience")
                            .city("Bangalore")
                            .stateProvince("Karnataka")
                            .country("India")
                            .address("100ft Road, HAL 2nd Stage, Indiranagar, Bangalore")
                            .phoneNumber("+91 80 4123 4567")
                            .managerEmail("arun.nair@tvs.dhadiya.com")
                            .status("ACTIVE")
                            .build(),
                    TenantLocation.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-CHN-01")
                            .locationName("Chennai Mount Road Experience Centre")
                            .city("Chennai")
                            .stateProvince("Tamil Nadu")
                            .country("India")
                            .address("Anna Salai, Mount Road, Chennai")
                            .phoneNumber("+91 44 2852 9876")
                            .managerEmail("karthik.s@tvs.dhadiya.com")
                            .status("ACTIVE")
                            .build(),
                    TenantLocation.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-DEL-01")
                            .locationName("Delhi South Premium Mobility Hub")
                            .city("New Delhi")
                            .stateProvince("Delhi NCR")
                            .country("India")
                            .address("Ring Road, Lajpat Nagar IV, New Delhi")
                            .phoneNumber("+91 11 4987 6543")
                            .managerEmail("vikram.mehta@tvs.dhadiya.com")
                            .status("ACTIVE")
                            .build(),
                    TenantLocation.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-MUM-01")
                            .locationName("Mumbai Bandra Experience Gallery")
                            .city("Mumbai")
                            .stateProvince("Maharashtra")
                            .country("India")
                            .address("Linking Road, Bandra West, Mumbai")
                            .phoneNumber("+91 22 6678 1234")
                            .managerEmail("rohit.deshmukh@tvs.dhadiya.com")
                            .status("ACTIVE")
                            .build(),

                    // Luxury Network Showrooms
                    TenantLocation.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .locationCode("BH-RODEO-01")
                            .locationName("Beverly Hills Rodeo Drive Flagship")
                            .city("Beverly Hills")
                            .stateProvince("California")
                            .country("USA")
                            .address("420 N Rodeo Dr, Beverly Hills, CA 90210")
                            .phoneNumber("+1 (310) 555-0192")
                            .managerEmail("general.manager@americas.dhadiya.com")
                            .status("ACTIVE")
                            .build(),
                    TenantLocation.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .locationCode("NY-5TH-01")
                            .locationName("New York 5th Ave Boutique")
                            .city("New York")
                            .stateProvince("New York")
                            .country("USA")
                            .address("767 5th Ave, New York, NY 10153")
                            .phoneNumber("+1 (212) 555-0177")
                            .managerEmail("finance.manager@americas.dhadiya.com")
                            .status("ACTIVE")
                            .build(),
                    TenantLocation.builder()
                            .realmCode("dhadiya-showroom-emea")
                            .locationCode("LON-MAYFAIR-01")
                            .locationName("London Mayfair Gallery")
                            .city("London")
                            .stateProvince("Greater London")
                            .country("United Kingdom")
                            .address("14 Berkeley Square, London W1J 6BQ")
                            .phoneNumber("+44 20 7946 0912")
                            .managerEmail("arthur.pendelton@emea.dhadiya.com")
                            .status("ACTIVE")
                            .build(),
                    TenantLocation.builder()
                            .realmCode("dhadiya-showroom-apac")
                            .locationCode("TYO-GINZA-01")
                            .locationName("Tokyo Ginza Flagship")
                            .city("Tokyo")
                            .stateProvince("Kanto")
                            .country("Japan")
                            .address("6-10-1 Ginza, Chuo-ku, Tokyo 104-0061")
                            .phoneNumber("+81 3 5555 0122")
                            .managerEmail("kenji.takahashi@apac.dhadiya.com")
                            .status("ACTIVE")
                            .build()
            ));
        }

        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    TenantRole.builder()
                            .realmCode("GLOBAL")
                            .roleCode("SHOWROOM_DIRECTOR")
                            .roleName("Showroom Branch Director")
                            .description("Full administrative clearance, VIP handover ceremony sign-off, and local dealership inventory allocation.")
                            .locationScope("ALL")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"MANAGE_STAFF\",\"APPROVE_PDI\",\"DELIVER_VEHICLE\",\"VIEW_FINANCE\",\"MANAGE_LOCATIONS\"]")
                            .colorCode("#9333ea")
                            .isSystemRole(true)
                            .build(),
                    TenantRole.builder()
                            .realmCode("GLOBAL")
                            .roleCode("PDI_TECHNICIAN")
                            .roleName("Master Diagnostic & PDI Technician")
                            .description("Executes 120-point pre-delivery inspections, OBD-II telemetry checks, and snag rework logs.")
                            .locationScope("ALL")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"VIEW_WORKFLOWS\",\"PERFORM_PDI\",\"LOG_SNAGS\"]")
                            .colorCode("#d97706")
                            .isSystemRole(true)
                            .build(),
                    TenantRole.builder()
                            .realmCode("GLOBAL")
                            .roleCode("DETAILING_SPECIALIST")
                            .roleName("Bespoke Detailing Specialist")
                            .description("Luxury paint finish inspection, ceramic coating certification, and floor staging clearance.")
                            .locationScope("ALL")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"VIEW_WORKFLOWS\",\"DETAILING_CLEARANCE\"]")
                            .colorCode("#4f46e5")
                            .isSystemRole(true)
                            .build(),
                    TenantRole.builder()
                            .realmCode("GLOBAL")
                            .roleCode("FINANCE_MANAGER")
                            .roleName("Client Financial & Insurance Manager")
                            .description("High-net-worth client financial verification, insurance binding, and payment clearance.")
                            .locationScope("ALL")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"VIEW_FINANCE\",\"APPROVE_FINANCE\"]")
                            .colorCode("#059669")
                            .isSystemRole(true)
                            .build(),
                    TenantRole.builder()
                            .realmCode("GLOBAL")
                            .roleCode("SALES_ADVISOR")
                            .roleName("VIP Client Sales Advisor")
                            .description("Showroom floor client hosting, vehicle allocation, test drives, and delivery ceremony.")
                            .locationScope("ALL")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"VIEW_INVENTORY\",\"ALLOCATE_VEHICLE\",\"HANDOVER_CEREMONY\"]")
                            .colorCode("#0284c7")
                            .isSystemRole(true)
                            .build(),

                    // TVS Custom Dynamic Roles
                    TenantRole.builder()
                            .realmCode("tvs-motors")
                            .roleCode("EV_DIAGNOSTIC_SPECIALIST")
                            .roleName("EV High-Voltage Telematics Lead")
                            .description("Certified high-voltage battery health verification, BMS diagnostics, and fast-charging calibration.")
                            .locationScope("ALL")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"VIEW_WORKFLOWS\",\"EV_TELEMATICS_AUDIT\",\"BATTERY_HEALTH_CERTIFY\"]")
                            .colorCode("#0d9488")
                            .isSystemRole(false)
                            .build(),
                    TenantRole.builder()
                            .realmCode("tvs-motors")
                            .roleCode("TEST_RIDE_COORDINATOR")
                            .roleName("Track & Test Ride Coordinator")
                            .description("Coordinates client test rides, safety waivers, and track experience days.")
                            .locationScope("TVS-BLR-01")
                            .permissionsJson("[\"VIEW_DASHBOARD\",\"SCHEDULE_TEST_RIDE\",\"LOG_CLIENT_FEEDBACK\"]")
                            .colorCode("#ea580c")
                            .isSystemRole(false)
                            .build()
            ));
        }

        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                    // TVS Staff
                    TenantUser.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-BLR-01")
                            .showroomLocation("Bangalore Indiranagar Flagship Experience")
                            .email("arun.nair@tvs.dhadiya.com")
                            .fullName("Arun Nair")
                            .role("SHOWROOM_DIRECTOR")
                            .phoneNumber("+91 80 4123 4567")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(1))
                            .build(),
                    TenantUser.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-BLR-01")
                            .showroomLocation("Bangalore Indiranagar Flagship Experience")
                            .email("priya.sharma@tvs.dhadiya.com")
                            .fullName("Priya Sharma")
                            .role("EV_DIAGNOSTIC_SPECIALIST")
                            .phoneNumber("+91 80 4123 4599")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(3))
                            .build(),
                    TenantUser.builder()
                            .realmCode("tvs-motors")
                            .locationCode("TVS-CHN-01")
                            .showroomLocation("Chennai Mount Road Experience Centre")
                            .email("karthik.s@tvs.dhadiya.com")
                            .fullName("Karthik Sundaram")
                            .role("SHOWROOM_DIRECTOR")
                            .phoneNumber("+91 44 2852 9876")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusDays(1))
                            .build(),

                    // Luxury Showrooms Staff
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .locationCode("BH-RODEO-01")
                            .showroomLocation("Beverly Hills Rodeo Drive Flagship")
                            .email("general.manager@americas.dhadiya.com")
                            .fullName("Sebastian Vance")
                            .role("SHOWROOM_DIRECTOR")
                            .phoneNumber("+1 (310) 555-0192")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(2))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-americas")
                            .locationCode("BH-RODEO-01")
                            .showroomLocation("Beverly Hills Rodeo Drive Flagship")
                            .email("chief.tech@americas.dhadiya.com")
                            .fullName("Marcus Rossi")
                            .role("PDI_TECHNICIAN")
                            .phoneNumber("+1 (310) 555-0144")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(1))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-emea")
                            .locationCode("LON-MAYFAIR-01")
                            .showroomLocation("London Mayfair Gallery")
                            .email("arthur.pendelton@emea.dhadiya.com")
                            .fullName("Arthur Pendelton")
                            .role("SALES_ADVISOR")
                            .phoneNumber("+44 20 7946 0912")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusDays(2))
                            .build(),
                    TenantUser.builder()
                            .realmCode("dhadiya-showroom-apac")
                            .locationCode("TYO-GINZA-01")
                            .showroomLocation("Tokyo Ginza Flagship")
                            .email("kenji.takahashi@apac.dhadiya.com")
                            .fullName("Kenji Takahashi")
                            .role("SHOWROOM_DIRECTOR")
                            .phoneNumber("+81 3 5555 0122")
                            .status("ACTIVE")
                            .lastLoginAt(LocalDateTime.now().minusHours(6))
                            .build()
            ));
        }
    }
}
