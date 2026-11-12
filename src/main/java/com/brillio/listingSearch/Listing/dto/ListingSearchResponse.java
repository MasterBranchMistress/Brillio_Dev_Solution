package com.brillio.listingSearch.Listing.dto;
import com.brillio.listingSearch.Listing.model.Listing;

public record ListingSearchResponse(
        Listing listing,
        double relevanceScore
) {}
