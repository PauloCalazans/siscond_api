package br.com.siscond.repository;

import br.com.siscond.modelo.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CondominioRepository extends JpaRepository<Condominio, Long> { }
