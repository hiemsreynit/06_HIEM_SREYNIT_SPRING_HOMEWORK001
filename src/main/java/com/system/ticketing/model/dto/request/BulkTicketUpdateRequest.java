package com.system.ticketing.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkTicketUpdateRequest {
    private List<Long> ticketId;
    private Boolean paymentStatus;

}
