package com.esme.spring.faircorp.model;

import com.esme.spring.faircorp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController  // (1)
@CrossOrigin(origins = { "http://localhost:3010" }, maxAge = 3600)
@RequestMapping("/api/rooms") // (2)
@Transactional // (3)
public class RoomController {

    @Autowired
    private LightDao lightDao; // (4)
    @Autowired
    private RoomDao roomDao;


    @GetMapping // (5)
    public List<RoomDto> findAll() {
        return roomDao.findAll()
                .stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public RoomDto findById(@PathVariable Long id) {
        return roomDao.findById(id).map(room -> new RoomDto(room)).orElse(null);
    }

    @PutMapping(path = "/{id}/switchLight")
    public RoomDto switchStatus(@PathVariable Long id) {
        Room room = roomDao.findById(id).orElseThrow(IllegalArgumentException::new);
        List<Light> Lights = room.getLights();
        for (int i = 0; i < Lights.size(); i++) {
            Lights.get(i).setStatus(Lights.get(i).getStatus() == Status.ON ? Status.OFF: Status.ON);
        }
        return new RoomDto(room);
    }

    @PostMapping
    public RoomDto create(@RequestBody RoomDto dto) {
        Room room = null;
        if (dto.getId() != null) {
            room = roomDao.findById(dto.getId()).orElse(null);
        }

        if (room == null)
            room = roomDao.save(new Room(dto.getName(), dto.getFloor()));
        else {
            room.setFloor(dto.getFloor());
            room.setName(dto.getName());
            roomDao.save(room);
        }

        return new RoomDto(room);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        Room room = roomDao.findById(id).orElseThrow(IllegalArgumentException::new);

        List<Light> lights = room.getLights();
        for (int i = 0; i < lights.size(); i++) {
            lightDao.deleteById(lights.get(i).getId());
        }

        roomDao.deleteById(id);
    }
}
