package com.system.ticketing.controller;


import com.system.ticketing.model.dto.ApiResponseListTicket;
import com.system.ticketing.model.dto.ApiResponseTicket;
import com.system.ticketing.model.dto.TicketStatus;
import com.system.ticketing.model.dto.request.BulkTicketUpdateRequest;
import com.system.ticketing.model.entity.Tickets;
import com.system.ticketing.model.dto.request.TicketsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final DateTimeFormatter travelFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final List<Tickets> TICKETS_LIST = new ArrayList<>(List.of(
            new Tickets(1L, "Rithy Sam", LocalDate.now(),
                    "Phnom Penh", "Kampot", 12.0, true, TicketStatus.BOOKED, "A01"),
            new Tickets(2L, "Sophea Nguon", LocalDate.now(),
                    "Phnom Penh", "Siem Reap", 25.0, true, TicketStatus.BOOKED, "S01"),
            new Tickets(3L, "Rithy Sam", LocalDate.now(),
                    "Phnom Penh", "Kampong Cham", 15.0, true, TicketStatus.BOOKED, "K01")
    ));

    private final List<ApiResponseListTicket> TICKETS = new ArrayList<>(List.of(
            new ApiResponseListTicket(true, "Tickets retrieved successfully", "200 OK",TICKETS_LIST, Instant.now())
    ));
    private final AtomicLong TICKET_ID = new AtomicLong(4L);


    @GetMapping
    public ResponseEntity<ApiResponseListTicket> getAllTicket(
            @RequestParam(defaultValue = "10", required = false) int pageSize,
            @RequestParam(defaultValue = "1", required = false) int pageNumber
            ) {
        int start = (pageNumber - 1) * pageSize;
        int end = start + pageSize;
        if(TICKETS_LIST.size() < end){
            end = TICKETS_LIST.size();
        }
        List<Tickets> responseTickets = TICKETS_LIST.subList(start, end);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponseListTicket(true, "Tickets retrieved successfully", "200 OK", responseTickets, Instant.now()));

    }


    @GetMapping("{ticket-id}")
    public ResponseEntity<ApiResponseTicket> getById(@PathVariable("ticket-id") Long targetId) {
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getTicketId().equals(targetId)) {
                ResponseEntity.ok(new ApiResponseTicket(true, "Tickets retrieved successfully", "200 OK", ticket, Instant.now()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseTicket(false, "No tickets found with the given ID.", "404 NOT FOUND", null, Instant.now()));
            }
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponseTicket> getByPassengerName(@RequestParam String targetName) {
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getPassengerName().equals(targetName)) {
                ApiResponseTicket response = new ApiResponseTicket(true, "Tickets fetched successfully", "200 OK", ticket, Instant.now());
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.ok(new ApiResponseTicket(true, "No tickets found with the given ID.", "404 NOT FOUND", null, Instant.now()));
    }


    @PostMapping
    public ResponseEntity<ApiResponseTicket> createOneTicket(@RequestBody TicketsRequest request) {
        Tickets newTicket = new Tickets(
                TICKET_ID.getAndIncrement(),
               request.getPassengerName(),
               request.getTravelDate(),
               request.getSourceStation(),
               request.getDestination(),
               request.getPrice(),
               request.getPaymentStatus(),
               request.getTicketStatus(),
               request.getSeatNumber()
        );
        TICKETS_LIST.add(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseTicket(true, "Tickets created successfully", "201 CREATED", newTicket, Instant.now()));
    }


    @PostMapping("/bulk")
    public ResponseEntity<ApiResponseListTicket> createMultiTickets(@RequestBody List<TicketsRequest> requests) {
        ArrayList<Tickets> newTickets = new ArrayList<>();
        for(TicketsRequest request : requests){
            newTickets.add(
                    new Tickets(
                            TICKET_ID.getAndIncrement(),
                            request.getPassengerName(),
                            request.getTravelDate(),
                            request.getSourceStation(),
                            request.getDestination(),
                            request.getPrice(),
                            request.getPaymentStatus(),
                            request.getTicketStatus(),
                            request.getSeatNumber()
                    )
            );
        }
        TICKETS_LIST.addAll(newTickets);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseListTicket(true, "Tickets created successfully", "201 CREATED", newTickets, Instant.now()));
    }


    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<ApiResponseTicket> deleteById(@PathVariable("ticket-id") Long targetId) {
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getTicketId().equals(targetId)) {
                TICKETS_LIST.remove(ticket);
                return ResponseEntity.ok(new ApiResponseTicket(true, "Tickets deleted successfully", "200 OK", null, Instant.now()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseTicket(false, "No tickets found with the given ID.", "404 NOT FOUND",null, Instant.now()));
    }


    @PutMapping("/{ticket-id}")
    public ResponseEntity<ApiResponseTicket> updateById(@PathVariable("ticket-id") Long targetId, @RequestBody TicketsRequest request){
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getTicketId().equals(targetId)) {
                ticket.setPassengerName(request.getPassengerName());
                ticket.setTravelDate(request.getTravelDate());
                ticket.setSourceStation(request.getSourceStation());
                ticket.setDestination(request.getDestination());
                ticket.setPrice(request.getPrice());
                ticket.setPaymentStatus(request.getPaymentStatus());
                ticket.setTicketStatus(request.getTicketStatus());
                ticket.setSeatNumber(request.getSeatNumber());
                return ResponseEntity.ok(new ApiResponseTicket(true, "Tickets updated successfully", "200 OK", ticket, Instant.now()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseTicket(false, "No tickets found with the given ID.", "404 NOT FOUND", null, Instant.now()));
    }

    @PutMapping("/bulk")
    public ResponseEntity<ApiResponseListTicket> updatePaymentStatus(@RequestBody BulkTicketUpdateRequest updateRequest) {
        List<Tickets> responseTickets = new ArrayList<>();
        for (Long id : updateRequest.getTicketId()) {
            for (Tickets ticket : TICKETS_LIST) {
                if (id.equals(ticket.getTicketId())) {
                    ticket.setPaymentStatus(updateRequest.getPaymentStatus());
                    responseTickets.add(ticket);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponseListTicket(true, "Tickets updated successfully", "202 ACCEPTED", responseTickets, Instant.now()));
    }

}

