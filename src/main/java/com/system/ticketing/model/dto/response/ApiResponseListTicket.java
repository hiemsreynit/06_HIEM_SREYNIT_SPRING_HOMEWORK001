package com.system.ticketing.model.dto.response;

import com.system.ticketing.model.entity.Tickets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseListTicket {
    private Boolean success;
    private String message;
    private String status;
    private List<Tickets> payload;
    private Instant timeStamp;
}
