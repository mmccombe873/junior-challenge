package com.unosquare.worldcup.strategy;

import com.unosquare.worldcup.dto.MatchWithCityDTO;
import com.unosquare.worldcup.dto.OptimisedRouteDTO;
import com.unosquare.worldcup.model.City;
import com.unosquare.worldcup.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NearestNeighbourStrategyTest — YOUR TASK #4
 *
 * ============================================================
 * WHAT YOU NEED TO IMPLEMENT:
 * ============================================================
 *
 * Write unit tests for the NearestNeighbourStrategy.
 * Each test has a TODO comment explaining what to test.
 *
 *
 */
class NearestNeighbourStrategyTest {

    private NearestNeighbourStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new NearestNeighbourStrategy();
    }

    @Test
    void shouldReturnValidRouteForMultipleMatches() {
        // TODO: Implement this test
        //
        // Arrange: Create a list of matches across different cities and dates
        // - Create 3 cities (one in each country: USA, Mexico, Canada)
        City mexicoCity = new City(
                "city-mexico-city",
                "Mexico City",
                "Mexico",
                19.3029,
                -99.1505,
                "Estadio Azteca",
                110.0
        );

        City newYork = new City(
                "city-new-york",
                "New York",
                "USA",
                40.7128,
                -74.0060,
                "MetLife Stadium",
                180.0
        );

        City toronto = new City(
                "city-toronto",
                "Toronto",
                "Canada",
                43.6532,
                -79.3832,
                "BMO Field",
                150.0
        );

        // - Create 2 teams
        Team teamMexico = new Team(
                "team-mexico",
                "Mexico",
                "MEX",
                "A"
        );

        Team teamCanada = new Team(
                "team-canada",
                "Canada",
                "CAN",
                "A"
        );

        // - Create 3 matches (one per city, on different dates)
        MatchWithCityDTO match1 = new MatchWithCityDTO(
                "match-1",
                teamMexico,
                teamCanada,
                mexicoCity,
                LocalDateTime.of(2026, 6, 11, 17, 0),
                "A",
                1,
                120.0
        );

        MatchWithCityDTO match2 = new MatchWithCityDTO(
                "match-2",
                teamCanada,
                teamMexico,
                newYork,
                LocalDateTime.of(2026, 6, 12, 18, 0),
                "A",
                2,
                150.0
        );

        MatchWithCityDTO match3 = new MatchWithCityDTO(
                "match-3",
                teamMexico,
                teamCanada,
                toronto,
                LocalDateTime.of(2026, 6, 13, 19, 0),
                "A",
                3,
                130.0
        );

        List<MatchWithCityDTO> matches = List.of(match1, match2, match3);

        // Act: Call strategy.optimise(matches, null)
        OptimisedRouteDTO result = strategy.optimise(matches, null);

        // Assert: Verify:
        // - result is not null
        // - result has 3 stops
        // - totalDistance > 0
        // - strategy = "nearest-neighbour"

        assertNotNull(result);
        assertEquals(3, result.getStops().size());
        assertTrue(result.getTotalDistance() > 0);
        assertEquals("nearest-neighbour", result.getStrategy());
    }

    @Test
    void shouldReturnEmptyRouteForEmptyMatches() {
        // TODO: Implement this test
        //
        // Arrange: Create an empty list of matches
        List<MatchWithCityDTO> emptyList = new ArrayList<>();

        // Act: Call strategy.optimise(emptyList, null)
        OptimisedRouteDTO result = strategy.optimise(emptyList, null);

        // Assert: Verify:
        // - result is not null
        // - result has empty stops
        // - totalDistance = 0
        // - feasible = false
        assertNotNull(result);
        assertEquals(0, result.getStops().size());
        assertEquals(0, result.getTotalDistance());
        assertFalse(result.isFeasible());

    }

    @Test
    void shouldReturnZeroDistanceForSingleMatch() {
        // TODO: Implement this test
        //
        // Arrange: Create a list with a single match
        // - Create 1 city
        City mexicoCity = new City(
                "city-mexico-city",
                "Mexico City",
                "Mexico",
                19.3029,
                -99.1505,
                "Estadio Azteca",
                110.0
        );

        // - Create 2 teams
        Team teamMexico = new Team(
                "team-mexico",
                "Mexico",
                "MEX",
                "A"
        );

        Team teamCanada = new Team(
                "team-canada",
                "Canada",
                "CAN",
                "A"
        );

        // - Create 1 match
        MatchWithCityDTO match1 = new MatchWithCityDTO(
                "match-1",
                teamMexico,
                teamCanada,
                mexicoCity,
                LocalDateTime.of(2026, 6, 11, 17, 0),
                "A",
                1,
                120.0
        );

        List<MatchWithCityDTO> matches = List.of(match1);

        // Act: Call strategy.optimise(matches, null)
        OptimisedRouteDTO result = strategy.optimise(matches, null);

        // Assert: Verify:
        // - result is not null
        // - stops.size() = 1
        // - totalDistance = 0
        assertNotNull(result);
        assertEquals(1, result.getStops().size());
        assertEquals(0, result.getTotalDistance());

    }

}
