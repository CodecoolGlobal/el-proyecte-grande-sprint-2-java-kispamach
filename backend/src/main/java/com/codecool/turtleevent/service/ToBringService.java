package com.codecool.turtleevent.service;

import com.codecool.turtleevent.model.Event;
import com.codecool.turtleevent.model.ToBring;
import com.codecool.turtleevent.model.dto.IdDTO;
import com.codecool.turtleevent.model.dto.RestResponseDTO;
import com.codecool.turtleevent.model.dto.ToBringDTO;
import com.codecool.turtleevent.repository.ToBringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToBringService {

    private ToBringRepository toBringRepository;

    private EventService eventService;
    private BringerService bringerService;

    @Autowired
    public void setToBringRepository(ToBringRepository toBringRepository) {
        this.toBringRepository = toBringRepository;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setBringerService(BringerService bringerService) {
        this.bringerService = bringerService;
    }

    public ToBring findById(Long id){
        Optional<ToBring> toBring = toBringRepository.findById(id);
        return toBring.orElse(null);
    }

    public List<ToBringDTO> getAll() {
        List<ToBring> toBrings = toBringRepository.findAll();
        List<ToBringDTO> toBringDTOList = toBrings.stream()
                .map(toBring -> new ToBringDTO(toBring.getId(),
                        toBring.getEvent().getId(),
                        toBring.getTitle(),
                        toBring.getComment(),
                        toBring.getSubAmount(),
                        toBring.getBringers().stream().map(t->t.getId()).collect(Collectors.toSet())))
                .collect(Collectors.toList());
        return toBringDTOList;
    }


    public List<ToBring> getAllByEvent(IdDTO eventId) {
        Event event = eventService.findEventById(eventId.getId());
        if (event != null) return toBringRepository.findAllByEvent(event);
        return null;
    }


    public RestResponseDTO add(ToBringDTO toBring) {
        try {
            Event event = eventService.findEventById(toBring.getEventId());
            ToBring newToBring = new ToBring(event, toBring.getTitle(), toBring.getComment(), 0, LocalDateTime.now());
            toBringRepository.save(newToBring);
            return new RestResponseDTO(true, "'to bring' added successfully!");
        } catch (Exception e) {
            return new RestResponseDTO(false, "Failed to add!");
        }
    }


    @Transactional
    public RestResponseDTO update(ToBring newToBring) {
        Optional<ToBring> toBring = toBringRepository.findById(newToBring.getId());
        if(toBring.isPresent()) {
            if(newToBring.getTitle() != null) toBring.get().setTitle(newToBring.getTitle());
            if(newToBring.getComment() != null) toBring.get().setComment(newToBring.getComment());
            // Do we get null or 0?
            if(newToBring.getTotalAmount() != 0) toBring.get().setTotalAmount(newToBring.getTotalAmount());
            return new RestResponseDTO(true, "'To bring' updated!");
            }
        return new RestResponseDTO(false, "'To bring' failed to update!");
    }

    public RestResponseDTO delete(Long id) {
        Optional<ToBring> toBring = toBringRepository.findById(id);
        if(toBring.isPresent()) {
            toBringRepository.delete(toBring.get());
            return new RestResponseDTO(true, "'To bring' deleted successfully!");
        }
        return new RestResponseDTO(false, "Failed to delete 'To bring'!");
    }
}
