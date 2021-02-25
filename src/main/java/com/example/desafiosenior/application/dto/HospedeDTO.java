package com.example.desafiosenior.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HospedeDTO {

	private long id;
	private String nome;
	private String documento;
	private String telefone;
	private double total;
	private double ultima;

}
