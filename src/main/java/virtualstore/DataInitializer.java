package virtualstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import virtualstore.entity.*;
import virtualstore.repository.*;
import virtualstore.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService;

    private Random random = new Random(42);

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() > 0) {
            System.out.println("Данные уже существуют, пропускаем инициализацию");
            return;
        }

        System.out.println("Инициализация тестовых данных...");

        // === ПОКУПАТЕЛИ ===
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomer("Иван", "Петров", "ivan.petrov@example.com",
                "+7-900-123-4567", "г. Оренбург, ул. Ленина, д. 10", 6));
        customers.add(createCustomer("Мария", "Сидорова", "maria.sidorova@example.com",
                "+7-900-765-4321", "г. Оренбург, ул. Советская, д. 25", 3));
        customers.add(createCustomer("Алексей", "Иванов", "alex.ivanov@example.com",
                "+7-900-555-1234", "г. Оренбург, ул. Пушкина, д. 5", 1));
        customers.add(createCustomer("Елена", "Смирнова", "elena.smirnova@example.com",
                "+7-900-111-2222", "г. Оренбург, ул. Гагарина, д. 15", 4));
        customers.add(createCustomer("Дмитрий", "Козлов", "dmitry.kozlov@example.com",
                "+7-900-333-4444", "г. Оренбург, ул. Чкалова, д. 30", 2));
        customers.add(createCustomer("Анна", "Волкова", "anna.volkova@example.com",
                "+7-900-777-8888", "г. Оренбург, ул. Мира, д. 8", 5));
        customers.add(createCustomer("Сергей", "Морозов", "sergey.morozov@example.com",
                "+7-900-999-0000", "г. Оренбург, ул. Победы, д. 12", 7));
        customers.add(createCustomer("Ольга", "Николаева", "olga.nikolaeva@example.com",
                "+7-900-222-3333", "г. Оренбург, ул. Жукова, д. 20", 8));

        // === ТОВАРЫ ===
        List<Product> products = new ArrayList<>();
        products.add(createProduct("Ноутбук Lenovo IdeaPad", "Электроника",
                new BigDecimal("45000.00"), 100));
        products.add(createProduct("Смартфон Samsung Galaxy A54", "Электроника",
                new BigDecimal("28000.00"), 100));
        products.add(createProduct("Наушники Sony WH-1000XM5", "Аксессуары",
                new BigDecimal("22000.00"), 100));
        products.add(createProduct("Клавиатура механическая Logitech", "Аксессуары",
                new BigDecimal("7500.00"), 100));
        products.add(createProduct("Монитор LG 27 UltraGear", "Электроника",
                new BigDecimal("32000.00"), 100));
        products.add(createProduct("Мышь беспроводная Logitech MX Master", "Аксессуары",
                new BigDecimal("5500.00"), 100));
        products.add(createProduct("Веб-камера Logitech C920", "Аксессуары",
                new BigDecimal("6500.00"), 100));
        products.add(createProduct("SSD Samsung 1TB", "Комплектующие",
                new BigDecimal("8500.00"), 100));
        products.add(createProduct("Оперативная память Kingston 16GB", "Комплектующие",
                new BigDecimal("4500.00"), 100));
        products.add(createProduct("Роутер TP-Link Archer", "Сетевое оборудование",
                new BigDecimal("3500.00"), 100));
        products.add(createProduct("Планшет Samsung Galaxy Tab", "Электроника",
                new BigDecimal("35000.00"), 100));
        products.add(createProduct("Умные часы Xiaomi Mi Band", "Аксессуары",
                new BigDecimal("3000.00"), 100));
        products.add(createProduct("Колонка JBL Charge 5", "Аксессуары",
                new BigDecimal("12000.00"), 100));
        products.add(createProduct("Внешний HDD Seagate 2TB", "Комплектующие",
                new BigDecimal("6000.00"), 100));
        products.add(createProduct("Флешка SanDisk 128GB", "Комплектующие",
                new BigDecimal("1500.00"), 100));

        // === ЗАКАЗЫ С ЧЁТКИМ ТРЕНДОМ РОСТА ЗА ПОСЛЕДНИЕ 60 ДНЕЙ ===
        List<Order> orders = new ArrayList<>();

        // Создаём 25 заказов
        int totalOrders = 25;

        for (int i = 0; i < totalOrders; i++) {
            // Случайная дата
            int daysAgo = random.nextInt(60);
            LocalDateTime orderDate = LocalDateTime.now().minusDays(daysAgo);

            // Случайный покупатель
            Customer customer = customers.get(random.nextInt(customers.size()));

            // Определяем статус
            Order.OrderStatus status;
            if (daysAgo > 10) {
                status = Order.OrderStatus.DELIVERED;
            } else if (daysAgo > 5) {
                status = Order.OrderStatus.SHIPPED;
            } else if (daysAgo > 2) {
                status = Order.OrderStatus.PENDING;
            } else {
                Order.OrderStatus[] statuses = Order.OrderStatus.values();
                status = statuses[random.nextInt(statuses.length)];
            }

            // Больше товаров в новых заказах
            int itemsCount;
            if (daysAgo > 45) {
                itemsCount = 1;
            } else if (daysAgo > 30) {
                itemsCount = 1 + random.nextInt(2);
            } else if (daysAgo > 15) {
                itemsCount = 2 + random.nextInt(2);
            } else {
                itemsCount = 3 + random.nextInt(2);
            }

            // ВАЖНО: передаём daysAgo для расчёта множителя цены
            Order order = createOrderWithMultipleItems(customer, products, itemsCount, orderDate, status, daysAgo);
            orders.add(order);

            // Создаём платёж
            Payment.PaymentStatus paymentStatus;
            if (status == Order.OrderStatus.DELIVERED || status == Order.OrderStatus.SHIPPED) {
                paymentStatus = Payment.PaymentStatus.COMPLETED;
            } else if (status == Order.OrderStatus.CANCELLED) {
                paymentStatus = Payment.PaymentStatus.FAILED;
            } else {
                paymentStatus = Payment.PaymentStatus.PENDING;
            }
            createPayment(order, order.getTotalAmount(), paymentStatus, orderDate.plusMinutes(5));

            // Создаём доставку
            if (status == Order.OrderStatus.DELIVERED || status == Order.OrderStatus.SHIPPED) {
                Shipment.ShipmentStatus shipmentStatus = status == Order.OrderStatus.DELIVERED
                        ? Shipment.ShipmentStatus.DELIVERED
                        : Shipment.ShipmentStatus.IN_TRANSIT;
                String trackingNumber = "TRACK-" + String.format("%06d", i + 1) + "-RU";
                createShipment(order, trackingNumber, shipmentStatus, orderDate.plusHours(2));
            }
        }

        // === ОТЗЫВЫ (на 40% от доставленных заказов) ===
        List<Order> deliveredOrders = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .toList();

        int reviewCount = Math.max(1, deliveredOrders.size() * 40 / 100);

        for (int i = 0; i < reviewCount && i < deliveredOrders.size(); i++) {
            Order order = deliveredOrders.get(i);
            Customer customer = customers.stream()
                    .filter(c -> c.getCustomerId().equals(order.getCustomerId()))
                    .findFirst()
                    .orElse(customers.get(0));

            // Создаём отзывы на некоторые товары из заказа
            for (OrderItem item : order.getOrderItems()) {
                // 50% шанс оставить отзыв на каждый товар
                if (random.nextBoolean()) {
                    Product product = products.stream()
                            .filter(p -> p.getProductId().equals(item.getProductId()))
                            .findFirst()
                            .orElse(products.get(0));

                    // Случайный рейтинг 3.0-5.0
                    double rating = 3.0 + (random.nextDouble() * 2.0);
                    BigDecimal ratingDecimal = BigDecimal.valueOf(Math.round(rating * 10) / 10.0);

                    String[] comments = {
                            "Отличный товар, рекомендую!",
                            "Хорошее качество за свои деньги",
                            "Доволен покупкой, всё работает отлично",
                            "Быстрая доставка, товар соответствует описанию",
                            "Неплохо, но есть небольшие недочёты",
                            "Превосходно! Именно то, что искал",
                            "Качество на высоте, буду заказывать ещё",
                            "Средний вариант, ожидал большего",
                            "Отличное соотношение цена-качество",
                            "Рекомендую к покупке!"
                    };

                    String comment = comments[random.nextInt(comments.length)];

                    createReview(customer, product, ratingDecimal, comment,
                            order.getOrderDate().plusDays(3 + random.nextInt(7)));
                }
            }
        }

        // === КОРЗИНЫ ===
        for (int i = 0; i < 3; i++) {
            Customer customer = customers.get(i);
            Cart cart = createCart(customer);

            // Добавляем 2-4 товара в корзину
            int itemsCount = 2 + random.nextInt(3);
            for (int j = 0; j < itemsCount; j++) {
                Product product = products.get(random.nextInt(products.size()));
                int quantity = 1 + random.nextInt(2);
                createCartItem(cart, product, quantity, product.getPrice());
            }
        }

        // === ПЕРЕСЧЁТ РЕЙТИНГОВ ТОВАРОВ ===
        System.out.println("Пересчитываем рейтинги товаров на основе отзывов...");
        for (Product product : products) {
            Double avgRating = reviewRepository.getAverageRatingByProduct(product.getProductId());
            if (avgRating != null && avgRating > 0) {
                product.setRating(BigDecimal.valueOf(Math.round(avgRating * 10) / 10.0));
                productRepository.save(product);
            } else {
                product.setRating(null);
                productRepository.save(product);
            }
        }

        System.out.println("Тестовые данные успешно добавлены!");
        System.out.println("- Покупателей: " + customerRepository.count());
        System.out.println("- Товаров: " + productRepository.count());
        System.out.println("- Заказов: " + orderRepository.count());
        System.out.println("- Отзывов: " + reviewRepository.count());
        System.out.println("- Платежей: " + paymentRepository.count());
        System.out.println("- Доставок: " + shipmentRepository.count());
        System.out.println("- Корзин: " + cartRepository.count());
        System.out.println("- Позиций в корзинах: " + cartItemRepository.count());
    }

    private Customer createCustomer(String firstName, String lastName, String email,
            String phone, String address, int monthsAgo) {
        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setRegistrationDate(LocalDate.now().minusMonths(monthsAgo));
        return customerRepository.save(customer);
    }

    private Product createProduct(String name, String category, BigDecimal price, Integer quantity) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setRating(null);
        return productRepository.save(product);
    }

    private Order createOrderWithMultipleItems(Customer customer, List<Product> allProducts,
            int itemsCount, LocalDateTime orderDate,
            Order.OrderStatus status, int daysAgo) {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomerId(customer.getCustomerId());
        order.setOrderDate(orderDate);
        order.setStatus(status);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // Множитель для имитации роста: старые заказы дешевле
        // 60 дней назад: множитель 0.5 (50% от цены)
        // Сегодня: множитель 1.5 (150% от цены)
        double priceMultiplier = 0.5 + (1.0 * (60 - daysAgo) / 60.0);

        // Выбираем случайные товары без повторений
        List<Product> selectedProducts = new ArrayList<>(allProducts);
        for (int i = 0; i < itemsCount && !selectedProducts.isEmpty(); i++) {
            Product product = selectedProducts.remove(random.nextInt(selectedProducts.size()));
            int quantity = 1 + random.nextInt(2);

            // Применяем множитель к цене (имитация роста цен/спроса)
            BigDecimal adjustedPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(priceMultiplier));

            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(product.getProductId());
            item.setQuantity(quantity);
            item.setPrice(adjustedPrice);

            orderItems.add(item);
            totalAmount = totalAmount.add(adjustedPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    private Review createReview(Customer customer, Product product, BigDecimal rating,
            String comment, LocalDateTime reviewDate) {
        Review review = new Review();
        review.setReviewId(UUID.randomUUID());
        review.setProductId(product.getProductId());
        review.setCustomerId(customer.getCustomerId());
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(reviewDate);
        return reviewRepository.save(review);
    }

    private Payment createPayment(Order order, BigDecimal amount,
            Payment.PaymentStatus status, LocalDateTime paymentDate) {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(order.getOrderId());
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setPaymentDate(paymentDate);
        return paymentRepository.save(payment);
    }

    private Shipment createShipment(Order order, String trackingNumber,
            Shipment.ShipmentStatus status, LocalDateTime shipmentDate) {
        Shipment shipment = new Shipment();
        shipment.setShipmentId(UUID.randomUUID());
        shipment.setOrderId(order.getOrderId());
        shipment.setTrackingNumber(trackingNumber);
        shipment.setStatus(status);
        shipment.setShipmentDate(shipmentDate);
        return shipmentRepository.save(shipment);
    }

    private Cart createCart(Customer customer) {
        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());
        cart.setCustomerId(customer.getCustomerId());
        return cartRepository.save(cart);
    }

    private CartItem createCartItem(Cart cart, Product product, int quantity, BigDecimal price) {
        CartItem item = new CartItem();
        item.setCartId(cart.getCartId());
        item.setProductId(product.getProductId());
        item.setQuantity(quantity);
        item.setPrice(price);
        return cartItemRepository.save(item);
    }

}
