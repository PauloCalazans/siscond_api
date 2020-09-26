package br.com.siscond.repository;

import br.com.siscond.modelo.Morador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoradorRepository extends JpaRepository<Morador, Long> { }
