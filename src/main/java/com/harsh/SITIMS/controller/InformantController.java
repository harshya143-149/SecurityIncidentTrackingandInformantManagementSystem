package com.harsh.SITIMS.controller;

import com.harsh.SITIMS.dto.InformantDTO;
import com.harsh.SITIMS.service.InformantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/informants")
@RequiredArgsConstructor
public class InformantController {

    private final InformantService service;

    @PostMapping
    public InformantDTO add(@RequestBody InformantDTO dto) {
        return service.addInformant(dto);
    }

    @GetMapping
    public List<InformantDTO> all() {
        return service.getAllInformants();
    }

    @GetMapping("/{id}")
    public InformantDTO get(@PathVariable Long id) {
        return service.getInformantById(id);
    }

    @PutMapping("/{id}")
    public InformantDTO update(@PathVariable Long id, @RequestBody InformantDTO dto) {
        return service.updateInformant(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteInformant(id);
    }
}
