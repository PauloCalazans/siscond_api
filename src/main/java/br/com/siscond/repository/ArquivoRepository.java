package br.com.siscond.repository;

import br.com.siscond.modelo.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> { }
