package br.com.siscond.controller;

import br.com.siscond.repository.ContadorRepository;
import br.com.siscond.repository.EnderecoRepository;
import br.com.siscond.controller.form.ContadorForm;
import br.com.siscond.modelo.Contador;
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
@RequestMapping("/contadores")
public class ContadorController {

    @Autowired
    ContadorRepository contadorRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @GetMapping
    public ResponseEntity<Page<Contador>> listar(Pageable pageable) {
        return ResponseEntity.ok(contadorRepository.findAll(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Contador> cadastrar(@RequestBody @Valid ContadorForm vo, UriComponentsBuilder uriBuilder) {
        Contador contador = vo.converter(enderecoRepository);
        contadorRepository.save(contador);
        URI uri = uriBuilder.path("/contadores/{id}").buildAndExpand(contador.getId()).toUri();
        return ResponseEntity.created(uri).body(contador);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contador> detalhar(@PathVariable Long id) {
        Optional<Contador> optionalContador = contadorRepository.findById(id);
        if(optionalContador.isPresent()) {
            return ResponseEntity.ok(optionalContador.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Contador> atualizar(@PathVariable Long id, @RequestBody @Valid ContadorForm vo) {
        Optional<Contador> optionalContador = contadorRepository.findById(id);

        if(optionalContador.isPresent()) {
            Contador contador = vo.converter(enderecoRepository);
            contador.setId(optionalContador.get().getId());
            return ResponseEntity.ok(contadorRepository.save(contador));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Contador> contadorOptional = contadorRepository.findById(id);
        if(contadorOptional.isPresent()) {
            contadorRepository.delete(contadorOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
