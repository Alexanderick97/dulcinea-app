package cl.duoc.dulcinea.productservice.repository;

import cl.duoc.dulcinea.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();
    List<Product> findByCategoryAndIsActiveTrue(String category);
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
    boolean existsByName(String name);
    List<Product> findByStockLessThanAndIsActiveTrue(Integer minStock);
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}