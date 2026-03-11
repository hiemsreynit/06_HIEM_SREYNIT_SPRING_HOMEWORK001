package com.system.ticketing.controller;


import com.system.ticketing.model.dto.response.ApiResponseListTicket;
import com.system.ticketing.model.dto.response.ApiResponseTicket;
import com.system.ticketing.model.dto.TicketStatus;
import com.system.ticketing.model.dto.request.BulkTicketUpdateRequest;
import com.system.ticketing.model.dto.response.ApiResponseVoid;
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
                    "Phnom Penh", "Kampong Cham", 15.0, true, TicketStatus.BOOKED, "K01"),
            new Tickets(4L, "Vannak Meas", LocalDate.now().plusDays(1),
                    "Phnom Penh", "Battambang", 18.0, true, TicketStatus.BOOKED, "B05"),
            new Tickets(5L, "Sreyneath Chan", LocalDate.now().plusDays(1),
                    "Phnom Penh", "Sihanoukville", 14.5, true, TicketStatus.COMPLETED, "V12"),
            new Tickets(6L, "Borey Sok", LocalDate.now().plusDays(2),
                    "Siem Reap", "Phnom Penh", 25.0, true, TicketStatus.BOOKED, "S02"),
            new Tickets(7L, "Dara Heng", LocalDate.now().plusDays(2),
                    "Phnom Penh", "Kep", 13.0, true, TicketStatus.CANCELLED, "K08"),
            new Tickets(8L, "Chanthy Ly", LocalDate.now().plusDays(3),
                    "Kampot", "Phnom Penh", 12.0, true, TicketStatus.BOOKED, "A02")
    ));

    private final AtomicLong TICKET_ID = new AtomicLong(9L);


    @GetMapping
    public ResponseEntity<ApiResponseListTicket> getAllTicket(
            @RequestParam(defaultValue = "5", required = false) int pageSize,
            @RequestParam(defaultValue = "1", required = false) int pageNumber
    ) {
        int start = (pageNumber - 1) * pageSize;
        int end = start + pageSize;
        if (TICKETS_LIST.size() < end) {
            end = TICKETS_LIST.size();
        }
        List<Tickets> responseTickets = TICKETS_LIST.subList(start, end);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponseListTicket(
                true,
                ApiResponseVoid.CREATE_SUCCESS,
                ApiResponseVoid.STATUS_OK,
                responseTickets,
                Instant.now()
        ));

    }


    @GetMapping("{ticket-id}")
    public ResponseEntity<ApiResponseTicket> getById9(@PathVariable("ticket-id") Long ticketId) {
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ResponseEntity.ok(new ApiResponseTicket(
                        true,
                        ApiResponseVoid.RETRIEVE_SUCCESS,
                        ApiResponseVoid.STATUS_OK,
                        ticket,
                        Instant.now()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseTicket(
                false,
                ApiResponseVoid.NOT_FOUND,
                ApiResponseVoid.STATUS_NOT_FOUND,
                null,
                Instant.now()));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponseTicket> getByPassengerName(@RequestParam String targetName) {
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getPassengerName().equals(targetName)) {
                ApiResponseTicket response = new ApiResponseTicket(
                        true,
                        ApiResponseVoid.RETRIEVE_SUCCESS,
                        ApiResponseVoid.STATUS_OK,
                        ticket,
                        Instant.now()
                );
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.ok(new ApiResponseTicket(
                true,
                ApiResponseVoid.RETRIEVE_SUCCESS,
                ApiResponseVoid.STATUS_OK,
                null,
                Instant.now()));
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

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseTicket(
                true,
                ApiResponseVoid.CREATE_SUCCESS,
                ApiResponseVoid.STATUS_CREATED,
                newTicket, Instant.now()));
    }


    @PostMapping("/bulk")
    public ResponseEntity<ApiResponseListTicket> createMultiTickets(@RequestBody List<TicketsRequest> requests) {
        ArrayList<Tickets> newTickets = new ArrayList<>();
        for (TicketsRequest request : requests) {
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
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseListTicket(
                        true,
                        ApiResponseVoid.CREATE_SUCCESS,
                        ApiResponseVoid.STATUS_CREATED,
                        newTickets,
                        Instant.now()));
    }


    @DeleteMapping("/{ticket-id}")
    public ResponseEntity<ApiResponseTicket> deleteById(@PathVariable("ticket-id") Long targetId) {
        for (Tickets ticket : TICKETS_LIST) {
            if (ticket.getTicketId().equals(targetId)) {
                TICKETS_LIST.remove(ticket);
                return ResponseEntity.ok(new ApiResponseTicket(
                        true,
                        ApiResponseVoid.DELETE_SUCCESS,
                        ApiResponseVoid.STATUS_OK,
                        null,
                        Instant.now()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseTicket(
                false,
                ApiResponseVoid.NOT_FOUND,
                ApiResponseVoid.STATUS_NOT_FOUND,
                null,
                Instant.now()));
    }


    @PutMapping("/{ticket-id}")
    public ResponseEntity<ApiResponseTicket> updateById(@PathVariable("ticket-id") Long targetId, @RequestBody TicketsRequest request) {
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
                return ResponseEntity.ok(new ApiResponseTicket(
                        true,
                        ApiResponseVoid.UPDATE_SUCCESS,
                        ApiResponseVoid.STATUS_OK,
                        ticket,
                        Instant.now()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseTicket(
                false,
                ApiResponseVoid.NOT_FOUND,
                ApiResponseVoid.STATUS_NOT_FOUND,
                null,
                Instant.now()));
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
        return ResponseEntity.ok(new ApiResponseListTicket(
                true,
                ApiResponseVoid.UPDATE_SUCCESS,
                ApiResponseVoid.STATUS_OK,
                responseTickets,
                Instant.now()));
    }

}

