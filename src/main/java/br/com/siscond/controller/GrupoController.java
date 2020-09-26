package br.com.siscond.controller;

import br.com.siscond.modelo.Grupo;
import br.com.siscond.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    GrupoRepository grupoRepository;

    @GetMapping
    public ResponseEntity<Page<Grupo>> listar(Pageable pageable) {
        return ResponseEntity.ok(grupoRepository.findAll(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Grupo> cadastrar(@RequestBody @Valid Grupo vo, UriComponentsBuilder uriBuilder) {
        Grupo grupo = vo;
        grupoRepository.save(grupo);
        URI uri = uriBuilder.path("/grupos/{id}").buildAndExpand(grupo.getId()).toUri();
        return ResponseEntity.created(uri).body(grupo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> detalhar(@PathVariable Long id) {
        Optional<Grupo> grupoOptional = grupoRepository.findById(id);
        if(grupoOptional.isPresent()) {
            return ResponseEntity.ok(grupoOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Grupo> atualizar(@PathVariable Long id, @RequestBody @Valid Grupo vo) {
        Optional<Grupo> optionalGrupo = grupoRepository.findById(id);

        if(optionalGrupo.isPresent()) {
            vo.setId(optionalGrupo.get().getId());
            return ResponseEntity.ok(grupoRepository.save(vo));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Grupo> grupoOptional = grupoRepository.findById(id);
        if(grupoOptional.isPresent()) {
            grupoRepository.delete(grupoOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
