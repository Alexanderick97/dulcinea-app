package cl.duoc.dulcinea.productservice.service;

import cl.duoc.dulcinea.productservice.model.Product;
import cl.duoc.dulcinea.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 1. Crear producto
    public Product createProduct(Product product) {
        // Validar que no exista producto con mismo nombre
        if (productRepository.existsByName(product.getName())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + product.getName());
        }

        // Asegurar que esté activo
        product.setIsActive(true);

        return productRepository.save(product);
    }

    // 2. Obtener todos los productos activos
    public List<Product> getAllProducts() {
        return productRepository.findByIsActiveTrue();
    }

    // 3. Obtener producto por ID (solo activos)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdAndIsActiveTrue(id);
    }

    // 4. Actualizar producto
    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Actualizar campos si no son nulos
                    if (productDetails.getName() != null) {
                        // Verificar que el nuevo nombre no esté en uso por otro producto
                        if (!existingProduct.getName().equals(productDetails.getName()) &&
                                productRepository.existsByName(productDetails.getName())) {
                            throw new RuntimeException("Ya existe otro producto con el nombre: " + productDetails.getName());
                        }
                        existingProduct.setName(productDetails.getName());
                    }

                    if (productDetails.getDescription() != null) {
                        existingProduct.setDescription(productDetails.getDescription());
                    }

                    if (productDetails.getPrice() != null) {
                        existingProduct.setPrice(productDetails.getPrice());
                    }

                    if (productDetails.getStock() != null) {
                        existingProduct.setStock(productDetails.getStock());
                    }

                    if (productDetails.getImageUrl() != null) {
                        existingProduct.setImageUrl(productDetails.getImageUrl());
                    }

                    if (productDetails.getCategory() != null) {
                        existingProduct.setCategory(productDetails.getCategory());
                    }

                    if (productDetails.getIsActive() != null) {
                        existingProduct.setIsActive(productDetails.getIsActive());
                    }

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // 5. Eliminar producto (borrado lógico)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Borrado lógico
        product.setIsActive(false);
        productRepository.save(product);
    }

    // 6. Buscar productos por categoría
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndIsActiveTrue(category);
    }

    // 7. Buscar productos por nombre
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    // 8. Reducir stock (para ventas)
    public Product reduceStock(Long id, Integer quantity) {
        Product product = productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o inactivo con ID: " + id));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + product.getStock() + ", Solicitado: " + quantity);
        }

        product.setStock(product.getStock() - quantity);
        return productRepository.save(product);
    }

    // 9. Aumentar stock (para reposición)
    public Product increaseStock(Long id, Integer quantity) {
        Product product = productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado o inactivo con ID: " + id));

        product.setStock(product.getStock() + quantity);
        return productRepository.save(product);
    }

    // 10. Obtener productos con stock bajo
    public List<Product> getLowStockProducts(Integer minStock) {
        return productRepository.findByStockLessThanAndIsActiveTrue(minStock);
    }
}