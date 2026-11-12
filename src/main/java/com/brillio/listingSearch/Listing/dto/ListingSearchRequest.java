package com.brillio.listingSearch.Listing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class ListingSearchRequest {

    @PositiveOrZero
    private Double minPrice;

    @Positive
    private Double maxPrice;

    @Min(0)
    private Integer minBedrooms;

    private String city;

    private String keyword;

    @Positive
    private Double targetBudget;

    @PositiveOrZero
    private Integer page = 0;

    @Positive
    private Integer pageSize = 10;

    @AssertTrue(message = "Minimum price cannot exceed maximum price")
    public boolean isPriceRangeValid() {
        return minPrice == null || maxPrice == null ||
                minPrice <= maxPrice;
    }
}