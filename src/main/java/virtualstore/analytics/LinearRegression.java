package virtualstore.analytics;

import java.util.List;

public class LinearRegression {

  private double slope; // Коэффициент наклона (a)
  private double intercept; // Свободный член (b)
  private double rSquared; // Коэффициент детерминации R²

  public LinearRegression(List<Double> xValues, List<Double> yValues) {
    if (xValues.size() != yValues.size() || xValues.isEmpty()) {
      throw new IllegalArgumentException("Массивы должны иметь одинаковую ненулевую длину");
    }

    calculateRegression(xValues, yValues);
  }

  private void calculateRegression(List<Double> xValues, List<Double> yValues) {
    int n = xValues.size();

    // Вычисляем средние значения
    double sumX = 0, sumY = 0;
    for (int i = 0; i < n; i++) {
      sumX += xValues.get(i);
      sumY += yValues.get(i);
    }
    double meanX = sumX / n;
    double meanY = sumY / n;

    // Вычисляем коэффициенты
    double numerator = 0; // Числитель
    double denominator = 0; // Знаменатель

    for (int i = 0; i < n; i++) {
      double xDiff = xValues.get(i) - meanX;
      double yDiff = yValues.get(i) - meanY;
      numerator += xDiff * yDiff;
      denominator += xDiff * xDiff;
    }

    // Рассчитываем наклон и свободный член
    this.slope = numerator / denominator;
    this.intercept = meanY - (this.slope * meanX);

    // Вычисляем R² (коэффициент детерминации)
    double ssTotal = 0; // Общая сумма квадратов
    double ssRes = 0; // Остаточная сумма квадратов

    for (int i = 0; i < n; i++) {
      double yPredicted = predict(xValues.get(i));
      ssTotal += Math.pow(yValues.get(i) - meanY, 2);
      ssRes += Math.pow(yValues.get(i) - yPredicted, 2);
    }

    this.rSquared = 1 - (ssRes / ssTotal);
  }

  // Предсказание значения Y для заданного X
  public double predict(double x) {
    return slope * x + intercept;
  }

  // Геттеры
  public double getSlope() {
    return slope;
  }

  public double getIntercept() {
    return intercept;
  }

  public double getRSquared() {
    return rSquared;
  }

  // Получить уравнение регрессии в виде строки
  public String getEquation() {
    return String.format("y = %.4fx + %.4f", slope, intercept);
  }

  @Override
  public String toString() {
    return String.format(
        "Линейная регрессия:\n" +
            "  Уравнение: %s\n" +
            "  R² = %.4f\n" +
            "  Наклон (a) = %.4f\n" +
            "  Свободный член (b) = %.4f",
        getEquation(), rSquared, slope, intercept);
  }
}
