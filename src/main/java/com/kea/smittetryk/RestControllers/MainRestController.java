package com.kea.smittetryk.RestControllers;

import com.kea.smittetryk.Models.DTO.MunicipalityDTO;
import com.kea.smittetryk.Models.DTO.ParishDTO;
import com.kea.smittetryk.Models.Municipality;
import com.kea.smittetryk.Models.Parish;
import com.kea.smittetryk.Services.MainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The one and only RestController of the Application. It's only job is to forward/receive data from the Service layer to
 * uphold separation of concerns. No manipulation of data is happening on this layer.
 * The /api/ mapping is used to make further distinctions between the Controllers and RestControllers
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class MainRestController
{
    private final MainService mainService;

    @GetMapping("/allParishes")
    public List<Parish> getAllParishes()
    {
        return mainService.getAllParish();
    }

    @GetMapping("/getParishById/{parishId}")
    public Parish getParishById(@PathVariable String parishId)
    {
        return mainService.getParishById(parishId);
    }

    @PostMapping("/create")
    public void createParish(@RequestBody ParishDTO parish)
    {
        mainService.createParish(parish);
    }

    @PostMapping("/updateParish")
    public void updateParish(@RequestBody ParishDTO parish)
    {
        mainService.updateParish(parish);
    }

    @DeleteMapping("/delete/{parishId}")
    public void deleteParish(@PathVariable Long parishId)
    {
        mainService.deleteParish(parishId);
    }

    @PostMapping("/addMunicipalities")
    public void addMunicipalities(@RequestBody List<Municipality> municipalities)
    {
        mainService.addMunicipalities(municipalities);
    }

    @GetMapping("/addDemoData")
    public void addDemoData()
    {
        mainService.addDemoData();
    }

    @GetMapping("/municipalityInfo")
    public List<MunicipalityDTO> getMunicipalityInfo()
    {
        return mainService.getMunicipalityInfo();
    }
}
