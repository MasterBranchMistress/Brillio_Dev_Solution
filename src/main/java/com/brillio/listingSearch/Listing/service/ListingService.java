package com.brillio.listingSearch.Listing.service;


import com.brillio.listingSearch.Listing.dto.ListingSearchRequest;
import com.brillio.listingSearch.Listing.dto.ListingSearchResponse;
import com.brillio.listingSearch.Listing.dto.PagedListingResponse;
import com.brillio.listingSearch.Listing.repository.ListingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final RelevanceScorer relevanceScorer;

    public PagedListingResponse searchListings(ListingSearchRequest request) {

        int page = request.getPage() == null ? 0 : request.getPage();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();

        LocalDate today = LocalDate.now();

        List<ListingSearchResponse> results = listingRepository.findAll()
                .stream()

                // Only active listings
                .filter(listing ->
                        "active".equalsIgnoreCase(listing.getStatus()))

                // Price range
                .filter(listing ->
                        request.getMinPrice() == null ||
                                listing.getPrice() >= request.getMinPrice())

                .filter(listing ->
                        request.getMaxPrice() == null ||
                                listing.getPrice() <= request.getMaxPrice())

                // Minimum bedrooms
                .filter(listing ->
                        request.getMinBedrooms() == null ||
                                listing.getBedrooms() >= request.getMinBedrooms())

                // City
                .filter(listing ->
                        request.getCity() == null ||
                                request.getCity().isBlank() ||
                                listing.getCity().equalsIgnoreCase(
                                        request.getCity().trim()
                                ))

                // Description keyword
                .filter(listing ->
                        request.getKeyword() == null ||
                                request.getKeyword().isBlank() ||
                                (listing.getDescription() != null &&
                                        listing.getDescription().toLowerCase(Locale.ROOT)
                                                .contains(request.getKeyword()
                                                        .trim().toLowerCase(Locale.ROOT))))

                // Calculate relevance
                .map(listing -> new ListingSearchResponse(
                        listing,
                        relevanceScorer.calculateScore(
                                listing,
                                request.getTargetBudget(),
                                today
                        )
                ))

                // Sort by relevance, then date, source, and ID
                .sorted(
                        Comparator.comparingDouble(
                                        ListingSearchResponse::relevanceScore
                                ).reversed()
                                .thenComparing(
                                        result -> result.listing().getListedDate(),
                                        Comparator.nullsLast(
                                                Comparator.reverseOrder()
                                        )
                                )
                                .thenComparing(
                                        result -> result.listing().getSource()
                                )
                                .thenComparing(
                                        result -> result.listing().getId()
                                )
                )
                .toList();

        // Pagination metadata
        long totalElements = results.size();

        int totalPages = (int) Math.ceil(
                (double) totalElements / pageSize
        );

        long start = (long) page * pageSize;

        List<ListingSearchResponse> content =
                start >= totalElements
                        ? List.of()
                        : results.subList(
                        (int) start,
                        (int) Math.min(start + pageSize, totalElements)
                );

        return new PagedListingResponse(
                content,
                page,
                pageSize,
                totalElements,
                totalPages
        );
    }
}
