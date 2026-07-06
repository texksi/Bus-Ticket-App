package com.busticket.app.service;

import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.mapper.GradMapper;
import com.busticket.app.model.dto.request.GradRequestDTO;
import com.busticket.app.model.dto.response.GradResponseDTO;
import com.busticket.app.model.entity.Grad;
import com.busticket.app.repository.GradRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GradService {

    private final GradRepository gradRepository;
    private final GradMapper gradMapper;

    public GradResponseDTO getGradById(Long id){
        Grad grad = gradRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Grad sa tim id-om ne postoji")
        );
        return gradMapper.toResponse(grad);
    }
    
    public GradResponseDTO getGradByNaziv(String naziv){
        Grad grad = gradRepository.findByNaziv(naziv).orElseThrow(
                () -> new EntityNotFoundException("Grad sa tim nazivom ne postoji")
        );
        return gradMapper.toResponse(grad);
    }

    public GradResponseDTO getGradBySkracenica(String skracenica){
        Grad grad = gradRepository.findBySkracenica(skracenica).orElseThrow(
                () -> new EntityNotFoundException("Grad sa tom skracenicom ne postoji")
        );
        return gradMapper.toResponse(grad);
    }
    
    public List<GradResponseDTO> getAllGradovi() {
        List<Grad> all = gradRepository.findAll();
        return all.stream().map(gradMapper::toResponse).toList();
    }

    public GradResponseDTO createGrad(GradRequestDTO dto){
       if(gradRepository.existsByNaziv(dto.getNaziv()) || gradRepository.existsBySkracenica(dto.getSkracenica())){
           throw new EntityAlreadyExistsException("Grad sa ovim nazivom/skracenicom vec postoji");
       }
       Grad saved = gradRepository.save(gradMapper.toEntity(dto));
       return gradMapper.toResponse(saved);
    }
    
    public GradResponseDTO updateGrad(Long id, GradRequestDTO dto){
        Grad grad = gradRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Grad sa tim id-om ne postoji")
        );
        if (!dto.getNaziv().equals(grad.getNaziv()) && gradRepository.existsByNaziv(dto.getNaziv())) {
            throw new EntityAlreadyExistsException("Grad sa ovim nazivom vec postoji");
        }
        if (!dto.getSkracenica().equals(grad.getSkracenica()) && gradRepository.existsBySkracenica(dto.getSkracenica())) {
            throw new EntityAlreadyExistsException("Grad sa ovom skracenicom vec postoji");
        }
        grad.setNaziv(dto.getNaziv());
        grad.setSkracenica(dto.getSkracenica());
        Grad updated = gradRepository.save(grad);
        return gradMapper.toResponse(updated);
    }
    
    public void deleteGrad(Long id){
        gradRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Grad sa tim id-om ne postoji")
        );
        gradRepository.deleteById(id);
    }
}
