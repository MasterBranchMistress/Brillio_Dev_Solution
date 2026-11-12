package com.brillio.listingSearch.Listing.repository;

import com.fasterxml.jackson.*;
import com.brillio.listingSearch.Listing.model.Listing;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Repository
public class ListingRepository {

    private final List<Listing> listings;

    public ListingRepository(ObjectMapper objectMapper) throws IOException {
        this.listings = objectMapper.readValue(
                new ClassPathResource("data/sample_listings.json").getInputStream(),
                new TypeReference<List<Listing>>() {
                }
        );
    }

    public List<Listing> findAll() {
        return listings;
    }
}
