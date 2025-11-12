package com.example.demo.controller;

import com.example.demo.dto.BusinessTypeResponse;
import com.example.demo.dto.BusinessTypeRequest;
import com.example.demo.service.BusinessTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/BusinessType")
@CrossOrigin(origins = "http://localhost:4200")

public class BusinessTypeController {
    private final BusinessTypeService service;
    public BusinessTypeController(BusinessTypeService service) { this.service = service; }

    @GetMapping
    public List<BusinessTypeResponse> lista() {
        return service.lista();
    }

    @GetMapping("/{id}")
    public BusinessTypeResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public BusinessTypeResponse create(@RequestBody BusinessTypeRequest request) {
        return service.create(request);
    }
    @PutMapping("/{id}")
    public BusinessTypeResponse update(@PathVariable Long id, @RequestBody BusinessTypeRequest request) {
        return service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}