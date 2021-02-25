package com.example.desafiosenior.application.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.desafiosenior.application.dto.HospedeDTO;
import com.example.desafiosenior.application.services.IHospedeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/hospede")
public class HospedeController {

	// Sei que é uma boa prática não depender da camada de acesso a dados no
	// controller, o correto seria criar métodos na camada de service para isso, mas
	// para otimizar o tempo utilizei as querys personalizadas do jpa.
	/*
	 * @Autowired private HospedeRepository hospedeRepo;
	 */

	@Autowired
	private IHospedeService service;

	@GetMapping
	public List<HospedeDTO> findAllHospedes() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<HospedeDTO> findHospedeById(@PathVariable(value = "id") long id) {
		HospedeDTO ret = service.findById(id);

		if (ret != null) {
			return ResponseEntity.ok().body(ret);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Void> saveHospede(@RequestBody HospedeDTO hospede) {
		HospedeDTO ret = service.save(hospede);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ret.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<HospedeDTO>> findHospedeByNome(@PathVariable(value = "nome") String nome) {
		List<HospedeDTO> ret = service.findByNomeLike(nome);

		if (ret.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(ret);
	}

	@GetMapping("/documento/{documento}")
	public ResponseEntity<HospedeDTO> findHospedeByDocumento(@PathVariable(value = "documento") String documento) {
		HospedeDTO ret = service.findByDocumentoEquals(documento);

		if (ret == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(ret);
	}

	@GetMapping("/telefone/{telefone}")
	public ResponseEntity<HospedeDTO> findHospedeByTelefone(@PathVariable(value = "telefone") String telefone) {
		HospedeDTO ret = service.findByTelefoneEquals(telefone);

		if (ret == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body(ret);
	}

	@GetMapping("dentrohotel")
	public List<HospedeDTO> getHospedesDentroHotel() {
		return service.getHospedesDentroHotel();
	}

	@GetMapping("forahotel")
	public List<HospedeDTO> getHospedesForaDoHotel() {
		return service.getHospedesForaDoHotel();
	}

}
