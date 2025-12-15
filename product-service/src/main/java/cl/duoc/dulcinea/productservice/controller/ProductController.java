package cl.duoc.dulcinea.productservice.controller;

import cl.duoc.dulcinea.productservice.model.Product;
import cl.duoc.dulcinea.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
// @CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. Crear producto (POST /api/products/)
    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product newProduct = productService.createProduct(product);
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("data", newProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 2. Obtener todos los productos (GET /api/products/)
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getAllProducts();
            response.put("success", true);
            response.put("message", "Productos obtenidos exitosamente");
            response.put("data", products);
            response.put("count", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 3. Obtener producto por ID (GET /api/products/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            return productService.getProductById(id)
                    .map(product -> {
                        response.put("success", true);
                        response.put("message", "Producto encontrado");
                        response.put("data", product);
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        response.put("success", false);
                        response.put("message", "Producto no encontrado con ID: " + id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                    });
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar producto: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 4. Actualizar producto (PUT /api/products/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("data", updatedProduct);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 5. Eliminar producto (DELETE /api/products/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productService.deleteProduct(id);
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente (borrado lógico)");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // 6. Buscar productos por categoría (GET /api/products/category/{category})
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getProductsByCategory(category);
            response.put("success", true);
            response.put("message", "Productos de categoría: " + category);
            response.put("data", products);
            response.put("count", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al buscar productos por categoría: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 7. Buscar productos por nombre (GET /api/products/search?name=...)
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProductsByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.searchProductsByName(name);
            response.put("success", true);
            response.put("message", "Resultados de búsqueda para: " + name);
            response.put("data", products);
            response.put("count", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en búsqueda: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 8. Reducir stock (POST /api/products/{id}/reduce-stock)
    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<Map<String, Object>> reduceStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.reduceStock(id, quantity);
            response.put("success", true);
            response.put("message", "Stock reducido exitosamente");
            response.put("data", product);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 9. Aumentar stock (POST /api/products/{id}/increase-stock)
    @PostMapping("/{id}/increase-stock")
    public ResponseEntity<Map<String, Object>> increaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.increaseStock(id, quantity);
            response.put("success", true);
            response.put("message", "Stock aumentado exitosamente");
            response.put("data", product);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 10. Obtener productos con stock bajo (GET /api/products/low-stock?minStock=...)
    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockProducts(
            @RequestParam(defaultValue = "5") Integer minStock) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productService.getLowStockProducts(minStock);
            response.put("success", true);
            response.put("message", "Productos con stock menor a " + minStock);
            response.put("data", products);
            response.put("count", products.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener productos con stock bajo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 11. Health check (GET /api/products/health)
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Product Service");
        response.put("port", "8082");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // 12. Insertar datos de prueba (POST /api/products/seed)
    @PostMapping("/seed")
    public ResponseEntity<Map<String, Object>> seedDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Productos de ejemplo
            List<Product> sampleProducts = List.of(
                    new Product(
                            "Alfajor Artesanal",
                            "Delicioso alfajor relleno de manjar casero hecho con ingredientes naturales",
                            1500.0,
                            15,
                            "https://ejemplo.com/alfajor.jpg",
                            "Dulces"
                    ),
                    new Product(
                            "Chilenitos",
                            "Dulce típico chileno con merengue y crema pastelera",
                            1000.0,
                            20,
                            "https://ejemplo.com/chilenitos.jpg",
                            "Pasteles"
                    ),
                    new Product(
                            "Torta Mil Hojas",
                            "Clásica torta chilena con mil capas de hojarasca y manjar",
                            8000.0,
                            5,
                            "https://ejemplo.com/milhojas.jpg",
                            "Tortas"
                    ),
                    new Product(
                            "Chocolate Artesanal 70%",
                            "Tableta de chocolate oscuro 70% cacao, ideal para repostería",
                            2500.0,
                            12,
                            "https://ejemplo.com/chocolate.jpg",
                            "Chocolates"
                    ),
                    new Product(
                            "Empanadas de Manjar",
                            "Empanadas dulces rellenas de manjar, horneadas al momento",
                            1200.0,
                            8,
                            "https://ejemplo.com/empanadas.jpg",
                            "Dulces"
                    )
            );

            // Guardar productos
            for (Product product : sampleProducts) {
                productService.createProduct(product);
            }

            response.put("success", true);
            response.put("message", "Base de datos poblada con " + sampleProducts.size() + " productos de ejemplo");
            response.put("count", sampleProducts.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al poblar base de datos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}