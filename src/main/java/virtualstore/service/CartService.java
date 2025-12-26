package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.Cart;
import virtualstore.entity.CartItem;
import virtualstore.repository.CartRepository;
import virtualstore.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  // CREATE - Создать корзину
  @Transactional
  public Cart createCart(Cart cart) {
    return cartRepository.save(cart);
  }

  // READ - Все корзины
  public List<Cart> getAllCarts() {
    return cartRepository.findAll();
  }

  // READ - Корзина по ID
  public Optional<Cart> getCartById(UUID id) {
    return cartRepository.findById(id);
  }

  // READ - Корзина покупателя
  public Optional<Cart> getCartByCustomerId(UUID customerId) {
    return cartRepository.findByCustomerId(customerId);
  }

  // READ или CREATE - Получить или создать корзину покупателя
  @Transactional
  public Cart getOrCreateCart(UUID customerId) {
    return cartRepository.findByCustomerId(customerId)
        .orElseGet(() -> {
          Cart cart = new Cart();
          cart.setCustomerId(customerId);
          return cartRepository.save(cart);
        });
  }

  // DELETE - Удалить корзину
  @Transactional
  public void deleteCart(UUID id) {
    cartRepository.deleteById(id);
  }

  // DELETE - Очистить корзину
  @Transactional
  public void clearCart(UUID cartId) {
    cartItemRepository.deleteByCartId(cartId);
  }
}
