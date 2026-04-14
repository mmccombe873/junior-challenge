package com.unosquare.worldcup.strategy;

import com.unosquare.worldcup.dto.BudgetResultDTO;
import com.unosquare.worldcup.dto.MatchWithCityDTO;
import com.unosquare.worldcup.model.City;
import com.unosquare.worldcup.model.FlightPrice;
import com.unosquare.worldcup.model.Team;
import com.unosquare.worldcup.util.CostCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CostCalculatorTest {

    private CostCalculator costCalculator;

    @BeforeEach
    void setup() { costCalculator = new CostCalculator(); }

    @Test
    void shouldBeFeasibleWhenNoMissingCountriesAndWithinBudget() {
        // Arrange
        // Arrange
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

        double budget = 2000;

        // Act
        BudgetResultDTO result = costCalculator.calculate(matches, budget, List.of(), mexicoCity);

        // Assert
        assertTrue(result.getFeasible());
        assertTrue(result.getMissingCountries().isEmpty());
        assertTrue(result.getSuggestions().isEmpty());
    }

    @Test
    void shouldReturnSuggestionsWhenMissingCountries() {
        // Arrange
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
                "city-los-angeles",
                "Los Angeles",
                "USA",
                34.0549,
                -118.2426,
                "BMO Field",
                150.0
        );

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

        List<FlightPrice> flightPrices = List.of();

        City originCity = newYork;

        double budget = 1000.0;

        // Act
        BudgetResultDTO result = costCalculator.calculate(matches, budget, flightPrices, originCity);

        // Assert
        assertNotNull(result);
        assertFalse(result.getFeasible());
        assertFalse(result.getMissingCountries().isEmpty());
        assertNotNull(result.getSuggestions());
        assertFalse(result.getSuggestions().isEmpty());
        assertEquals(3, result.getRoute().getStops().size());
        assertTrue(result.getMinimumBudgetRequired() > 0);
    }

    @Test
    void shouldBeNotFeasibleWhenCostExceedsBudget() {
        // Arrange
        City mexicoCity = new City(
                "city-mexico-city",
                "Mexico City",
                "Mexico",
                19.3029,
                -99.1505,
                "Estadio Azteca",
                110.0
        );

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

        MatchWithCityDTO match = new MatchWithCityDTO(
                "m1", teamMexico, teamCanada, mexicoCity,
                LocalDateTime.of(2026, 6, 10, 12, 0),
                "A", 1, 500.0
        );

        List<MatchWithCityDTO> matches = List.of(match);

        double tinyBudget = 10.0;

        // Act
        BudgetResultDTO result = costCalculator.calculate(matches, tinyBudget, List.of(), mexicoCity);

        // Assert
        assertFalse(result.getFeasible());
        assertTrue(result.getMinimumBudgetRequired() > tinyBudget);
    }

    @ParameterizedTest
    @CsvSource({
            "999, false",
            "1000, true",
            "1001, true"
    })
    void shouldHandleBudgetBoundaryCorrectly(double budget, boolean expectedFeasible) {
        // Arrange
        City newYork = new City(
                "city-usa", "New York", "USA",
                0.0, 0.0, "Stadium", 0.0
        );

        City mexicoCity = new City(
                "city-mex", "Mexico City", "Mexico",
                0.0, 0.0, "Stadium", 0.0
        );

        City toronto = new City(
                "city-can", "Toronto", "Canada",
                0.0, 0.0, "Stadium", 0.0
        );

        Team t1 = new Team("t1", "A", "A", "A");
        Team t2 = new Team("t2", "B", "B", "A");

        // Total ticket cost = 1000 (400 + 300 + 300)
        MatchWithCityDTO m1 = new MatchWithCityDTO(
                "m1", t1, t2, newYork,
                LocalDateTime.of(2026, 6, 10, 12, 0),
                "A", 1, 400.0
        );

        MatchWithCityDTO m2 = new MatchWithCityDTO(
                "m2", t1, t2, mexicoCity,
                LocalDateTime.of(2026, 6, 11, 12, 0),
                "A", 2, 300.0
        );

        MatchWithCityDTO m3 = new MatchWithCityDTO(
                "m3", t1, t2, toronto,
                LocalDateTime.of(2026, 6, 12, 12, 0),
                "A", 3, 300.0
        );

        List<MatchWithCityDTO> matches = List.of(m1, m2, m3);

        // No flight prices → distance calc = 0 (same coords)
        List<FlightPrice> flightPrices = List.of();

        City originCity = newYork;

        // Act
        BudgetResultDTO result = costCalculator.calculate(matches, budget, flightPrices, originCity);

        // Assert
        assertEquals(expectedFeasible, result.getFeasible());

        // Extra safety checks (optional but strong)
        assertTrue(result.getMissingCountries().isEmpty()); // ensures we're isolating budget
        assertEquals(3, result.getRoute().getStops().size());
        assertEquals(1000.0, result.getMinimumBudgetRequired(), 0.01);
    }
}
