package com.example.desafiosenior.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.desafiosenior.application.models.Hospede;

public interface HospedeRepository extends CrudRepository<Hospede, Long> {
	
	@Query("select h from Hospede h where h.nome like %?1%")
	List<Hospede> findByNomeLike(String nome);
	
	Optional<Hospede> findByDocumentoEquals(String documento);
	
	Optional<Hospede> findByTelefoneEquals(String telefone);

}
