package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.GroupDTO;
import sn.axa.apiaxacnaas.entities.Group;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.mappers.GroupMapper;
import sn.axa.apiaxacnaas.repositories.GroupRepository;
import java.util.List;

import static java.lang.Math.log;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final UserService userService;
    private final ContractService contractService;
    public GroupDTO createGroup(GroupDTO groupDTO){
        Group group = groupMapper.toEntity(groupDTO);
        User currentUser = userService.getCurrentUser();
        group.setUser(currentUser);
        if(group.getInsureds()!=null){
            group.getInsureds().forEach(insured -> {
                insured.setGroup(group);
            });
        }
        Group savedGroup = groupRepository.save(group);
        contractService.createContract(group,null);

        return groupMapper.toDTO(savedGroup);
    }

    public GroupDTO getFroupById(Long id){
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));
        return  groupMapper.toDTO(existingGroup);
    }

    public List<GroupDTO> getAllGroups(){
        List<Group> listGroups = groupRepository.findAll();
        return listGroups.stream().map(groupMapper::toDTO).toList();
    }

    public GroupDTO updateGroup(GroupDTO groupDTO, Long id){
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));
        existingGroup.setName(groupDTO.getName());
        Group saveGroup = groupRepository.save(existingGroup);
        return groupMapper.toDTO(saveGroup);
    }

    public void deleteGroup(Long id){
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));
        groupRepository.delete(existingGroup);
    }

}
