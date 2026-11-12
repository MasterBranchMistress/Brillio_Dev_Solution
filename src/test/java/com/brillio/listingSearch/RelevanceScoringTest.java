package com.brillio.listingSearch;

import com.brillio.listingSearch.Listing.model.Listing;
import com.brillio.listingSearch.Listing.service.RelevanceScorer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RelevanceScorerTest {

    private RelevanceScorer scorer;
    private Listing listing;

    private final LocalDate TODAY = LocalDate.of(2026, 11, 12);

    @BeforeEach
    void setUp() {
        scorer = new RelevanceScorer();

        listing = new Listing();
        listing.setPrice(300000.0);
        listing.setListedDate(TODAY);
    }

    @Test
    void exactBudgetMatchShouldReceiveMaximumBudgetScore() {
        double score = scorer.calculateScore(listing, 300000.0, TODAY);

        assertEquals(1.0, score, 0.0001);
    }

    @Test
    void listingOutsideBudgetShouldReceiveReducedScore() {
        listing.setPrice(400000.0);

        double score = scorer.calculateScore(listing, 300000.0, TODAY);

        // Budget score = 1 - (100000 / 300000)
        // Recency score = 1
        // Final score = (0.6667 * 0.70) + (1 * 0.30)
        assertEquals(0.7667, score, 0.001);
    }

    @Test
    void listingWithoutTargetBudgetShouldUseRecencyScoreOnly() {
        listing.setListedDate(TODAY.minusDays(30));

        double score = scorer.calculateScore(listing, null, TODAY);

        assertEquals(Math.exp(-1), score, 0.0001);
    }

    @Test
    void listingWithoutListedDateShouldReceiveZeroRecencyScore() {
        listing.setListedDate(null);

        double score = scorer.calculateScore(listing, null, TODAY);

        assertEquals(0.0, score, 0.0001);
    }

    @Test
    void futureListingDateShouldNotProduceRecencyAboveOne() {
        listing.setListedDate(TODAY.plusDays(5));

        double score = scorer.calculateScore(listing, null, TODAY);

        assertEquals(1.0, score, 0.0001);
    }
}

