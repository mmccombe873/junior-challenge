package com.unosquare.worldcup.controller;

import com.unosquare.worldcup.dto.*;
import com.unosquare.worldcup.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RouteController — YOUR TASK #3
 *
 * Route optimisation endpoints.
 */
@RestController
@RequestMapping("/api/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // ============================================================
    //  POST /api/route/optimise
    // ============================================================
    //
    // TODO: Implement this endpoint
    //
    // Request body (OptimiseRequestDTO):
    //   - matchIds: List<String> — IDs of matches to include in the route
    //   - originCityId: String — ID of the starting city
    //
    // Response: OptimisedRouteDTO — the optimised route
    //
    // Hint: Call routeService.optimise() with the match IDs and origin city ID
    //
    // ============================================================
    @PostMapping("/optimise")
    public OptimisedRouteDTO optimise(@RequestBody OptimiseRequestDTO request) {
        List<String> matchIds = request.getMatchIds();
        String originCityId = request.getOriginCityId();
        return routeService.optimise(matchIds, originCityId);
    }

    // ============================================================
    //  POST /api/route/budget — YOUR TASK #5
    // ============================================================
    //
    // TODO: Implement this endpoint
    //
    // Request body (BudgetRequestDTO):
    //   - matchIds: List<String> — IDs of matches to include
    //   - budget: Double — the user's budget
    //   - originCityId: String — ID of the starting city
    //
    // Response: BudgetResultDTO — cost breakdown and feasibility
    //
    // Hint: Call routeService.calculateBudget() with the request data
    //
    // ============================================================
    @PostMapping("/budget")
    public BudgetResultDTO calculateBudget(@RequestBody BudgetRequestDTO request) {
        // TODO: Implement this endpoint
        // Hint: Call routeService.calculateBudget() with the request data
        List<String> matchIds = request.getMatchIds();
        System.out.println(">> calculateBudget called with matchIds=" + matchIds.size());
        double budget = request.getBudget();
        String originCityId = request.getOriginCityId();
        return routeService.calculateBudget(matchIds, budget, originCityId);
    }

    // ============================================================
    //  POST /api/route/best-value — BONUS CHALLENGE #1
    // ============================================================
    //
    // TODO: Implement this endpoint (BONUS)
    //
    // Request body (BestValueRequestDTO):
    //   - budget: Double — the user's budget
    //   - originCityId: String — ID of the starting city
    //
    // Response: BestValueResultDTO — best value combination
    //
    // Hint: Call routeService.findBestValue() with the request data
    //
    // ============================================================
    @PostMapping("/best-value")
    public BestValueResultDTO findBestValue(@RequestBody BestValueRequestDTO request) {
        // TODO: Implement this endpoint (BONUS)
        // Hint: Call routeService.findBestValue() with the request data
        return null;
    }
}
