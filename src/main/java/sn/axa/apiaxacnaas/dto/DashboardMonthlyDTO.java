package sn.axa.apiaxacnaas.dto;

public record DashboardMonthlyDTO(
        Integer year,
        Integer month,
        Long totalInsured,
        Long totalClaim

        ) {

}
