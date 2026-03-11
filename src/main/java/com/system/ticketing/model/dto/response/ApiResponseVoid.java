package com.system.ticketing.model.dto.response;


public class ApiResponseVoid {
    public static final String RETRIEVE_SUCCESS = "Tickets retrieved successfully";
    public static final String UPDATE_SUCCESS = "Tickets updated successfully";
    public static final String UPDATE_FAILED = "No tickets were updated";
    public static final String DELETE_SUCCESS = "Tickets deleted successfully";
    public static final String CREATE_SUCCESS = "Tickets created successfully";
    public static final String NOT_FOUND = "No tickets found with the given ID.";
    public static final String FILTER_FAILED = "No tickets found with given filters";

    public static final String STATUS_OK = "200 OK";
    public static final String STATUS_CREATED = "201 CREATED";
    public static final String STATUS_NOT_FOUND = "404 NOT FOUND";
}
