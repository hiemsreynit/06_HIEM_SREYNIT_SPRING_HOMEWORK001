package com.system.ticketing.model.dto.response;

import com.system.ticketing.model.dto.ApiResponseTicket;
import com.system.ticketing.model.entity.Tickets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseVoid {
    private Boolean success;
    private String message;
    private String status;
    private Tickets payload;
    private String timeStamp;
}
