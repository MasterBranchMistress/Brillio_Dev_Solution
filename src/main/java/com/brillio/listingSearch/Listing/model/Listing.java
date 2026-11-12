package com.brillio.listingSearch.Listing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Listing {

    private String id;
    private String source;
    private String address;
    private String city;
    private String state;
    private String zip;

    private double price;
    private int bedrooms;
    private double bathrooms;
    private int sqft;

    private double latitude;
    private double longitude;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate listedDate;

    private String status;
    private String description;
}

