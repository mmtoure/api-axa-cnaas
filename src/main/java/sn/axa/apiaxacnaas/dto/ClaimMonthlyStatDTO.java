package sn.axa.apiaxacnaas.dto;

public record ClaimMonthlyStatDTO(
        Integer year,
        Integer month,
        Long total
) {}
