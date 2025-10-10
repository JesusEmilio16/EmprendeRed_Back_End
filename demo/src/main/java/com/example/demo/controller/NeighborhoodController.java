package com.example.demo.controller;

import com.example.demo.dto.NeighborhoodResponse;
import com.example.demo.dto.NeighborhoodRequest;
import com.example.demo.service.NeighborhoodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Neighborhood")

public class NeighborhoodController {
    private final NeighborhoodService service;
    public NeighborhoodController(NeighborhoodService service) { this.service = service; }

    @GetMapping
    public List<NeighborhoodResponse> lista() {
        return service.lista();
    }

    @GetMapping("/{id}")
    public NeighborhoodResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public NeighborhoodResponse create(@RequestBody NeighborhoodRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public NeighborhoodResponse update(@PathVariable Long id, @RequestBody NeighborhoodRequest request) {
        return service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
