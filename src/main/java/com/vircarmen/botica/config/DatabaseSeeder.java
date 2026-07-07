package com.vircarmen.botica.config;

import com.vircarmen.botica.entity.*;
import com.vircarmen.botica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired private RolRepository rolRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private LoteRepository loteRepository;
    @Autowired private MovimientoRepository movimientoRepository;
    @Autowired private DetalleMovimientoRepository detalleMovimientoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() > 0) {
            System.out.println(">>> Base de datos ya inicializada. Seeder omitido.");
            return;
        }

        System.out.println(">>> Iniciando Seeder para insertar datos masivos...");
        Random random = new Random(12345);

        // ===================== ROLES =====================
        Rol adminRol = Rol.ADMIN;
        adminRol = rolRepository.save(adminRol);

        Rol tecnicoRol = Rol.CAJERO;
        tecnicoRol = rolRepository.save(tecnicoRol);

        // ===================== USUARIOS (10) =====================
        List<Usuario> usuarios = new ArrayList<>();
        
        Usuario adminUser = new Usuario();
        adminUser.setNombreCompleto("Juan Carlos Florez Pareja");
        adminUser.setUsername("jflorez");
        adminUser.setPasswordHash(passwordEncoder.encode("123456"));
        adminUser.setRol(adminRol);
        usuarios.add(usuarioRepository.save(adminUser));

        String[][] nombresTecnicos = {
            {"Jhosetd Deyker Gamboa Quispe", "jgamboa"},
            {"María Elena Torres Ramos", "mtorres"},
            {"Carlos Alberto Ruiz", "cruiz"},
            {"Ana Sofía Vargas", "avargas"},
            {"Luis Fernando Mendoza", "lmendoza"},
            {"Carmen Rosa Silva", "csilva"},
            {"Jorge Eduardo Castro", "jcastro"},
            {"Diana Carolina Rojas", "drojas"},
            {"Miguel Angel Paz", "mpaz"}
        };

        for (String[] nt : nombresTecnicos) {
            Usuario u = new Usuario();
            u.setNombreCompleto(nt[0]);
            u.setUsername(nt[1]);
            u.setPasswordHash(passwordEncoder.encode("123456"));
            u.setRol(tecnicoRol);
            usuarios.add(usuarioRepository.save(u));
        }

        // ===================== CATEGORIAS (10) =====================
        List<Categoria> categorias = new ArrayList<>();
        categorias.add(crearCategoria("Analgésicos", "Medicamentos para el alivio del dolor."));
        categorias.add(crearCategoria("Antibióticos", "Medicamentos para tratar infecciones bacterianas."));
        categorias.add(crearCategoria("Antiinflamatorios", "Medicamentos que reducen la inflamación."));
        categorias.add(crearCategoria("Antiparasitarios", "Medicamentos contra parásitos intestinales y externos."));
        categorias.add(crearCategoria("Vitaminas y Suplementos", "Suplementos vitamínicos y minerales."));
        categorias.add(crearCategoria("Dispositivos Médicos", "Insumos y dispositivos médicos de uso clínico."));
        categorias.add(crearCategoria("Dermatológicos", "Cremas, pomadas y tratamientos para la piel."));
        categorias.add(crearCategoria("Gastrointestinales", "Medicamentos para acidez, digestión y úlceras."));
        categorias.add(crearCategoria("Oftalmológicos", "Gotas y ungüentos para los ojos."));
        categorias.add(crearCategoria("Respiratorios", "Inhaladores, jarabes para la tos y mucolíticos."));

        // ===================== PROVEEDORES (10) =====================
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(crearProveedor("20520520334", "Distribuidora Farmacéutica del Sur S.A.C.", "066-312456", "ventas@farmasur.com", "Av. Mariscal Cáceres 456, Ayacucho"));
        proveedores.add(crearProveedor("20100096621", "Laboratorios Medifarma S.A.", "01-6100200", "pedidos@medifarma.com.pe", "Av. Santa Rosa 390, Santa Anita, Lima"));
        proveedores.add(crearProveedor("20100128056", "Química Suiza S.A.C.", "01-6138500", "pedidos@quimicasuiza.com", "Av. Tomás Marsano 2230, Surquillo, Lima"));
        proveedores.add(crearProveedor("20100055237", "Laboratorios AC Farma S.A.", "01-2196300", "ventas@acfarma.com", "Calle Las Begonias 450, San Isidro, Lima"));
        proveedores.add(crearProveedor("20512345678", "Droguería INTI S.A.", "01-4455667", "contacto@inti.com.pe", "Av. Brasil 1234, Jesús María, Lima"));
        proveedores.add(crearProveedor("20698765432", "PharmaGen S.A.C.", "01-7788990", "ventas@pharmagen.pe", "Calle Los Pinos 789, San Isidro, Lima"));
        proveedores.add(crearProveedor("20444555666", "Distribuidora Los Andes", "066-554433", "andes@andesfarma.com", "Jr. Asamblea 112, Ayacucho"));
        proveedores.add(crearProveedor("20111222333", "Bayer S.A.", "01-2223334", "pedidos.peru@bayer.com", "Paseo de la República 3147, San Isidro, Lima"));
        proveedores.add(crearProveedor("20333444555", "Pfizer S.A.", "01-3334445", "info@pfizer.com", "Av. Javier Prado Este 6230, La Molina, Lima"));
        proveedores.add(crearProveedor("20999888777", "Farmaindustria S.A.", "01-9998887", "ventas@farmaindustria.com", "Av. Arequipa 4560, Miraflores, Lima"));

        // ===================== PRODUCTOS (50+) =====================
        String[][] datosProductos = {
            {"Paracetamol 500mg", "Paracetamol", "Tableta 500mg", "0.50", "0"}, // Categoria 0: Analgésicos
            {"Metamizol 500mg", "Metamizol Sódico", "Tableta 500mg", "0.40", "0"},
            {"Tramadol 50mg", "Tramadol clorhidrato", "Cápsula 50mg", "1.20", "0"},
            {"Diclofenaco 50mg", "Diclofenaco sódico", "Tableta 50mg", "0.30", "0"},
            {"Ketorolaco 10mg", "Ketorolaco trometamina", "Tableta 10mg", "0.80", "0"},
            
            {"Amoxicilina 500mg", "Amoxicilina", "Cápsula 500mg", "2.80", "1"}, // Categoria 1: Antibióticos
            {"Azitromicina 500mg", "Azitromicina", "Tableta 500mg", "3.50", "1"},
            {"Ciprofloxacino 500mg", "Ciprofloxacino", "Tableta 500mg", "1.50", "1"},
            {"Cefalexina 500mg", "Cefalexina", "Cápsula 500mg", "2.00", "1"},
            {"Clindamicina 300mg", "Clindamicina", "Cápsula 300mg", "2.50", "1"},
            
            {"Ibuprofeno 400mg", "Ibuprofeno", "Tableta 400mg", "0.80", "2"}, // Categoria 2: Antiinflamatorios
            {"Naproxeno 550mg", "Naproxeno sódico", "Tableta 550mg", "1.00", "2"},
            {"Meloxicam 15mg", "Meloxicam", "Tableta 15mg", "1.50", "2"},
            {"Celecoxib 200mg", "Celecoxib", "Cápsula 200mg", "3.00", "2"},
            {"Piroxicam 20mg", "Piroxicam", "Cápsula 20mg", "0.90", "2"},
            
            {"Albendazol 200mg", "Albendazol", "Tableta 200mg", "1.00", "3"}, // Categoria 3: Antiparasitarios
            {"Mebendazol 100mg", "Mebendazol", "Tableta 100mg", "0.50", "3"},
            {"Ivermectina 6mg", "Ivermectina", "Tableta 6mg", "8.00", "3"},
            {"Secnidazol 500mg", "Secnidazol", "Tableta 500mg", "2.50", "3"},
            {"Metronidazol 500mg", "Metronidazol", "Tableta 500mg", "1.20", "3"},
            
            {"Vitamina C 500mg", "Ácido Ascórbico", "Tableta 500mg", "0.30", "4"}, // Categoria 4: Vitaminas
            {"Complejo B", "Vitaminas B1, B6, B12", "Tableta", "1.00", "4"},
            {"Vitamina D3 2000 UI", "Colecalciferol", "Cápsula", "1.50", "4"},
            {"Zinc 20mg", "Sulfato de Zinc", "Tableta 20mg", "0.80", "4"},
            {"Hierro 100mg", "Sulfato Ferroso", "Tableta 100mg", "0.50", "4"},
            
            {"Jeringa 5ml", "N/A", "Unidad", "0.50", "5"}, // Categoria 5: Dispositivos
            {"Gasa Estéril 10x10", "N/A", "Sobre 5 uds", "1.00", "5"},
            {"Alcohol 70% 1L", "Alcohol etílico", "Frasco 1L", "10.00", "5"},
            {"Algodón 100g", "Algodón hidrófilo", "Paquete 100g", "3.50", "5"},
            {"Termómetro Digital", "N/A", "Unidad", "15.00", "5"},
            
            {"Clotrimazol 1%", "Clotrimazol", "Crema 20g", "5.00", "6"}, // Categoria 6: Dermatológicos
            {"Ketoconazol 2%", "Ketoconazol", "Crema 20g", "6.50", "6"},
            {"Terbinafina 1%", "Terbinafina", "Crema 15g", "12.00", "6"},
            {"Hidrocortisona 1%", "Hidrocortisona", "Crema 20g", "8.00", "6"},
            {"Betametasona 0.05%", "Betametasona", "Crema 20g", "7.50", "6"},
            
            {"Omeprazol 20mg", "Omeprazol", "Cápsula 20mg", "0.60", "7"}, // Categoria 7: Gastrointestinales
            {"Pantoprazol 40mg", "Pantoprazol", "Tableta 40mg", "1.50", "7"},
            {"Ranitidina 150mg", "Ranitidina", "Tableta 150mg", "0.50", "7"},
            {"Bismuto 262mg", "Subsalicilato de Bismuto", "Tableta 262mg", "1.20", "7"},
            {"Loperamida 2mg", "Loperamida", "Tableta 2mg", "0.40", "7"},
            
            {"Ketotifeno 0.025%", "Ketotifeno", "Gotas Oftálmicas 5ml", "15.00", "8"}, // Categoria 8: Oftalmológicos
            {"Lágrimas Artificiales", "Hipromelosa", "Gotas Oftálmicas 15ml", "12.00", "8"},
            {"Timolol 0.5%", "Timolol maleato", "Gotas Oftálmicas 5ml", "20.00", "8"},
            {"Ciprofloxacino 0.3% Oft", "Ciprofloxacino", "Gotas Oftálmicas 5ml", "18.00", "8"},
            {"Tobramicina 0.3%", "Tobramicina", "Gotas Oftálmicas 5ml", "22.00", "8"},
            
            {"Salbutamol Inhalador", "Salbutamol", "Inhalador 200 dosis", "18.00", "9"}, // Categoria 9: Respiratorios
            {"Cetirizina 10mg", "Cetirizina", "Tableta 10mg", "0.40", "9"},
            {"Loratadina 10mg", "Loratadina", "Tableta 10mg", "0.50", "9"},
            {"Dextrometorfano", "Dextrometorfano", "Jarabe 120ml", "15.00", "9"},
            {"Ambroxol 30mg/5ml", "Ambroxol", "Jarabe 120ml", "12.00", "9"}
        };

        List<Producto> productos = new ArrayList<>();
        for (String[] dp : datosProductos) {
            Categoria cat = categorias.get(Integer.parseInt(dp[4]));
            int stockActual = 0; // Se llenará con los ingresos
            int stockMinimo = random.nextInt(20) + 10; // entre 10 y 30
            Producto p = crearProducto(dp[0], dp[1], dp[2], new BigDecimal(dp[3]), stockActual, stockMinimo, cat);
            productos.add(p);
        }

        // ===================== LOTES E INGRESOS =====================
        LocalDate hoy = LocalDate.now();
        List<Lote> lotes = new ArrayList<>();

        // Haremos 5 ingresos grandes, distribuyendo lotes
        for (int i = 0; i < 5; i++) {
            LocalDateTime fechaIngreso = LocalDateTime.now().minusDays(random.nextInt(60) + 10);
            Proveedor prov = proveedores.get(random.nextInt(proveedores.size()));
            Movimiento ingreso = crearMovimiento(TipoMovimiento.INGRESO, adminUser, prov, "Compra a proveedor — OC-2026-0" + (100 + i), fechaIngreso);
            
            // Cada ingreso tendrá 10 productos aleatorios
            for (int j = 0; j < 10; j++) {
                Producto prod = productos.get(random.nextInt(productos.size()));
                
                String codigoLote = "LOT-" + prod.getNombre().substring(0, 2).toUpperCase() + String.format("%03d", i * 10 + j);
                LocalDate fechaVencimiento = hoy.plusMonths(random.nextInt(24) - 2); // Algunos pocos pueden estar vencidos
                int cantidad = random.nextInt(100) + 50; // Entre 50 y 150
                BigDecimal precioUnit = prod.getPrecioVenta().multiply(new BigDecimal("0.6")); // Precio costo aprox 60%
                
                Lote lote = crearLote(codigoLote, fechaIngreso.toLocalDate(), fechaVencimiento, cantidad, prod);
                lotes.add(lote);
                
                // Actualizar stock del producto
                prod.setStockActual(prod.getStockActual() + cantidad);
                productoRepository.save(prod);
                
                crearDetalle(ingreso, lote, cantidad, precioUnit);
            }
        }

        // ===================== VENTAS (SALIDAS) =====================
        // Generar unas 45 ventas en los últimos 60 días
        for (int i = 0; i < 45; i++) {
            LocalDateTime fechaVenta = LocalDateTime.now().minusDays(random.nextInt(60));
            Usuario vendedor = usuarios.get(random.nextInt(usuarios.size()));
            
            Movimiento venta = crearMovimiento(TipoMovimiento.SALIDA, vendedor, null, "Venta directa — Boleta B001-" + String.format("%05d", 1000 + i), fechaVenta);
            
            // Cada venta tiene de 1 a 4 detalles
            int numDetalles = random.nextInt(4) + 1;
            for (int j = 0; j < numDetalles; j++) {
                // Seleccionar un lote disponible
                List<Lote> lotesDisp = lotes.stream().filter(l -> l.getCantidadLote() > 0).toList();
                if (lotesDisp.isEmpty()) break;
                
                Lote loteElegido = lotesDisp.get(random.nextInt(lotesDisp.size()));
                int maxVenta = Math.min(loteElegido.getCantidadLote(), random.nextInt(5) + 1); // Vende entre 1 y 5 unidades
                if (maxVenta == 0) continue;
                
                loteElegido.setCantidadLote(loteElegido.getCantidadLote() - maxVenta);
                loteRepository.save(loteElegido);
                
                Producto p = loteElegido.getProducto();
                p.setStockActual(p.getStockActual() - maxVenta);
                productoRepository.save(p);
                
                crearDetalle(venta, loteElegido, maxVenta, p.getPrecioVenta());
            }
        }
        
        // ===================== ANULACIONES =====================
        // Generar 2 anulaciones de ventas previas (las buscamos)
        List<Movimiento> ventasRealizadas = movimientoRepository.findAll().stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimiento.SALIDA)
                .toList();
        
        if (ventasRealizadas.size() >= 2) {
            for (int i = 0; i < 2; i++) {
                Movimiento v = ventasRealizadas.get(i);
                Movimiento anula = crearMovimiento(TipoMovimiento.ANULACION, adminUser, null, "Anulación por error en venta", LocalDateTime.now());
                anula.setReferenciaAnulacion(v.getIdMovimiento());
                movimientoRepository.save(anula);
                
                // Buscar detalles de la venta y revertir stock
                List<DetalleMovimiento> detalles = detalleMovimientoRepository.findAll().stream()
                        .filter(d -> d.getMovimiento().getIdMovimiento().equals(v.getIdMovimiento()))
                        .toList();
                        
                for (DetalleMovimiento d : detalles) {
                    Lote l = d.getLote();
                    l.setCantidadLote(l.getCantidadLote() + d.getCantidad());
                    loteRepository.save(l);
                    
                    Producto p = l.getProducto();
                    p.setStockActual(p.getStockActual() + d.getCantidad());
                    productoRepository.save(p);
                    
                    crearDetalle(anula, l, d.getCantidad(), d.getPrecioUnitario());
                }
            }
        }

        System.out.println(">>> Base de datos inicializada exitosamente con 50+ productos, 10 usuarios y muchos movimientos.");
    }

    private Categoria crearCategoria(String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        return categoriaRepository.save(c);
    }

    private Proveedor crearProveedor(String ruc, String razonSocial, String telefono,
                                     String correo, String direccion) {
        Proveedor p = new Proveedor();
        p.setRuc(ruc);
        p.setRazonSocial(razonSocial);
        p.setTelefono(telefono);
        p.setCorreo(correo);
        p.setDireccion(direccion);
        return proveedorRepository.save(p);
    }

    private Producto crearProducto(String nombre, String principioActivo, String presentacion,
                                   BigDecimal precio, int stock, int stockMin, Categoria categoria) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrincipioActivo(principioActivo);
        p.setPresentacion(presentacion);
        p.setPrecioVenta(precio);
        p.setStockActual(stock);
        p.setStockMinimo(stockMin);
        p.setCategoria(categoria);
        return productoRepository.save(p);
    }

    private Lote crearLote(String codigo, LocalDate ingreso, LocalDate vencimiento,
                           int cantidad, Producto producto) {
        Lote l = new Lote();
        l.setCodigoLote(codigo);
        l.setFechaIngreso(ingreso);
        l.setFechaVencimiento(vencimiento);
        l.setCantidadLote(cantidad);
        l.setProducto(producto);
        return loteRepository.save(l);
    }

    private Movimiento crearMovimiento(TipoMovimiento tipo, Usuario usuario, Proveedor proveedor,
                                       String motivo, LocalDateTime fecha) {
        Movimiento m = new Movimiento();
        m.setTipoMovimiento(tipo);
        m.setUsuario(usuario);
        m.setProveedor(proveedor);
        m.setMotivo(motivo);
        m.setFechaMovimiento(fecha);
        return movimientoRepository.save(m);
    }

    private void crearDetalle(Movimiento movimiento, Lote lote, int cantidad, BigDecimal precio) {
        if (lote == null) return;
        DetalleMovimiento d = new DetalleMovimiento();
        d.setMovimiento(movimiento);
        d.setLote(lote);
        d.setCantidad(cantidad);
        d.setPrecioUnitario(precio);
        detalleMovimientoRepository.save(d);
    }
}
