package sn.axa.apiaxacnaas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.axa.apiaxacnaas.dto.*;
import sn.axa.apiaxacnaas.entities.*;
import sn.axa.apiaxacnaas.exceptions.ResourceNotFoundException;
import sn.axa.apiaxacnaas.mappers.ClaimDocumentMapper;
import sn.axa.apiaxacnaas.mappers.ClaimMapper;
import sn.axa.apiaxacnaas.repositories.*;
import sn.axa.apiaxacnaas.util.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;
@Service
@RequiredArgsConstructor
public class ClaimService {
    private final ClaimRepository claimRepository;
    private final ClaimMapper claimMapper;
    private final ClaimDocumentService claimDocumentService;
    private final ClaimDocumentMapper claimDocumentMapper;
    private final ContractRepository contractRepository;
    private  final InsuredRepository insuredRepository;
    private final UserService userService;
    private final ClaimHistoryService claimHistoryService;
    private final NotificationService notificationService;

    public ClaimDTO createClaim(ClaimDTO claimDTO, List<MultipartFile> files, List<ClaimDocumentType> types) throws IOException {
        Insured insured = insuredRepository.findById(claimDTO.getInsuredId())
                .orElseThrow(()->new ResourceNotFoundException("Assure n'existe pas"));
        Contract contract = insured.getContract();
        if(contract.getStatus()!=StatusContract.ACTIVE){
            throw  new ResourceNotFoundException("Pas de sinistre car contrat n'est pas actif");
        }

        if(types==null || files.size() != types.size()) {
            throw new ResourceNotFoundException("Chaque document doit avoir un type");
        }
        // 🔹 Création du sinistre
        Claim claim = Claim.builder()
                .sinisterType(claimDTO.getSinisterType())
                .hospitalizationStartDate(claimDTO.getHospitalizationStartDate())
                .hospitalizationEndDate(claimDTO.getHospitalizationEndDate())
                .compensationAmount(claimDTO.getCompensationAmount())
                .numeroSinistre(generateNumeroSinistre())
                .status(ClaimStatus.EN_COURS)
                .insured(insured)
                .build();
        Claim savedClaim = claimRepository.save(claim);
        claimDocumentService.uploadClaimDocuments(savedClaim.getId(),files, types);
        List<ClaimDocumentDTO> claimDocumentDTO = claimDocumentMapper.toDTOList(claim.getClaimDocuments());
        Claim fullClaim = claimRepository.findById(savedClaim.getId())
                .orElseThrow(() -> new RuntimeException("Claim not found after save"));
        return claimMapper.toDTO(fullClaim);
    }

    public List<ClaimDTO> createAllClaims(CreateAllClaimsDTO dto, List<MultipartFile> files, List<ClaimDocumentType> types,List<String> claimTypes ) {
        User currentUser = userService.getCurrentUser();
        Insured insured = insuredRepository.findById(dto.getInsuredId())
                .orElseThrow(()->new ResourceNotFoundException("Assure n'existe pas"));
        Contract contract = insured.getContract();
        if(contract.getStatus()!=StatusContract.ACTIVE){
            throw  new ResourceNotFoundException("Pas de sinistre car contrat n'est pas actif");
        }
        List<ClaimDTO> claimDTOList = dto.getClaims();
        Map<String,List<DocumentPayload>> docsByClaimType =getDocumentsByClaimType(files,types, claimTypes);
        claimDTOList.forEach((claimDTO -> {
            System.out.println(claimDTO.getSinisterType());
            Claim newClaim = new Claim();
            newClaim.setInsured(insured);
            newClaim.setUser(currentUser);
            newClaim.setCreatedBy(currentUser);
            newClaim.setValidatedBy(currentUser);
            newClaim.setNumeroSinistre(generateNumeroSinistre());
            newClaim.setStatus(ClaimStatus.EN_COURS);
            newClaim.setHospitalizationStartDate(claimDTO.getHospitalizationStartDate());
            newClaim.setHospitalizationEndDate(claimDTO.getHospitalizationEndDate());
            if(claimDTO.getHospitalizationStartDate()!=null && claimDTO.getHospitalizationEndDate()!=null){
                newClaim.setNumberNuitsHospitalisation(getNuitsHospitalisation(claimDTO.getHospitalizationStartDate(), claimDTO.getHospitalizationEndDate()));
            }

            newClaim.setSinisterType(claimDTO.getSinisterType());
            Claim savedClaim = claimRepository.save(newClaim);
            ClaimDTO savedClaimDTO = claimMapper.toDTO(savedClaim);
            notificationService.notifyNewClaim(savedClaimDTO);
            claimHistoryService.saveHistory(
                    savedClaim,
                    currentUser,
                    ClaimStatus.EN_COURS,
                    "Création du sinistre"
            );

            List<DocumentPayload> documents = docsByClaimType
                    .getOrDefault(savedClaim.getSinisterType().name(), List.of());
            for(DocumentPayload doc: documents){
                claimDocumentService.uploadSingleDocument(savedClaim.getId(), doc.file(),doc.type());
            }
        }));
        List<Claim> claims = claimRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return claims.stream().map(claimMapper::toDTO).toList();
    }


    public ClaimDTO getClaimById(Long claimId){
        Claim existingClaim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Sinistre avec cette  (id=" + claimId + ") est introuvable"));
        return claimMapper.toDTO(existingClaim);
    }

    public void deleteClaimById(Long claimId){
        Claim existingClaim = claimRepository.findById(claimId)
                .orElseThrow(()->new ResourceNotFoundException("Sinistre avec cette  (id=" + claimId + ") est introuvable"));
        if(existingClaim.getStatus()==ClaimStatus.EN_COURS){
            claimRepository.delete(existingClaim);
        }

    }

    public List<ClaimDTO> getAllClaims(){
        User currentUser = userService.getCurrentUser();
        RoleEnum roleAdmin = currentUser.getRole().getName();
        List<Claim> claims;
        if(roleAdmin.equals(RoleEnum.ADMIN)){
            claims = claimRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        else {
            claims = claimRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId());

        }

        return claims.stream().map(claimMapper::toDTO).toList();
    }

    public String generateNumeroSinistre1(){
        Long seq = claimRepository.getNextSequence();
        return "SIN-" + Year.now().getValue()+ "-" +String.format("%06d",seq);
    }

    private String generateNumeroSinistre(){
        int year = LocalDate.now().getYear();
        int nextNumber = 1;
        Optional<Claim> lastClaim = claimRepository.findTopByOrderByIdDesc();
        if(lastClaim.isPresent()){
            String lastInvoiceNumber = lastClaim.get().getNumeroSinistre();
            String[] parts = lastInvoiceNumber.split("-");
            int lastSequence = Integer.parseInt(parts[2]);
            nextNumber = lastSequence+1;
        }

        return  String.format("SIN-%d-%05d", year, nextNumber);

    }


    public Long getNuitsHospitalisation(LocalDate startDate, LocalDate endDate){
        return (ChronoUnit.DAYS.between(startDate,endDate)-1);
    }

    public Double getMontantVerse(ClaimDTO claimDTO, Contract contract){
        Double montantVerse=0.0;
        if(claimDTO.getSinisterType()==GarantieEnum.HOSPICASH){
            Long nightHospicash = getNuitsHospitalisation(claimDTO.getHospitalizationStartDate(),claimDTO.getHospitalizationEndDate());
            montantVerse += GlobalConstants.MONTANT_VERSEMENT_PAR_NUIT*nightHospicash;
        }
        if(claimDTO.getSinisterType()==GarantieEnum.INVALIDITE){
            montantVerse+= claimDTO.getCompensationAmount();
        }
        if(claimDTO.getSinisterType()==GarantieEnum.CAPITAL_FUNERAIRE){
            montantVerse = GlobalConstants.CAPITAL_MAX-contract.getCapitalDejaVerse();
        }
        return  montantVerse;
    }

    public Map<String, List<DocumentPayload>> getDocumentsByClaimType(List<MultipartFile> files,List<ClaimDocumentType> types, List<String> claimTypes){
        Map<String,List<DocumentPayload>> docsByClaimType =new HashMap<>();
        for(int i=0; i<files.size(); i++){
            docsByClaimType
                    .computeIfAbsent(claimTypes.get(i), k-> new ArrayList<>())
                    .add( new DocumentPayload(
                            files.get(i),
                            types.get(i)
                    ));

        }
        return docsByClaimType;

    }

    // get latest 5 claims for currentUser
    public List<ClaimDTO> getLatest5claimsForCurrentUser(){
        User currentUser = userService.getCurrentUser();
        List<Claim> listClaims = claimRepository.findTop5ByUserIdOrderByCreatedAtDesc(currentUser.getId());
        return listClaims.stream().map(claimMapper::toDTO).toList();
    }

    public Long getNbOpenClaim(){
        User currentUser = userService.getCurrentUser();
        RoleEnum roleAdmin = currentUser.getRole().getName();
        Long nbOpenClaims ;
        if(roleAdmin.equals(RoleEnum.ADMIN)){
            nbOpenClaims = claimRepository.countOpenClaims();
        }
        else {
            nbOpenClaims = claimRepository.countOpenClaimsForCurrentUser(currentUser.getId());
        }
        return nbOpenClaims;
    }

    public Long getNbAcceptedClaim(){
        User currentUser = userService.getCurrentUser();
        RoleEnum roleAdmin = currentUser.getRole().getName();
        Long nbAcceptedClaims;
        if(roleAdmin.equals(RoleEnum.ADMIN)){
            nbAcceptedClaims = claimRepository.countAcceptedClaims();
        }
        else {
            nbAcceptedClaims = claimRepository.countAcceptedClaimsForCurrentUser(currentUser.getId());
        }
        return nbAcceptedClaims;
    }

    public ClaimDTO validateClaim(Long claimId){
        System.out.println("CLAIMID" +claimId);
        User currentUser = userService.getCurrentUser();
        RoleEnum roleAdmin = currentUser.getRole().getName();
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Sinistre not found"));
            claim.setStatus(ClaimStatus.ACCEPTE);
            claim.setValidatedBy(currentUser);
            claim.setValidatedAt(LocalDateTime.now());
            Claim savedClaim = claimRepository.save(claim);
        claimHistoryService.saveHistory(
                savedClaim,
                currentUser,
                ClaimStatus.ACCEPTE,
                "Sinistre validé"
        );
            return claimMapper.toDTO(savedClaim);
    }

    public ClaimDTO rejectClaim(Long claimId,String rejectReason){
        User currentUser = userService.getCurrentUser();
        System.out.println("CLAIMID" +claimId);
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Sinistre not found++"));
        claim.setStatus(ClaimStatus.REJETE);
        claim.setRejectedAt(LocalDateTime.now());
        claim.setRejectedBy(currentUser);
        claim.setRejectReason(rejectReason);
        Claim savedClaim = claimRepository.save(claim);
        ClaimHistoryDTO newHistory = claimHistoryService.saveHistory(
                savedClaim,
                currentUser,
                ClaimStatus.ACCEPTE,
                "Sinistre rejeté"
        );

        return claimMapper.toDTO(savedClaim);
    }

    public ClaimDTO paidClaim(Long claimId){
        User currentUser = userService.getCurrentUser();
        System.out.println("CLAIMID" +claimId);
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Sinistre not found++"));
        if(claim.getStatus().equals(ClaimStatus.ACCEPTE)){
            claim.setStatus(ClaimStatus.PAYE);
            claim.setPaidAt(LocalDateTime.now());
            claim.setPaidBy(currentUser);
            validateContractForClaim(claimId);
            Claim savedClaim = claimRepository.save(claim);
            claimHistoryService.saveHistory(
                    savedClaim,
                    currentUser,
                    ClaimStatus.ACCEPTE,
                    "Sinistre payé"
            );
            return claimMapper.toDTO(savedClaim);

        }
        else{
            throw new RuntimeException("Sinistre must be accepted");
        }

    }

    public void validateContractForClaim(Long claimId){
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(()-> new ResourceNotFoundException("Sinistre not found++"));
        Contract contract = claim.getInsured().getContract();


        if(contract.getCapitalDejaVerse()>=contract.getCapitalMax()){
            throw  new ResourceNotFoundException("Capital MAX est atteint");
        }

        if(claim.getSinisterType()== GarantieEnum.HOSPICASH){

            if(contract.getNuitsRestantes()<=0 || getNuitsHospitalisation(claim.getHospitalizationStartDate(),claim.getHospitalizationEndDate())>=30){
                throw  new ResourceNotFoundException("Plafond nuit hospitalisation atteint");
            }
            Double montantVerse = GlobalConstants.MONTANT_VERSEMENT_PAR_NUIT*getNuitsHospitalisation(claim.getHospitalizationStartDate(),claim.getHospitalizationEndDate());
            contract.setCapitalDejaVerse(contract.getCapitalDejaVerse()+montantVerse);
            contract.setNuitsRestantes(contract.getNuitsRestantes()-getNuitsHospitalisation(claim.getHospitalizationStartDate(),claim.getHospitalizationEndDate()));
            contractRepository.save(contract);
        }
        if(claim.getSinisterType()==GarantieEnum.INVALIDITE){
            if(contract.getCapitalDejaVerse()>=contract.getCapitalMax()){
                throw  new ResourceNotFoundException("Capital MAX est atteint");
            }
            if(claim.getCompensationAmount()>= GlobalConstants.CAPITAL_MAX){
                throw  new ResourceNotFoundException("Montant MAX dépassé");
            }
            contract.setCapitalDejaVerse(contract.getCapitalDejaVerse()+claim.getCompensationAmount());
            contractRepository.save(contract);
        }

        if(claim.getSinisterType()==GarantieEnum.CAPITAL_FUNERAIRE){
            if(contract.getCapitalDejaVerse()>=contract.getCapitalMax()){
                throw  new ResourceNotFoundException("Capital MAX est atteint");
            }
            claim.setCompensationAmount(contract.getCapitalMax()-contract.getCapitalDejaVerse());
            contract.setCapitalDejaVerse(contract.getCapitalDejaVerse()+claim.getCompensationAmount());
            contractRepository.save(contract);
        }



    }

}
