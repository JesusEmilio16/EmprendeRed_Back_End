package com.example.demo.service;

import com.example.demo.dto.NeighborhoodResponse;
import com.example.demo.dto.NeighborhoodRequest;
import com.example.demo.model.Neighborhood;
import com.example.demo.repository.NeighborhoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NeighborhoodService {
    private final NeighborhoodRepository repo;
    public NeighborhoodService(NeighborhoodRepository repo) { this.repo = repo; }

    //traer datos de neighborhood
    public List<NeighborhoodResponse> lista() {
        return repo.findAll()
                .stream()
                .map(u -> new NeighborhoodResponse(u.getIdNeighborhood(),u.getName(),u.getDescription())
                ).collect(Collectors.toList());
    }

    //crear neighborhood
    public NeighborhoodResponse create(NeighborhoodRequest request) {
        Neighborhood entity = new Neighborhood();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());


        Neighborhood saved = repo.save(entity);
        return new NeighborhoodResponse(saved.getIdNeighborhood(),saved.getName(), saved.getDescription());
    }

    //listar por id
    public NeighborhoodResponse findById(Long idType) {
        Neighborhood entity = repo.findById(idType)
                .orElseThrow(()-> new RuntimeException("neighborhood no encontrado"+ idType) );
        return new NeighborhoodResponse(entity.getIdNeighborhood(), entity.getName(), entity.getDescription());
    }


    //actualizar neighborhood
    public NeighborhoodResponse update(Long idType, NeighborhoodRequest request) {
        Neighborhood entity = repo.findById(idType)
                .orElseThrow(()-> new RuntimeException("neighborhood no encontrado con id"+ idType) );
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());


        Neighborhood saved = repo.save(entity);
        return new NeighborhoodResponse(saved.getIdNeighborhood(), saved.getName(), saved.getDescription());
    }

    public void delete(Long id) {

        if (!repo.existsById(id)){
            throw new RuntimeException("neighborhood no encontrado con id"+ id);
        }
        repo.deleteById(id);
    }
}