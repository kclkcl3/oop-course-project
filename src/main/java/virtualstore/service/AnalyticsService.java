package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import virtualstore.analytics.LinearRegression;
import virtualstore.entity.Order;
import virtualstore.repository.OrderRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

  @Autowired
  private OrderRepository orderRepository;

  // Прогнозирование продаж с использованием линейной регрессии
  public SalesForecast forecastSales(int daysAhead) {
    List<Order> orders = orderRepository.findAll();

    if (orders.isEmpty()) {
      throw new RuntimeException("Недостаточно данных для прогнозирования");
    }

    // Группируем заказы по дням
    LocalDateTime minDate = orders.stream()
        .map(Order::getOrderDate)
        .min(LocalDateTime::compareTo)
        .orElse(LocalDateTime.now());

    Map<Long, Double> salesByDay = orders.stream()
        .collect(Collectors.groupingBy(
            order -> ChronoUnit.DAYS.between(minDate, order.getOrderDate()),
            Collectors.summingDouble(order -> order.getTotalAmount().doubleValue())));

    // Подготавливаем данные для регрессии и сортируем по дню
    List<Double> xValues = new ArrayList<>();
    List<Double> yValues = new ArrayList<>();

    // Создаём список пар (день, сумма) и сортируем
    List<Map.Entry<Long, Double>> sortedEntries = new ArrayList<>(salesByDay.entrySet());
    sortedEntries.sort(Map.Entry.comparingByKey());

    for (Map.Entry<Long, Double> entry : sortedEntries) {
      xValues.add(entry.getKey().doubleValue());
      yValues.add(entry.getValue());
    }

    if (xValues.size() < 2) {
      throw new RuntimeException("Недостаточно данных для построения регрессии (минимум 2 точки)");
    }

    // Строим модель линейной регрессии
    LinearRegression regression = new LinearRegression(xValues, yValues);

    // Прогнозируем на указанное количество дней вперёд
    long currentDay = ChronoUnit.DAYS.between(minDate, LocalDateTime.now());
    double forecastedSales = regression.predict(currentDay + daysAhead);

    return new SalesForecast(
        regression,
        forecastedSales,
        daysAhead,
        xValues,
        yValues);
  }

  // Класс для результата прогноза
  public static class SalesForecast {
    private final LinearRegression regression;
    private final double forecastedAmount;
    private final int daysAhead;
    private final List<Double> historicalDays;
    private final List<Double> historicalSales;

    public SalesForecast(LinearRegression regression, double forecastedAmount,
        int daysAhead, List<Double> historicalDays,
        List<Double> historicalSales) {
      this.regression = regression;
      this.forecastedAmount = forecastedAmount;
      this.daysAhead = daysAhead;
      this.historicalDays = historicalDays;
      this.historicalSales = historicalSales;
    }

    public LinearRegression getRegression() {
      return regression;
    }

    public double getForecastedAmount() {
      return forecastedAmount;
    }

    public int getDaysAhead() {
      return daysAhead;
    }

    public List<Double> getHistoricalDays() {
      return historicalDays;
    }

    public List<Double> getHistoricalSales() {
      return historicalSales;
    }

    @Override
    public String toString() {
      return String.format(
          "Прогноз продаж на %d дней:\n" +
              "  Ожидаемая сумма: %.2f ₽\n" +
              "  %s\n" +
              "  Количество точек данных: %d",
          daysAhead, forecastedAmount, regression.toString(), historicalDays.size());
    }
  }
}
