package com.brillio.listingSearch.Listing.controller;

import com.brillio.listingSearch.Listing.dto.ListingSearchRequest;
import com.brillio.listingSearch.Listing.dto.PagedListingResponse;
import com.brillio.listingSearch.Listing.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @GetMapping("/search")
    public PagedListingResponse searchListings(
            @Valid @ModelAttribute ListingSearchRequest request
    ) {
        return listingService.searchListings(request);
    }
}

