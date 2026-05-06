package sn.axa.apiaxacnaas.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.axa.apiaxacnaas.entities.Region;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.repositories.DepartmentRepository;
import sn.axa.apiaxacnaas.repositories.RegionRepository;
import sn.axa.apiaxacnaas.repositories.UserRepository;
import sn.axa.apiaxacnaas.services.RegionService;
import sn.axa.apiaxacnaas.util.DepartementEnum;
import sn.axa.apiaxacnaas.util.RegionEnum;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegionSeeder implements CommandLineRunner {
    private final RegionRepository regionRepository;
    private final RegionService regionService;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    @Override
    public void run(String... args) throws Exception {
        if (regionRepository.count() >0) return;

        List<Region> regions = List.of(

                create("DAKAR", List.of("DAKAR", "GUEDIAWAYE", "PIKINE", "RUFISQUE")),

                create("THIES", List.of("THIES", "TIVAOUANE", "MBOUR")),

                create("DIOURBEL", List.of("DIOURBEL", "BAMBEY", "MBACKE")),

                create("SAINT_LOUIS", List.of("SAINT_LOUIS", "DAGANA", "PODOR")),

                create("LOUGA", List.of("LOUGA", "LINGUERE", "KEBEMER")),

                create("FATICK", List.of("FATICK", "FOUNDIOUGNE", "GOSSAS")),

                create("KAOLACK", List.of("KAOLACK", "NIORO_DU_RIP", "GUINGUINEO")),

                create("KAFFRINE", List.of("KAFFRINE", "BIRKILANE", "KOUNGHEUL", "MALEM_HODAR")),

                create("KOLDA", List.of("KOLDA", "VELINGARA", "MEDINA_YORO_FOULAH")),

                create("SEDHIOU", List.of("SEDHIOU", "GOUDOMP", "BOUNKILING")),

                create("ZIGUINCHOR", List.of("ZIGUINCHOR", "BIGNONA", "OUSSOUYE")),

                create("TAMBACOUNDA", List.of("TAMBACOUNDA", "GOUDIRY", "KOUMPENTOUM")),

                create("KEDOUGOU", List.of("KEDOUGOU", "SARAYA", "SALEMATA")),

                create("MATAM", List.of("MATAM", "KANEL", "RANEROU"))
        );
        regionRepository.saveAll(regions);
        System.out.println("✅ Régions du Sénégal créées avec départements");


    }
    private Region create(String name, List<String> departments) {
        Region region = new Region();
        region.setName(RegionEnum.valueOf(name));
        region.setDepartments(departments);
        return region;
    }
}
