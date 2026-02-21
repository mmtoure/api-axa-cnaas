package sn.axa.apiaxacnaas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.repositories.*;
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
    CommandLineRunner seedRoles(RoleRepository roleRepository, AgenceRepository agenceRepository, PartnerRepository partnerRepository) {
        return args -> {
            persistRoles(roleRepository);
            persistAgence(agenceRepository);
            persistPartner(partnerRepository);
        };
    }
    public void persistRoles(RoleRepository roleRepository){
        if (roleRepository.findByName(RoleEnum.ADMIN).isEmpty()) {
            Role role = new Role();
            role.setName(RoleEnum.ADMIN);
            roleRepository.save(role);
        }
        if (roleRepository.findByName(RoleEnum.MANAGER).isEmpty()) {
            Role roleChefZone = new Role();
            roleChefZone.setName(RoleEnum.MANAGER);
            roleRepository.save(roleChefZone);
        }
        if (roleRepository.findByName(RoleEnum.USER).isEmpty()) {
            Role roleChef_agence = Role.builder()
                    .name(RoleEnum.USER)
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
    public void persistPartner(PartnerRepository partnerRepository){
        if(partnerRepository.findAll().isEmpty()){
            Partner partner = Partner.builder()
                    .name("CNAAS")
                    .code("CNAAS")
                    .email("contact@cnaas.sn")
                    .phoneNumber("773606060")
                    .build();
            partnerRepository.save(partner);
        }

    }

}
