package com.kea.smittetryk.Models.DTO;

import com.kea.smittetryk.Models.Parish;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * A Data transfer object made for the specific reason to populate the Municipality-mapping - because of limitations
 * in the relations between Parish and Municipality, I could choose to have either Parish with its Municipal or Municipal
 * with its Parishes. I chose the first, which made this DTO necessary.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MunicipalityDTO
{
    private String municipalityName;
    private double totalInfectionPercent;
    private Set<Parish> parishes;
}
