package com.system.ticketing.model.dto.request;

import com.system.ticketing.model.dto.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsRequest {
    private String passengerName;
    private LocalDate travelDate;
    private String sourceStation;
    private String destination;
    private Double price;
    private Boolean paymentStatus;
    private TicketStatus ticketStatus;
    private String seatNumber;
}
