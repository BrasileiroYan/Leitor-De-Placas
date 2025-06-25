package com.example.plateReader.service;

import com.example.plateReader.dto.AddressResponseDTO;
import com.example.plateReader.model.Address;
import com.example.plateReader.repository.AddressRepository;
import com.example.plateReader.service.exception.AddressNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<AddressResponseDTO> findAll() {
        return addressRepository.findAll()
                .stream().map(AddressResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AddressResponseDTO findById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));

        return new AddressResponseDTO(address);
    }

}
