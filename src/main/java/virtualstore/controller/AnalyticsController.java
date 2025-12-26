package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

  @Autowired
  private AnalyticsService analyticsService;

  // GET /api/analytics/forecast?days=7
  @GetMapping("/forecast")
  public ResponseEntity<AnalyticsService.SalesForecast> getForecast(
      @RequestParam(defaultValue = "7") int days) {
    try {
      AnalyticsService.SalesForecast forecast = analyticsService.forecastSales(days);
      return ResponseEntity.ok(forecast);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
