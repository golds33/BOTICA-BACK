package com.vircarmen.botica.config;

import com.vircarmen.botica.entity.Rol;
import com.vircarmen.botica.entity.Usuario;
import com.vircarmen.botica.repository.UsuarioRepository;
import com.vircarmen.botica.entity.Categoria;
import com.vircarmen.botica.entity.Cliente;
import com.vircarmen.botica.repository.CategoriaRepository;
import com.vircarmen.botica.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setRol(Rol.ADMIN);
                usuarioRepository.save(admin);
                System.out.println("====== USUARIO ADMIN CREADO (admin / admin123) ======");
            }
            
            if (categoriaRepository.count() == 0) {
                Categoria c1 = new Categoria(); c1.setNombre("Analgésicos");
                Categoria c2 = new Categoria(); c2.setNombre("Antibióticos");
                Categoria c3 = new Categoria(); c3.setNombre("Vitaminas");
                categoriaRepository.save(c1);
                categoriaRepository.save(c2);
                categoriaRepository.save(c3);
                System.out.println("====== CATEGORÍAS CREADAS ======");
            }
            
            if (clienteRepository.count() == 0) {
                Cliente cli = new Cliente();
                cli.setTipoDocumento("DNI");
                cli.setNumeroDocumento("00000000");
                cli.setNombreRazonSocial("Cliente Público General");
                cli.setDireccion("-");
                clienteRepository.save(cli);
                System.out.println("====== CLIENTE DEFAULT CREADO ======");
            }
        };
    }
}
