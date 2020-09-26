package br.com.siscond.controller;

import br.com.siscond.controller.dto.UsuarioDto;
import br.com.siscond.controller.form.UsuarioForm;
import br.com.siscond.modelo.Usuario;
import br.com.siscond.repository.ContabilidadeRepository;
import br.com.siscond.repository.GrupoRepository;
import br.com.siscond.repository.MoradorRepository;
import br.com.siscond.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.*;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    MoradorRepository moradorRepository;

    @Autowired
    ContabilidadeRepository contabilidadeRepository;

    @Autowired
    GrupoRepository grupoRepository;

    @GetMapping
    public ResponseEntity<Page<UsuarioDto>> listar(Pageable pageable) {
        return ResponseEntity.ok(UsuarioDto.converter(usuarioRepository.findAll(pageable)));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioForm vo, UriComponentsBuilder uriBuilder) {
        Usuario usuario = vo.converter(moradorRepository, contabilidadeRepository, grupoRepository);
        usuarioRepository.save(usuario);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> detalhar(@PathVariable Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        return optionalUsuario.map(usuario -> ResponseEntity.ok().body(new UsuarioDto(usuario))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UsuarioDto> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioForm vo) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if(optionalUsuario.isPresent()) {
            Usuario usuario = vo.converter(moradorRepository, contabilidadeRepository, grupoRepository);
            usuario.setId(optionalUsuario.get().getId());
            return ResponseEntity.ok(new UsuarioDto(usuarioRepository.save(usuario)));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isPresent()) {
            usuarioRepository.delete(usuarioOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
