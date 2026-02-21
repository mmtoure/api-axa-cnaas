package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.axa.apiaxacnaas.dto.ClaimMonthlyStatDTO;
import sn.axa.apiaxacnaas.dto.DashboardMonthlyDTO;
import sn.axa.apiaxacnaas.dto.InsuredMonthlyStatDTO;
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
        List<InsuredMonthlyStatDTO> dtoList = insuredRepository.countAllInsuredByMonth();
        List<ClaimMonthlyStatDTO> claimList = claimRepository.countAllClaimsByMonth();
        Map<String, DashboardMonthlyDTO> map = new TreeMap<>();
        for(InsuredMonthlyStatDTO item: dtoList){
            String key = item.year()+"-"+item.month();
            map.put(key, new DashboardMonthlyDTO(
                    item.year(),
                    item.month(),
                    item.total(),
                    0L
            ));

        }
        for(ClaimMonthlyStatDTO item : claimList){
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



        return new ArrayList<>(map.values());

    }

    }
