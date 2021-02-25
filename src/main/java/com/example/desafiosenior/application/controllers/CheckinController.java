package com.example.desafiosenior.application.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.desafiosenior.application.models.Checkin;
import com.example.desafiosenior.application.repository.CheckinRepository;

@RestController
@RequestMapping("api/checkin")
public class CheckinController {

	@Autowired
	private CheckinRepository repo;

	@GetMapping
	public List<Checkin> findAllCheckins() {
		return (List<Checkin>) repo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Checkin> findCheckinById(@PathVariable(value = "id") long id) {
		Optional<Checkin> ret = repo.findById(id);

		if (ret.isPresent()) {
			return ResponseEntity.ok().body(ret.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public Checkin saveHospede(@RequestBody Checkin checkin) {
		return repo.save(checkin);
	}

}