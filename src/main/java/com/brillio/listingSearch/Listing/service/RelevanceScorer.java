package com.brillio.listingSearch.Listing.service;

import com.brillio.listingSearch.Listing.model.Listing;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class RelevanceScorer {

    private static final double BUDGET_WEIGHT = 0.70;
    private static final double RECENCY_WEIGHT = 0.30;
    private static final double RECENCY_DECAY_DAYS = 30.0;


    //I used a 70/30 split exponential decay to weight relevance scores. I imagine budget would be more
    //important than recency.

    //The math:
    // return (budgetScore * 0.70) + (recencyScore * 0.30);
    //For example:
    //
    //- Budget score: 0.90
    //- Recency score: 0.80
    //
    //(0.90 \times 0.70) + (0.80 \times 0.30) = 0.87
    // Final score = .87

    public double calculateScore(
            Listing listing,
            Double targetBudget,
            LocalDate today
    ) {

        double recencyScore = calculateRecencyScore(listing, today);

        if (targetBudget == null) {
            return recencyScore;
        }

        double budgetScore = calculateBudgetScore(
                listing.getPrice(),
                targetBudget
        );

        return (budgetScore * BUDGET_WEIGHT)
                + (recencyScore * RECENCY_WEIGHT);
    }

    private double calculateBudgetScore(
            double price,
            double targetBudget
    ) {

        return Math.max(
                0,
                1 - (Math.abs(price - targetBudget) / targetBudget)
        );
    }

    private double calculateRecencyScore(
            Listing listing,
            LocalDate today
    ) {

        if (listing.getListedDate() == null) {
            return 0;
        }

        long daysListed = Math.max(
                0,
                ChronoUnit.DAYS.between(listing.getListedDate(), today)
        );

        return Math.exp(-daysListed / RECENCY_DECAY_DAYS);
    }
}

