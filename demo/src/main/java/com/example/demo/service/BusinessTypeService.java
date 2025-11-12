package com.example.demo.service;

import com.example.demo.dto.BusinessTypeResponse;
import com.example.demo.dto.BusinessTypeRequest;
import com.example.demo.model.BusinessType;
import com.example.demo.repository.BusinessTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessTypeService {
    private final BusinessTypeRepository repo;
    public BusinessTypeService(BusinessTypeRepository repo) { this.repo = repo; }

    //traer tipo de negocio
    public List<BusinessTypeResponse> lista() {
        return repo.findAll()
                .stream()
                .map(u -> new BusinessTypeResponse(Math.toIntExact(u.getIdType()),u.getName(),u.getDescription())
                ).collect(Collectors.toList());
    }

    //crear tipo de negocio
    public BusinessTypeResponse create(BusinessTypeRequest request) {
        BusinessType entity = new BusinessType();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());


        BusinessType saved = repo.save(entity);
        return new BusinessTypeResponse(Math.toIntExact(saved.getIdType()),saved.getName(), saved.getDescription());
    }

    //listar por id
    public BusinessTypeResponse findById(Long idType) {
        BusinessType entity = repo.findById(idType)
                .orElseThrow(()-> new RuntimeException("Tipo de negocio no encontrado"+ idType) );
        return new BusinessTypeResponse(Math.toIntExact(entity.getIdType()), entity.getName(), entity.getDescription());
    }


    //actualizar tipo de negocio
    public BusinessTypeResponse update(Long idType, BusinessTypeRequest request) {
        BusinessType entity = repo.findById(idType)
                .orElseThrow(()-> new RuntimeException("Tipo no encontrado con id"+ idType) );
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());


        BusinessType saved = repo.save(entity);
        return new BusinessTypeResponse(Math.toIntExact(saved.getIdType()), saved.getName(), saved.getDescription());
    }

    public void delete(Long id) {

        if (!repo.existsById(id)){
            throw new RuntimeException("Tipo no encontrado con id"+ id);
        }
        repo.deleteById(id);
    }
}