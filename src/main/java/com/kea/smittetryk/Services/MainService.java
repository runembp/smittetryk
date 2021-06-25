package com.kea.smittetryk.Services;

import com.kea.smittetryk.Models.DTO.MunicipalityDTO;
import com.kea.smittetryk.Models.DTO.ParishDTO;
import com.kea.smittetryk.Models.Municipality;
import com.kea.smittetryk.Models.Parish;
import com.kea.smittetryk.Repositories.MunicipalityRepository;
import com.kea.smittetryk.Repositories.ParishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The "factory" class of the Application. The Service layer handles all the manipulation of the data, if any is need.
 * Otherwise it simple forwards data to and from the Repository or Controller layer, so separation of concerns is always upheld.
 */
@Service
@AllArgsConstructor
public class MainService
{
    private final ParishRepository parishRepository;
    private final MunicipalityRepository municipalityRepository;

    /**
     * Returns a list of all the existing Parishes from the database
     * @return
     */
    public List<Parish> getAllParish()
    {
        return parishRepository.findAll();
    }

    /**
     * Creates a new Parish, with a relation to a Municipality, obtained through the DTO properties.
     * @param newParish
     */
    public void createParish(ParishDTO newParish)
    {
        var municipality = municipalityRepository.findById(newParish.getMunicipalityId()).get();
        var parish = parishRepository.save(new Parish(newParish.getParishCode(), newParish.getParishName(), newParish.getParishInfectionPercent()));

        parish.setMunicipality(municipality);
        parishRepository.save(parish);

        municipality.getParishes().add(parish);
        municipalityRepository.save(municipality);
    }

    /**
     * Deletes a Parish based on the parish id obtained in the parameter.
     * @param parishId
     */
    public void deleteParish(Long parishId)
    {
        parishRepository.deleteById(parishId);
    }

    /**
     * Recieves a list of municipalities through the parameters, which is added to the database. The if-statement is made
     * to avoid both duplicated data and extra database operations. There is only ever 99 municipals in Denmark and the
     * if the Database contains either less or more than this number, all municipalities are deleted and order is restored.
     * @param municipalities
     */
    public void addMunicipalities(List<Municipality> municipalities)
    {
        if(municipalityRepository.findAll().size() != 99)
        {
            municipalityRepository.deleteAll();
            municipalityRepository.saveAll(municipalities);
        }
    }

    /**
     * Returns a parish based on the parish id in the parameter.
     * @param parishId
     * @return
     */
    public Parish getParishById(String parishId)
    {
        return parishRepository.findById(Long.parseLong(parishId)).get();
    }

    /**
     * Updates a parish with data obtained from the ParishDTO. A ParishDTO always contains the used properties, as it is
     * made through a form where all the forms are annotated with required.
     * @param updatedParish
     */
    public void updateParish(ParishDTO updatedParish)
    {
        var parish = parishRepository.findById(updatedParish.getParishId()).get();
        parish.setParishCode(updatedParish.getParishCode());
        parish.setParishName(updatedParish.getParishName());
        parish.setInfectionPercent(updatedParish.getParishInfectionPercent());
        parish.setShutdownDate(updatedParish.getParishShutdownDate());
        parishRepository.save(parish);
    }

    /**
     * Returns a list of MunicipalityDTOs.
     * A list of all municipalities is obtained from the database, and then all the municipalities with no associated Parishes
     * are filtered out, leaving only populated Municipalities.
     * The sum of the infectionpercent is calculated through a stream of every parish to the iterated municipality.
     * @return
     */
    public List<MunicipalityDTO> getMunicipalityInfo()
    {
        var municipalityList = municipalityRepository.findAll().stream().filter(municipality -> !municipality.getParishes().isEmpty()).toList();
        var dtoList = new ArrayList<MunicipalityDTO>();

        municipalityList.forEach(municipality ->
        {
            var dto = new MunicipalityDTO();

            dto.setMunicipalityName(municipality.getMunicipalityName());
            dto.setParishes(municipality.getParishes());
            dto.setTotalInfectionPercent(municipality.getParishes().stream().mapToDouble(Parish::getInfectionPercent).sum() / municipality.getParishes().size());

            dtoList.add(dto);
        });

        return dtoList;
    }

    /**
     * Quality of life implementation of Demo data. 10 Parishes are created each added to a relation to a Municipality,
     * with a infection percent and shutdown date set.
     */
    public void addDemoData()
    {
        parishRepository.deleteAll();

        var p1 = new Parish(1,"Fjeldeby",3);
        p1.setShutdownDate(LocalDate.now().minusDays(1));
        p1.setMunicipality(municipalityRepository.findById(1L).get());
        parishRepository.save(p1);

        var p2 = new Parish(2,"Nørre Vissing",12);
        p2.setShutdownDate(LocalDate.now().plusDays(1));
        p2.setMunicipality(municipalityRepository.findById(1L).get());
        parishRepository.save(p2);

        var p3 = new Parish(3,"Gyllen",2);
        p3.setShutdownDate(LocalDate.now().minusDays(2));
        p3.setMunicipality(municipalityRepository.findById(2L).get());
        parishRepository.save(p3);

        var p4 = new Parish(4,"Bov",27);
        p4.setShutdownDate(LocalDate.now().plusDays(2));
        p4.setMunicipality(municipalityRepository.findById(2L).get());
        parishRepository.save(p4);

        var p5 = new Parish(5,"Klippeby",21);
        p5.setShutdownDate(LocalDate.now().minusDays(3));
        p5.setMunicipality(municipalityRepository.findById(2L).get());
        parishRepository.save(p5);

        var p6 = new Parish(6,"Amalienborg",59);
        p6.setShutdownDate(LocalDate.now().plusDays(3));
        p6.setMunicipality(municipalityRepository.findById(3L).get());
        parishRepository.save(p6);

        var p7 = new Parish(7,"Lemming",3);
        p7.setShutdownDate(LocalDate.now().minusDays(4));
        p7.setMunicipality(municipalityRepository.findById(3L).get());
        parishRepository.save(p7);

        var p8 = new Parish(8,"Store Lille Snede Nørre",5);
        p8.setShutdownDate(LocalDate.now().plusDays(4));
        p8.setMunicipality(municipalityRepository.findById(4L).get());
        parishRepository.save(p8);

        var p9 = new Parish(9,"Lille Store Nørre Snede",6);
        p9.setShutdownDate(LocalDate.now().minusDays(5));
        p9.setMunicipality(municipalityRepository.findById(5L).get());
        parishRepository.save(p9);

        var p10 = new Parish(10,"Storby",12);
        p10.setShutdownDate(LocalDate.now().plusDays(5));
        p10.setMunicipality(municipalityRepository.findById(10L).get());
        parishRepository.save(p10);
    }
}