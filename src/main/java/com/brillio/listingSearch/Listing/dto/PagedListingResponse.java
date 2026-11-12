package com.brillio.listingSearch.Listing.dto;

import java.util.List;

public record PagedListingResponse(
        List<ListingSearchResponse> content,
        int page,
        int pageSize,
        long totalElements,
        int totalPages
) {}
