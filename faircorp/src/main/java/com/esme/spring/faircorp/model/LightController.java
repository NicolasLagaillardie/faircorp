package com.esme.spring.faircorp.model;

import com.esme.spring.faircorp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController  // (1)
@CrossOrigin(origins = { "http://localhost:3010" }, maxAge = 3600)
@RequestMapping("/api/lights") // (2)
@Transactional // (3)
public class LightController {

    @Autowired
    private LightDao lightDao; // (4)
    @Autowired
    private RoomDao roomDao;


    @GetMapping // (5)
    public List<LightDto> findAll() {
        return lightDao.findAll()
                .stream()
                .map(LightDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public LightDto findById(@PathVariable Long id) {
        return lightDao.findById(id).map(light -> new LightDto(light)).orElse(null);
    }

    @PutMapping(path = "/{id}/switch")
    public LightDto switchStatus(@PathVariable Long id) {
        Light light = lightDao.findById(id).orElseThrow(IllegalArgumentException::new);
        light.setStatus(light.getStatus() == Status.ON ? Status.OFF: Status.ON);
        return new LightDto(light);
    }

    @PostMapping
    public LightDto create(@RequestBody LightDto dto) {
        Light light = null;
        if (dto.getId() != null) {
            light = lightDao.findById(dto.getId()).orElse(null);
        }

        if (light == null)
            light = lightDao.save(new Light(roomDao.getOne(dto.getRoomId()), dto.getLevel(), dto.getStatus()));
        else {
            light.setLevel(dto.getLevel());
            light.setStatus(dto.getStatus());
            lightDao.save(light);
        }

        return new LightDto(light);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        lightDao.deleteById(id);
    }
}