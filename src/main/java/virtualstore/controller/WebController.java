package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import virtualstore.entity.*;
import virtualstore.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class WebController {

  @Autowired
  private ProductService productService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private ShipmentService shipmentService;

  @Autowired
  private CartService cartService;

  @Autowired
  private CartItemService cartItemService;

  @Autowired
  private AnalyticsService analyticsService;

  // Главная страница
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("productCount", productService.getAllProducts().size());
    model.addAttribute("customerCount", customerService.getAllCustomers().size());
    model.addAttribute("orderCount", orderService.getAllOrders().size());
    return "index";
  }

  // === ТОВАРЫ ===

  @GetMapping("/products")
  public String products(Model model) {
    model.addAttribute("products", productService.getAllProducts());
    model.addAttribute("categories", productService.getAllCategories());
    return "products";
  }

  @GetMapping("/products/new")
  public String newProductForm(Model model) {
    model.addAttribute("product", new Product());
    return "product-form";
  }

  @PostMapping("/products/save")
  public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
    try {
      productService.createProduct(product);
      redirectAttributes.addFlashAttribute("success", "Товар успешно добавлен");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении товара");
    }
    return "redirect:/products";
  }

  @GetMapping("/products/edit/{id}")
  public String editProductForm(@PathVariable UUID id, Model model) {
    productService.getProductById(id).ifPresent(product -> {
      model.addAttribute("product", product);
    });
    return "product-form";
  }

  @PostMapping("/products/delete/{id}")
  public String deleteProduct(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
    try {
      productService.deleteProduct(id);
      redirectAttributes.addFlashAttribute("success", "Товар удален");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка при удалении товара");
    }
    return "redirect:/products";
  }

  // === ПОКУПАТЕЛИ ===

  @GetMapping("/customers")
  public String customers(Model model) {
    model.addAttribute("customers", customerService.getAllCustomers());
    return "customers";
  }

  @GetMapping("/customers/new")
  public String newCustomerForm(Model model) {
    model.addAttribute("customer", new Customer());
    return "customer-form";
  }

  @PostMapping("/customers/save")
  public String saveCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
    try {
      if (customer.getCustomerId() != null) {
        customerService.updateCustomer(customer.getCustomerId(), customer);
        redirectAttributes.addFlashAttribute("success", "Покупатель обновлен");
      } else {
        customerService.createCustomer(customer);
        redirectAttributes.addFlashAttribute("success", "Покупатель добавлен");
      }
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/customers";
  }

  @GetMapping("/customers/edit/{id}")
  public String editCustomerForm(@PathVariable UUID id, Model model) {
    customerService.getCustomerById(id).ifPresent(customer -> {
      model.addAttribute("customer", customer);
    });
    return "customer-form";
  }

  @PostMapping("/customers/delete/{id}")
  public String deleteCustomer(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
    try {
      customerService.deleteCustomer(id);
      redirectAttributes.addFlashAttribute("success", "Покупатель удален");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка при удалении");
    }
    return "redirect:/customers";
  }

  // === ЗАКАЗЫ ===

  @GetMapping("/orders")
  public String orders(Model model) {
    model.addAttribute("orders", orderService.getAllOrders());
    return "orders";
  }

  @GetMapping("/orders/{id}")
  public String orderDetails(@PathVariable UUID id, Model model) {
    orderService.getOrderById(id).ifPresent(order -> {
      model.addAttribute("order", order);
    });
    return "order-details";
  }

  @GetMapping("/orders/new")
  public String newOrderForm(Model model) {
    model.addAttribute("customers", customerService.getAllCustomers());
    model.addAttribute("products", productService.getAllProducts());
    return "order-form";
  }

  @PostMapping("/orders/save")
  public String saveOrder(
      @RequestParam UUID customerId,
      @RequestParam UUID productId,
      @RequestParam Integer quantity,
      RedirectAttributes redirectAttributes) {
    try {
      Product product = productService.getProductById(productId)
          .orElseThrow(() -> new RuntimeException("Товар не найден"));

      // Создаём заказ с ID
      Order order = new Order();
      UUID orderId = UUID.randomUUID();
      order.setOrderId(orderId);
      order.setCustomerId(customerId);
      order.setStatus(Order.OrderStatus.PENDING);

      // Создаём позицию заказа с правильным order_id
      OrderItem item = new OrderItem();
      item.setOrderId(orderId);
      item.setProductId(productId);
      item.setQuantity(quantity);
      item.setPrice(product.getPrice());

      List<OrderItem> items = new ArrayList<>();
      items.add(item);
      order.setOrderItems(items);

      orderService.createOrder(order);
      redirectAttributes.addFlashAttribute("success", "Заказ создан");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/orders";
  }

  // === ОТЗЫВЫ ===

  @GetMapping("/reviews")
  public String reviews(Model model) {
    model.addAttribute("reviews", reviewService.getAllReviews());
    return "reviews";
  }

  @GetMapping("/reviews/new")
  public String newReviewForm(Model model) {
    model.addAttribute("review", new Review());
    model.addAttribute("products", productService.getAllProducts());
    model.addAttribute("customers", customerService.getAllCustomers());
    return "review-form";
  }

  @PostMapping("/reviews/save")
  public String saveReview(@ModelAttribute Review review, RedirectAttributes redirectAttributes) {
    try {
      reviewService.createReview(review);
      redirectAttributes.addFlashAttribute("success", "Отзыв добавлен");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/reviews";
  }

  @PostMapping("/reviews/delete/{id}")
  public String deleteReview(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
    try {
      reviewService.deleteReview(id);
      redirectAttributes.addFlashAttribute("success", "Отзыв удален");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка при удалении");
    }
    return "redirect:/reviews";
  }

  // === ПЛАТЕЖИ ===

  @GetMapping("/payments")
  public String payments(Model model) {
    model.addAttribute("payments", paymentService.getAllPayments());
    return "payments";
  }

  @GetMapping("/payments/new")
  public String newPaymentForm(Model model) {
    model.addAttribute("payment", new Payment());
    model.addAttribute("orders", orderService.getAllOrders());
    return "payment-form";
  }

  @PostMapping("/payments/save")
  public String savePayment(@ModelAttribute Payment payment, RedirectAttributes redirectAttributes) {
    try {
      paymentService.createPayment(payment);
      redirectAttributes.addFlashAttribute("success", "Платёж добавлен");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/payments";
  }

  @PostMapping("/payments/delete/{id}")
  public String deletePayment(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
    try {
      paymentService.deletePayment(id);
      redirectAttributes.addFlashAttribute("success", "Платёж удален");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка при удалении");
    }
    return "redirect:/payments";
  }

  // === ДОСТАВКИ ===

  @GetMapping("/shipments")
  public String shipments(Model model) {
    model.addAttribute("shipments", shipmentService.getAllShipments());
    return "shipments";
  }

  @GetMapping("/shipments/new")
  public String newShipmentForm(Model model) {
    model.addAttribute("shipment", new Shipment());
    model.addAttribute("orders", orderService.getAllOrders());
    return "shipment-form";
  }

  @PostMapping("/shipments/save")
  public String saveShipment(@ModelAttribute Shipment shipment, RedirectAttributes redirectAttributes) {
    try {
      shipmentService.createShipment(shipment);
      redirectAttributes.addFlashAttribute("success", "Доставка добавлена");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/shipments";
  }

  @PostMapping("/shipments/delete/{id}")
  public String deleteShipment(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
    try {
      shipmentService.deleteShipment(id);
      redirectAttributes.addFlashAttribute("success", "Доставка удалена");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка при удалении");
    }
    return "redirect:/shipments";
  }

  // === АНАЛИТИКА ===

  @GetMapping("/analytics")
  public String analytics(
      @RequestParam(defaultValue = "7") int days,
      Model model) {
    try {
      AnalyticsService.SalesForecast forecast = analyticsService.forecastSales(days);
      model.addAttribute("forecast", forecast);
      model.addAttribute("days", days);
    } catch (RuntimeException e) {
      model.addAttribute("error", e.getMessage());
    }
    return "analytics";
  }

  // === КОРЗИНЫ ===

  @GetMapping("/cart/{customerId}")
  public String viewCart(@PathVariable UUID customerId, Model model) {
    try {
      Cart cart = cartService.getOrCreateCart(customerId);
      List<CartItem> items = cartItemService.getCartItemsByCartId(cart.getCartId());

      // Загружаем информацию о товарах
      for (CartItem item : items) {
        productService.getProductById(item.getProductId()).ifPresent(product -> {
          item.setProduct(product);
        });
      }

      customerService.getCustomerById(customerId).ifPresent(customer -> {
        model.addAttribute("customer", customer);
      });

      model.addAttribute("cart", cart);
      model.addAttribute("cartItems", items);

      // Вычисляем общую сумму корзины
      BigDecimal total = items.stream()
          .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      model.addAttribute("total", total);

    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "cart";
  }

  @PostMapping("/cart/add")
  public String addToCart(
      @RequestParam UUID customerId,
      @RequestParam UUID productId,
      @RequestParam Integer quantity,
      RedirectAttributes redirectAttributes) {
    try {
      Product product = productService.getProductById(productId)
          .orElseThrow(() -> new RuntimeException("Товар не найден"));

      if (product.getQuantity() < quantity) {
        throw new RuntimeException("Недостаточно товара на складе");
      }

      Cart cart = cartService.getOrCreateCart(customerId);

      CartItem item = new CartItem();
      item.setCartId(cart.getCartId());
      item.setProductId(productId);
      item.setQuantity(quantity);
      item.setPrice(product.getPrice());

      cartItemService.addItem(item);
      redirectAttributes.addFlashAttribute("success", "Товар добавлен в корзину");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/cart/" + customerId;
  }

  @PostMapping("/cart/remove/{itemId}")
  public String removeFromCart(
      @PathVariable Long itemId,
      @RequestParam UUID customerId,
      RedirectAttributes redirectAttributes) {
    try {
      cartItemService.deleteCartItem(itemId);
      redirectAttributes.addFlashAttribute("success", "Товар удален из корзины");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/cart/" + customerId;
  }

  @PostMapping("/cart/checkout/{customerId}")
  public String checkout(@PathVariable UUID customerId, RedirectAttributes redirectAttributes) {
    try {
      Cart cart = cartService.getOrCreateCart(customerId);
      List<CartItem> items = cartItemService.getCartItemsByCartId(cart.getCartId());

      if (items.isEmpty()) {
        throw new RuntimeException("Корзина пуста");
      }

      // Создаём заказ из корзины
      Order order = new Order();
      UUID orderId = UUID.randomUUID();
      order.setOrderId(orderId);
      order.setCustomerId(customerId);
      order.setStatus(Order.OrderStatus.PENDING);

      List<OrderItem> orderItems = new ArrayList<>();
      for (CartItem cartItem : items) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getPrice());
        orderItems.add(orderItem);
      }

      order.setOrderItems(orderItems);
      orderService.createOrder(order);

      // Очищаем корзину
      cartService.clearCart(cart.getCartId());

      redirectAttributes.addFlashAttribute("success", "Заказ создан из корзины");
      return "redirect:/orders";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
      return "redirect:/cart/" + customerId;
    }
  }

  @GetMapping("/cart/select-customer")
  public String selectCustomerForCart(Model model) {
    model.addAttribute("customers", customerService.getAllCustomers());
    return "cart-select-customer";
  }

  // === ИЗМЕНЕНИЕ СТАТУСА ЗАКАЗА ===

  @PostMapping("/orders/update-status/{id}")
  public String updateOrderStatus(
      @PathVariable UUID id,
      @RequestParam Order.OrderStatus status,
      RedirectAttributes redirectAttributes) {
    try {
      orderService.updateOrderStatus(id, status);
      redirectAttributes.addFlashAttribute("success", "Статус заказа обновлён");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/orders/" + id;
  }

  // === ИЗМЕНЕНИЕ СТАТУСА ДОСТАВКИ ===

  @PostMapping("/shipments/update-status/{id}")
  public String updateShipmentStatus(
      @PathVariable UUID id,
      @RequestParam Shipment.ShipmentStatus status,
      RedirectAttributes redirectAttributes) {
    try {
      shipmentService.updateShipmentStatus(id, status);
      redirectAttributes.addFlashAttribute("success", "Статус доставки обновлён");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
    }
    return "redirect:/shipments";
  }

}
