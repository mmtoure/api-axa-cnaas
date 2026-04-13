package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ClaimMonthlyStatDTO;
import sn.axa.apiaxacnaas.dto.DashboardMonthlyDTO;
import sn.axa.apiaxacnaas.dto.InsuredMonthlyStatDTO;
import sn.axa.apiaxacnaas.entities.Role;
import sn.axa.apiaxacnaas.entities.User;
import sn.axa.apiaxacnaas.repositories.ClaimRepository;
import sn.axa.apiaxacnaas.repositories.InsuredRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final  ClaimService claimService;
    private final ContractService contractService;
    private final InsuredService insuredService;
    private final GroupService groupService;
    private final InsuredRepository insuredRepository;
    private final ClaimRepository claimRepository;
    private final UserService userService;

    public Map<String, Object> getDashboardData(){
        Map<String, Object> returnValue = new LinkedHashMap<>();

        returnValue.put("openClaims", claimService.getNbOpenClaim());
        returnValue.put("acceptedClaims", claimService.getNbAcceptedClaim());
        returnValue.put("activeInsured", contractService.getNbContractActive());
        returnValue.put("nbInsureds", insuredService.getNbInsureds());
        returnValue.put("nbGroups", groupService.getNbGroups());
        returnValue.put("insuredByMonth", getMonthlyStats());
        returnValue.put("recentClaims", claimService.getLatest5claimsForCurrentUser());
        returnValue.put("recentInsureds", insuredService.getLatest5InsuredsForCurrentUser());


        return returnValue;
    }
    public List<DashboardMonthlyDTO>  getMonthlyStats() {
        User currentUser = userService.getCurrentUser();
        Role currentRole = currentUser.getRole();
        List<ClaimMonthlyStatDTO> claimMonthlyStats = new ArrayList<>() ;
        List<InsuredMonthlyStatDTO> insuredMonthlyStats= new ArrayList<>() ;
        Map<String, DashboardMonthlyDTO> map = new TreeMap<>();


            insuredMonthlyStats = insuredRepository.countAllInsuredByMonth();
            claimMonthlyStats = claimRepository.countAllClaimsByMonth();


        if(insuredMonthlyStats!=null){
            for(InsuredMonthlyStatDTO item: insuredMonthlyStats){
                String key = item.year()+"-"+item.month();
                map.put(key, new DashboardMonthlyDTO(
                        item.year(),
                        item.month(),
                        item.total(),
                        0L
                ));

            }
        }

        if(claimMonthlyStats!=null){
            for(ClaimMonthlyStatDTO item : claimMonthlyStats){
                String key = item.year()+"-"+item.month();
                if(map.containsKey(key)){
                    map.put(key, new DashboardMonthlyDTO(
                            item.year(),
                            item.month(),
                            map.get(key).totalInsured(),
                            item.total()

                    ));
                }


            }
        }




        return new ArrayList<>(map.values());

    }

    }
