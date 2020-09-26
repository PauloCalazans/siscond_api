package br.com.siscond.repository;

import br.com.siscond.modelo.Contador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContadorRepository extends JpaRepository<Contador, Long> { }
