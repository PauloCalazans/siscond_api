package br.com.siscond.controller;

import br.com.siscond.modelo.Endereco;
import br.com.siscond.repository.EnderecoRepository;
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
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    EnderecoRepository enderecoRepository;

    @GetMapping
    public ResponseEntity<Page<Endereco>> listar(Pageable pageable) {
        return ResponseEntity.ok(enderecoRepository.findAll(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Endereco> cadastrar(@RequestBody @Valid Endereco vo, UriComponentsBuilder uriBuilder) {
        Endereco endereco = vo;
        enderecoRepository.save(endereco);
        URI uri = uriBuilder.path("/enderecos/{id}").buildAndExpand(endereco.getId()).toUri();
        return ResponseEntity.created(uri).body(endereco);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> detalhar(@PathVariable Long id) {
        Optional<Endereco> Endereco = enderecoRepository.findById(id);
        if(Endereco.isPresent()) {
            return ResponseEntity.ok(Endereco.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Endereco> atualizar(@PathVariable Long id, @RequestBody @Valid Endereco vo) {
        Optional<Endereco> optionalEndereco = enderecoRepository.findById(id);

        if(optionalEndereco.isPresent()) {
            vo.setId(optionalEndereco.get().getId());
            return ResponseEntity.ok(enderecoRepository.save(vo));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Endereco> deletar(@PathVariable Long id) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);
        if(enderecoOptional.isPresent()) {
            enderecoRepository.delete(enderecoOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
