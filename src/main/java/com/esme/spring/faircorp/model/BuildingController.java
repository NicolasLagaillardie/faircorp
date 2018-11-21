package com.esme.spring.faircorp.model;

import com.esme.spring.faircorp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController  // (1)
@CrossOrigin(origins = { "http://localhost:3010" }, maxAge = 3600)
@RequestMapping("/api/buildings") // (2)
@Transactional // (3)
public class BuildingController {

    @Autowired
    private LightDao lightDao; // (4)
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private BuildingDao buildingDao;


    @GetMapping // (5)
    public List<BuildingDto> findAll() {
        return buildingDao.findAll()
                .stream()
                .map(BuildingDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public BuildingDto findById(@PathVariable Long id) {
        return buildingDao.findById(id).map(building -> new BuildingDto(building)).orElse(null);
    }

    @PutMapping(path = "/{id}/switchLight")
    public BuildingDto switchStatus(@PathVariable Long id) {
        Building building = buildingDao.findById(id).orElseThrow(IllegalArgumentException::new);
        List<Light> Lights = building.getLights();
        for (int i = 0; i < Lights.size(); i++) {
            Lights.get(i).setStatus(Lights.get(i).getStatus() == Status.ON ? Status.OFF: Status.ON);
        }
        return new BuildingDto(building);
    }

    @PostMapping
    public BuildingDto create(@RequestBody BuildingDto dto) {
        Building building = null;
        if (dto.getId() != null) {
            building = buildingDao.findById(dto.getId()).orElse(null);
        }

        if (building == null)
            building = buildingDao.save(new Building(dto.getName()));
        else {
            building.setName(dto.getName());
            buildingDao.save(building);
        }

        return new BuildingDto(building);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        Building building = buildingDao.findById(id).orElseThrow(IllegalArgumentException::new);

        List<Room> rooms = building.getRooms();
        for (int i = 0; i < rooms.size(); i++) {
            roomDao.deleteById(rooms.get(i).getId());
        }

        buildingDao.deleteById(id);
    }
}
