package sn.axa.apiaxacnaas.mappers;

import sn.axa.apiaxacnaas.dto.BeneficiaireDTO;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.entities.Group;

public interface BeneficiaireMapper {
   BeneficiaireDTO toDTO(Group entity);
    Group toEntity(GroupDTO dto);
}
