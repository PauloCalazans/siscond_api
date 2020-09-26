package br.com.siscond.controller;

import br.com.siscond.controller.dto.MoradorDto;
import br.com.siscond.modelo.Morador;
import br.com.siscond.repository.CondominioRepository;
import br.com.siscond.repository.EnderecoRepository;
import br.com.siscond.repository.MoradorRepository;
import br.com.siscond.controller.form.MoradorForm;
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
@RequestMapping("/moradores")
public class MoradorController {

    @Autowired
    MoradorRepository moradorRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    CondominioRepository condominioRepository;

    @GetMapping
    public ResponseEntity<Page<Morador>> listar(Pageable pageable) {
        return ResponseEntity.ok(moradorRepository.findAll(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<MoradorDto> cadastrar(@RequestBody @Valid MoradorForm vo, UriComponentsBuilder uriBuilder) {
        Morador morador = vo.converter(enderecoRepository, condominioRepository);
        moradorRepository.save(morador);
        URI uri = uriBuilder.path("/moradores/{id}").buildAndExpand(morador.getId()).toUri();
        return ResponseEntity.created(uri).body(new MoradorDto(morador));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoradorDto> detalhar(@PathVariable Long id) {
        Optional<Morador> optionalMorador = moradorRepository.findById(id);
        if(optionalMorador.isPresent()) {
            return ResponseEntity.ok(new MoradorDto(optionalMorador.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<MoradorDto> atualizar(@PathVariable Long id, @RequestBody @Valid MoradorForm vo) {
        Optional<Morador> optionalMorador = moradorRepository.findById(id);

        if(optionalMorador.isPresent()) {
            Morador morador = vo.converter(enderecoRepository, condominioRepository);
            morador.setId(optionalMorador.get().getId());
            return ResponseEntity.ok(new MoradorDto(moradorRepository.save(morador)));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Morador> moradorOptional = moradorRepository.findById(id);
        if(moradorOptional.isPresent()) {
            moradorRepository.delete(moradorOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
