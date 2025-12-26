package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.Cart;
import virtualstore.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {

  @Autowired
  private CartService cartService;

  @PostMapping
  public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
    Cart created = cartService.createCart(cart);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Cart>> getAllCarts() {
    return ResponseEntity.ok(cartService.getAllCarts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cart> getCartById(@PathVariable UUID id) {
    return cartService.getCartById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<Cart> getCartByCustomerId(@PathVariable UUID customerId) {
    return ResponseEntity.ok(cartService.getOrCreateCart(customerId));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCart(@PathVariable UUID id) {
    cartService.deleteCart(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/clear")
  public ResponseEntity<Void> clearCart(@PathVariable UUID id) {
    cartService.clearCart(id);
    return ResponseEntity.noContent().build();
  }
}
