package com.example.desafiosenior.application.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Checkin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name="hospede_id")
	private Hospede hospede;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm", shape=JsonFormat.Shape.STRING)
	private LocalDateTime dataEntrada;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm", shape=JsonFormat.Shape.STRING)
	private LocalDateTime dataSaida;
	
	private boolean adicionalVeiculo;

}
