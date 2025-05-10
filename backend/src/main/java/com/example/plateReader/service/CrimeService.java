package com.example.plateReader.service;

import com.example.plateReader.model.Crime;
import com.example.plateReader.repository.CrimeRepository;
import com.example.plateReader.service.exception.CrimePlateNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrimeService {

    private final CrimeRepository crimeRepository;

    public CrimeService(CrimeRepository crimeRepository) {
        this.crimeRepository = crimeRepository;
    }

    public List<Crime> findAll(){
       return crimeRepository.findAll();
   }

   public Crime findByCriminalRecord_Person_PlateAndId(String plate, Long id){
        return crimeRepository.findByCriminalRecord_Person_PlateAndId(plate, id).orElseThrow(() -> new CrimePlateNotFoundException(id, plate));
   }

   public List<Crime> findByCriminalRecord_Id(Long id){
        return crimeRepository.findByCriminalRecord_Id(id);
   }
}
