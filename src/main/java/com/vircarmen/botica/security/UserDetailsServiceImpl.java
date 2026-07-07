package com.vircarmen.botica.security;

import com.vircarmen.botica.entity.Usuario;
import com.vircarmen.botica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
      // Dentro de UserDetailsServiceImpl.java
    return new org.springframework.security.core.userdetails.User(
    usuario.getUsername(),
    usuario.getPasswordHash(),
    // Simplificado para usar el Enum correctamente:
    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
);
    }
}
