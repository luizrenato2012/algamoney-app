package com.algaworks.algamoney.api.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Permissao;
import com.algaworks.algamoney.api.model.Usuario;
import com.algaworks.algamoney.api.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioReposity;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> optionalUsuario = usuarioReposity.findByEmail(email);
		Usuario usuario = optionalUsuario.orElseThrow( () -> new RuntimeException("usuario"));
		return new User(usuario.getEmail(), usuario.getSenha(), this.getPermissoes(usuario.getPermissoes()));
	}

	private List<SimpleGrantedAuthority> getPermissoes(List<Permissao> permissoes) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		permissoes.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao())));
		return authorities;
	}
	
}
