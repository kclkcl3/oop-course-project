package virtualstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virtualstore.entity.CartItem;
import virtualstore.repository.CartItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemService {

  @Autowired
  private CartItemRepository cartItemRepository;

  // CREATE - Добавить товар в корзину
  @Transactional
  public CartItem addItem(CartItem cartItem) {
    return cartItemRepository.save(cartItem);
  }

  // READ - Все позиции
  public List<CartItem> getAllCartItems() {
    return cartItemRepository.findAll();
  }

  // READ - Позиция по ID
  public Optional<CartItem> getCartItemById(Long id) {
    return cartItemRepository.findById(id);
  }

  // READ - Позиции корзины
  public List<CartItem> getCartItemsByCartId(UUID cartId) {
    return cartItemRepository.findByCartId(cartId);
  }

  // UPDATE - Обновить позицию
  @Transactional
  public CartItem updateCartItem(Long id, CartItem updatedItem) {
    return cartItemRepository.findById(id)
        .map(item -> {
          item.setQuantity(updatedItem.getQuantity());
          item.setPrice(updatedItem.getPrice());
          return cartItemRepository.save(item);
        })
        .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
  }

  // DELETE - Удалить позицию
  @Transactional
  public void deleteCartItem(Long id) {
    cartItemRepository.deleteById(id);
  }
}
