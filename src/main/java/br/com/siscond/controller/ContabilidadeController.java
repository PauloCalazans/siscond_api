package br.com.siscond.controller;

import br.com.siscond.controller.dto.ContabilidadeDto;
import br.com.siscond.controller.form.ContabilidadeForm;
import br.com.siscond.modelo.Contabilidade;
import br.com.siscond.repository.ContabilidadeRepository;
import br.com.siscond.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
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
@RequestMapping("/contabilidades")
public class ContabilidadeController {

    @Autowired
    ContabilidadeRepository contabilidadeRepository;

    @Autowired
    EnderecoRepository enderecoRepository;
    @GetMapping
    public ResponseEntity<Page<ContabilidadeDto>> listar(Pageable pageable) {
        Page<Contabilidade> contabilidades = contabilidadeRepository.findAll(pageable);
        return ResponseEntity.ok(ContabilidadeDto.converter(contabilidades));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ContabilidadeDto> cadastrar(@RequestBody @Valid ContabilidadeForm vo, UriComponentsBuilder uriBuilder) {
        Contabilidade contabilidade = vo.converter(enderecoRepository);
        contabilidadeRepository.save(contabilidade);
        URI uri = uriBuilder.path("/contabilidades/{id}").buildAndExpand(contabilidade.getId()).toUri();
        return ResponseEntity.created(uri).body(new ContabilidadeDto(contabilidade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContabilidadeDto> detalhar(@PathVariable Long id) {
        Optional<Contabilidade> contabilidade = contabilidadeRepository.findById(id);
        if(contabilidade.isPresent()) {
            return ResponseEntity.ok(new ContabilidadeDto(contabilidade.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Contabilidade> contabilidade = contabilidadeRepository.findById(id);
        if(contabilidade.isPresent()) {
            contabilidadeRepository.delete(contabilidade.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ContabilidadeDto> atualizar(@PathVariable Long id, @RequestBody @Valid ContabilidadeForm vo) {
        Optional<Contabilidade> optionalContabilidade = contabilidadeRepository.findById(id);

        if(optionalContabilidade.isPresent()) {
            Contabilidade contabilidade = vo.converter(enderecoRepository);
            contabilidade.setId(optionalContabilidade.get().getId());
            contabilidade = contabilidadeRepository.save(contabilidade);
            return ResponseEntity.ok(new ContabilidadeDto(contabilidade));
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
        if(contabilidade.isPresent() && contabilidade.get().getLogo() != null) {
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
