package sn.axa.apiaxacnaas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sn.axa.apiaxacnaas.entities.Agence;
import sn.axa.apiaxacnaas.entities.Garantie;
import sn.axa.apiaxacnaas.entities.Role;
import sn.axa.apiaxacnaas.repositories.AgenceRepository;
import sn.axa.apiaxacnaas.repositories.GarantieRepository;
import sn.axa.apiaxacnaas.repositories.RoleRepository;
import sn.axa.apiaxacnaas.util.GarantieEnum;
import sn.axa.apiaxacnaas.util.RoleEnum;
import sn.axa.apiaxacnaas.util.TypeInvalidityEnum;
import sn.axa.apiaxacnaas.util.VilleEnum;

@SpringBootApplication
public class ApiAxaCnaasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAxaCnaasApplication.class, args);
    }
    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository, AgenceRepository agenceRepository, GarantieRepository garantieRepository) {
        return args -> {
            persistRoles(roleRepository);
            persistAgence(agenceRepository);
            persistGaranties(garantieRepository);



        };
    }

    public void persistRoles(RoleRepository roleRepository){
        if (roleRepository.findByName(RoleEnum.ADMIN).isEmpty()) {
            Role role = new Role();
            role.setName(RoleEnum.ADMIN);
            roleRepository.save(role);
        }

        if (roleRepository.findByName(RoleEnum.CHEF_ZONE).isEmpty()) {
            Role roleChefZone = new Role();
            roleChefZone.setName(RoleEnum.CHEF_ZONE);
            roleRepository.save(roleChefZone);
        }

        if (roleRepository.findByName(RoleEnum.CHEF_AGENCE).isEmpty()) {
            Role roleChef_agence = Role.builder()
                    .name(RoleEnum.CHEF_AGENCE)
                    .build();
            roleRepository.save(roleChef_agence);
        }



    }

    public void persistAgence(AgenceRepository agenceRepository){
        if(agenceRepository.findAll().isEmpty()){
            Agence agence = Agence.builder()
                    .name("AxaAgence")
                    .ville(VilleEnum.DAKAR)
                    .build();
            agenceRepository.save(agence);
        }

    }

    public void persistGaranties( GarantieRepository garantieRepository){
        if(garantieRepository.findByName(GarantieEnum.HOSPICASH).isEmpty()){
            Garantie garantie = Garantie.builder()
                    .name(GarantieEnum.HOSPICASH)
                    .build();
            garantieRepository.save(garantie);
        }

        if(garantieRepository.findByName(GarantieEnum.INVALIDITE).isEmpty()){
            Garantie garantie = Garantie.builder()
                    .name(GarantieEnum.INVALIDITE)
                    .type_invalidity(TypeInvalidityEnum.PARTIELLE)
                    .build();
            garantieRepository.save(garantie);
        }
        if(garantieRepository.findByName(GarantieEnum.CAPITAL_FUNERAIRE).isEmpty()){
            Garantie garantie = Garantie.builder()
                    .name(GarantieEnum.CAPITAL_FUNERAIRE)
                    .build();
            garantieRepository.save(garantie);
        }
    }

}
