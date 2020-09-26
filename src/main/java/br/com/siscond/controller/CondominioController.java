package br.com.siscond.controller;

import br.com.siscond.controller.dto.ContabilidadeDto;
import br.com.siscond.controller.form.CondominioForm;
import br.com.siscond.modelo.Condominio;
import br.com.siscond.modelo.Contabilidade;
import br.com.siscond.repository.CondominioRepository;
import br.com.siscond.repository.ContabilidadeRepository;
import br.com.siscond.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/condominios")
public class CondominioController {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    ContabilidadeRepository contabilidadeRepository;

    @Autowired
    CondominioRepository condominioRepository;

    @GetMapping
    public ResponseEntity<Page<Condominio>> listar(Pageable pageable) {
        return ResponseEntity.ok(condominioRepository.findAll(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Condominio> cadastrar(@RequestBody @Valid CondominioForm vo, UriComponentsBuilder uriBuilder) {
        Condominio condominio = vo.converter(enderecoRepository, contabilidadeRepository);
        condominioRepository.save(condominio);
        URI uri = uriBuilder.path("/condominios/{id}").buildAndExpand(condominio.getId()).toUri();
        return ResponseEntity.created(uri).body(condominio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Condominio> detalhar(@PathVariable Long id) {
        Optional<Condominio> optionalCondominio = condominioRepository.findById(id);
        if(optionalCondominio.isPresent()) {
            return ResponseEntity.ok(optionalCondominio.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Condominio> atualizar(@PathVariable Long id, @RequestBody @Valid CondominioForm vo) {
        Optional<Condominio> optionalCondominio = condominioRepository.findById(id);

        if(optionalCondominio.isPresent()) {
            Condominio condominio = vo.converter(enderecoRepository, contabilidadeRepository);
            condominio.setId(optionalCondominio.get().getId());
            return ResponseEntity.ok(condominioRepository.save(condominio));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Condominio> condominioOptional = condominioRepository.findById(id);
        if(condominioOptional.isPresent()) {
            condominioRepository.delete(condominioOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Transactional
    @PostMapping("/upload")
    public ResponseEntity<ContabilidadeDto> logo(@RequestParam Long id, @RequestParam MultipartFile logo) {
        Optional<Contabilidade> contabilidade = contabilidadeRepository.findById(id);
        if(contabilidade.isPresent()) {
            try {
                contabilidade.get().setLogo(logo.getBytes());
                Contabilidade vo = contabilidadeRepository.save(contabilidade.get());
                return ResponseEntity.ok().body(new ContabilidadeDto(vo));
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/logo")
    public ResponseEntity<InputStreamResource> download(@RequestParam Long id, @RequestParam(required = false) String forceDownload) throws IOException {
        Optional<Contabilidade> contabilidade = contabilidadeRepository.findById(id);
        if(contabilidade.isPresent()) {
            byte[] arquivo = contabilidade.get().getLogo();

            HttpHeaders httpHeaders = new HttpHeaders();
            if(forceDownload != null && forceDownload.equals("S")) {
                httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"logo.jpg\"");
            }

            InputStream is = new ByteArrayInputStream(arquivo);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .headers(httpHeaders)
                    .body(new InputStreamResource(is));
        }

        return ResponseEntity.notFound().build();
    }
}
