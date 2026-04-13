package sn.axa.apiaxacnaas.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.entities.Partner;
import sn.axa.apiaxacnaas.entities.Role;
import sn.axa.apiaxacnaas.services.AgenceService;
import sn.axa.apiaxacnaas.services.PartnerService;
import sn.axa.apiaxacnaas.services.RoleService;
import sn.axa.apiaxacnaas.services.UserService;
import sn.axa.apiaxacnaas.util.RoleEnum;
import sn.axa.apiaxacnaas.util.VilleEnum;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleService roleService;
    private final PartnerService partnerService;
    private final AgenceService agenceService;
    private final UserService userService;
    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = roleService.CreateRoleIfNotExist(RoleEnum.ADMIN);
        Role roleUser = roleService.CreateRoleIfNotExist(RoleEnum.USER);
        Role roleManger = roleService.CreateRoleIfNotExist(RoleEnum.MANAGER);
        Partner partner = partnerService.createPartnerIfNotExist("AXA", "AXA");
        Agence agence = agenceService.createAgenceIfNotExist("Dakar", VilleEnum.DAKAR);
        userService.createAdminIfNotExists(
                "Admin",
                "Admin",
                "admin@axa.sn",
                "Axa@2026",
                "774544351",
                partner,
                roleAdmin,
                agence
        );

        System.out.println("✅ Database initialized");



    }
}
