package com.api.parkingcontrol.services;

import com.api.parkingcontrol.model.ParkingSpot;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {
    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public Object saveParkingSpot(ParkingSpot parkingSpot) {
        return parkingSpotRepository.save(parkingSpot);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return parkingSpotRepository.existsByLicensePlate(licensePlate);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public Page<ParkingSpot> getAllParkingSpots(Pageable pageable) {
        return parkingSpotRepository.findAll((org.springframework.data.domain.Pageable) pageable);

    }

    public Optional<ParkingSpot> getParkingSpotById(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    public void deleteParkingSpotById(UUID id) {
        parkingSpotRepository.deleteById(id);
    }
}
