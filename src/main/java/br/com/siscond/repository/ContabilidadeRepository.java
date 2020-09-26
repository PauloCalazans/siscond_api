package br.com.siscond.repository;

import br.com.siscond.modelo.Contabilidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContabilidadeRepository extends JpaRepository<Contabilidade, Long> { }
