package com.system.ticketing.model.dto;


import com.system.ticketing.model.entity.Tickets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseTicket {
    private Boolean success;
    private String message;
    private String status;
    private Tickets payload;
    private Instant timeStamp;
}
