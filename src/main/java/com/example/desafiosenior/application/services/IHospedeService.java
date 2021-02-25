package com.example.desafiosenior.application.services;

import java.util.List;

import com.example.desafiosenior.application.dto.HospedeDTO;

public interface IHospedeService {

	List<HospedeDTO> getHospedesDentroHotel();

	List<HospedeDTO> getHospedesForaDoHotel();

	HospedeDTO findById(long id);

	List<HospedeDTO> findAll();

	HospedeDTO save(HospedeDTO hospede);

	List<HospedeDTO> findByNomeLike(String nome);

	HospedeDTO findByDocumentoEquals(String documento);

	HospedeDTO findByTelefoneEquals(String telefone);

}
