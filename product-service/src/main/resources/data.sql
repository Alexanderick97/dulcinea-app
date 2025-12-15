-- product-service/src/main/resources/data.sql
-- Datos iniciales para productos

INSERT INTO products (name, description, price, stock, image_url, category, is_active, created_at, updated_at)
VALUES
('Alfajor Artesanal', 'Delicioso alfajor relleno de manjar casero hecho con ingredientes naturales', 1500.0, 15, 'https://ejemplo.com/alfajor.jpg', 'Dulces', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chilenitos', 'Dulce típico chileno con merengue y crema pastelera', 1000.0, 20, 'https://ejemplo.com/chilenitos.jpg', 'Pasteles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Torta Mil Hojas', 'Clásica torta chilena con mil capas de hojarasca y manjar', 8000.0, 5, 'https://ejemplo.com/milhojas.jpg', 'Tortas', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Chocolate Artesanal 70%', 'Tableta de chocolate oscuro 70% cacao, ideal para repostería', 2500.0, 12, 'https://ejemplo.com/chocolate.jpg', 'Chocolates', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Empanadas de Manjar', 'Empanadas dulces rellenas de manjar, horneadas al momento', 1200.0, 8, 'https://ejemplo.com/empanadas.jpg', 'Dulces', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);