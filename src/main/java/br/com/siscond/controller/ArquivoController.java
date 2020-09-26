package br.com.siscond.controller;

import br.com.siscond.controller.dto.ArquivoDto;
import br.com.siscond.controller.form.ArquivoForm;
import br.com.siscond.modelo.Arquivo;
import br.com.siscond.modelo.Morador;
import br.com.siscond.repository.ArquivoRepository;
import br.com.siscond.repository.MoradorRepository;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/arquivos")
public class ArquivoController {

    @Autowired
    ArquivoRepository arquivoRepository;

    @Autowired
    MoradorRepository moradorRepository;

    @GetMapping
    public ResponseEntity<Page<ArquivoDto>> listar(Pageable pageable) {
        Page<Arquivo> arquivos = arquivoRepository.findAll(pageable);
        return ResponseEntity.ok(ArquivoDto.converter(arquivos));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ArquivoDto> cadastrar(@RequestParam MultipartFile boleto, @RequestParam Long idMorador, @RequestParam(required = false)  String observacao, UriComponentsBuilder uriBuilder) throws IOException {
        Optional<Morador> moradorOptional = moradorRepository.findById(idMorador);
        if(moradorOptional.isPresent()) {
            Arquivo arquivo = new Arquivo();
            arquivo.setMorador(moradorOptional.get());
            arquivo.setBoleto(boleto.getBytes());
            arquivo.setObservacao(observacao);

            arquivoRepository.save(arquivo);
            URI uri = uriBuilder.path("/arquivos/{id}").buildAndExpand(arquivo.getId()).toUri();
            return ResponseEntity.created(uri).body(new ArquivoDto(arquivo));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Arquivo> detalhar(@PathVariable Long id) {
        Optional<Arquivo> optionalArquivo = arquivoRepository.findById(id);
        return optionalArquivo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @ApiOperation(value = "Atualiza o morador e/ou a observação, para atualizar o arquivo utilizar o upload.")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ArquivoDto> atualizar(@PathVariable Long id, @RequestBody @Valid ArquivoForm vo) {
        Optional<Arquivo> optionalArquivo = arquivoRepository.findById(id);

        if(optionalArquivo.isPresent()) {
            Arquivo arquivo = vo.converter(moradorRepository);
            arquivo.setId(optionalArquivo.get().getId());
            return ResponseEntity.ok(new ArquivoDto(arquivoRepository.save(arquivo)));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        Optional<Arquivo> arquivoOptional = arquivoRepository.findById(id);
        if(arquivoOptional.isPresent()) {
            arquivoRepository.delete(arquivoOptional.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Recebe um arquivo como multipartfile, e atualiza o registro a partir do id informado")
    @Transactional
    @PostMapping("/upload")
    public ResponseEntity<ArquivoDto> boleto(@RequestParam Long id, @RequestParam MultipartFile boleto) {
        Optional<Arquivo> arquivo = arquivoRepository.findById(id);
        if(arquivo.isPresent() && boleto.getOriginalFilename() != null && boleto.getOriginalFilename().endsWith(".pdf")) {
            try {
                arquivo.get().setBoleto(boleto.getBytes());
                Arquivo vo = arquivoRepository.save(arquivo.get());
                return ResponseEntity.ok().body(new ArquivoDto(vo));
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Retorna a visualização do arquivo, caso queira fazer o download informar 'S' para forceDownload")
    @GetMapping(value = "/boleto")
    public ResponseEntity<InputStreamResource> download(@RequestParam Long id, @RequestParam(required = false) String forceDownload) throws IOException {
        Optional<Arquivo> boleto = arquivoRepository.findById(id);

        if(boleto.isPresent()) {
            byte[] arquivo = boleto.get().getBoleto();
            InputStream is = new ByteArrayInputStream(arquivo);
            HttpHeaders httpHeaders = new HttpHeaders();

            if(forceDownload != null && forceDownload.equals("S")) {
                httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"boleto.pdf\"");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .headers(httpHeaders) // essa linha força o download
                    .body(new InputStreamResource(is));
        }

        return ResponseEntity.notFound().build();
    }
}
