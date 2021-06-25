package com.kea.smittetryk.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * A Data Transfer Object made for the purpose of creating and updating Parishes. When a Parish is created, it needs to get a relation to
 * a municipality, and this is done through the municipalityId of the DTO.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParishDTO
{
    private Long parishId;
    private int parishCode;
    private String parishName;
    private double parishInfectionPercent;
    private LocalDate parishShutdownDate;
    private Long municipalityId;
}
