package virtualstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtualstore.entity.CartItem;
import virtualstore.service.CartItemService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

  @Autowired
  private CartItemService cartItemService;

  @PostMapping
  public ResponseEntity<CartItem> addItem(@RequestBody CartItem cartItem) {
    CartItem created = cartItemService.addItem(cartItem);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<CartItem>> getAllCartItems() {
    return ResponseEntity.ok(cartItemService.getAllCartItems());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
    return cartItemService.getCartItemById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/cart/{cartId}")
  public ResponseEntity<List<CartItem>> getCartItemsByCartId(@PathVariable UUID cartId) {
    return ResponseEntity.ok(cartItemService.getCartItemsByCartId(cartId));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItem) {
    try {
      CartItem updated = cartItemService.updateCartItem(id, cartItem);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
    cartItemService.deleteCartItem(id);
    return ResponseEntity.noContent().build();
  }
}
