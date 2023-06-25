package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dtos.ParkingSpotDTO;
import com.api.parkingcontrol.model.ParkingSpot;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/parking-spot")
public class ParkingSpotController {
    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO) {
        if(parkingSpotService.existsByLicensePlate(parkingSpotDTO.getLicensePlate())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("License plate already exists");
        }
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Parking spot number already exists");
        }
        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Apartment and block already exists");
        }

        var parkingSpot = new ParkingSpot();
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpot);
        parkingSpot.setEntryDate(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.saveParkingSpot(parkingSpot));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpot>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(parkingSpotService.getAllParkingSpots(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getParkingSpotById(@PathVariable UUID id) {
        Optional<ParkingSpot> parkingSpotOptional = (Optional<ParkingSpot>) parkingSpotService.getParkingSpotById(id);
        if(parkingSpotOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");
        } return ResponseEntity.ok(parkingSpotService.getParkingSpotById(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpotById(@PathVariable UUID id) {
        Optional<ParkingSpot> parkingSpotOptional = (Optional<ParkingSpot>) parkingSpotService.getParkingSpotById(id);
        if(parkingSpotOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");
        } parkingSpotService.deleteParkingSpotById(id);
        return ResponseEntity.ok("Parking spot deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpotById(@PathVariable UUID id, @RequestBody @Valid ParkingSpotDTO parkingSpotDTO) {
        Optional<ParkingSpot> parkingSpotOptional = (Optional<ParkingSpot>) parkingSpotService.getParkingSpotById(id);
        if(parkingSpotOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");
        }

        var parkingSpot = parkingSpotOptional.get();
        parkingSpot.setParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber());
        parkingSpot.setLicensePlate(parkingSpotDTO.getLicensePlate());
        parkingSpot.setCarBrand(parkingSpotDTO.getCarBrand());
        parkingSpot.setCarModel(parkingSpotDTO.getCarModel());
        parkingSpot.setCarColor(parkingSpotDTO.getCarColor());
        parkingSpot.setClientName(parkingSpotDTO.getClientName());
        parkingSpot.setApartment(parkingSpotDTO.getApartment());
        parkingSpot.setBlock(parkingSpotDTO.getBlock());

        return ResponseEntity.ok(parkingSpotService.saveParkingSpot(parkingSpot));

    }
}
