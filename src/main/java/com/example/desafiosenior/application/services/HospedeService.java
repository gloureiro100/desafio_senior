package com.example.desafiosenior.application.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.desafiosenior.application.dto.HospedeDTO;
import com.example.desafiosenior.application.models.Checkin;
import com.example.desafiosenior.application.models.Hospede;
import com.example.desafiosenior.application.repository.CheckinRepository;
import com.example.desafiosenior.application.repository.HospedeRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class HospedeService implements IHospedeService {

	@Autowired
	private CheckinRepository checkinRepo;

	@Autowired
	private HospedeRepository hospedeRepo;

	static final double DIARIA_SEMANA = 120;
	static final double DIARIA_FDS = 150;
	static final double TAXA_CARRO_SEMANA = 15;
	static final double TAXA_CARRO_FDS = 20;
	static final LocalTime HORA_LIMITE = LocalTime.of(16, 30);

	@Override
	public HospedeDTO findById(long id) {
		Optional<Hospede> ret = hospedeRepo.findById(id);

		if (ret.isPresent())
			return MapAndCalcDTO(ret.get());

		return null;
	}

	@Override
	public List<HospedeDTO> findAll() {
		List<Hospede> entityList = (List<Hospede>) hospedeRepo.findAll();
		List<HospedeDTO> dtoList = new ArrayList<HospedeDTO>();

		entityList.forEach(e -> {
			dtoList.add(MapAndCalcDTO(e));
		});

		return dtoList;
	}

	@Override
	public List<HospedeDTO> getHospedesDentroHotel() {
		List<Long> idList = checkinRepo.findHospedesIdsEstaoNoHotel();
		List<HospedeDTO> hospedes = new ArrayList<HospedeDTO>();

		idList.forEach(id -> hospedes.add(findById(id)));

		return hospedes;
	}

	@Override
	public List<HospedeDTO> getHospedesForaDoHotel() {
		List<Long> idList = checkinRepo.findHospedesIdsNaoEstaoNoHotel();
		List<HospedeDTO> hospedes = new ArrayList<HospedeDTO>();

		idList.forEach(id -> hospedes.add(findById(id)));

		return hospedes;
	}

	private HospedeDTO calcularValor(HospedeDTO dto) {

		List<Checkin> checkins = checkinRepo.getCheckinsByHospedeId(dto.getId());

		double valorTotal = 0;
		double ultimoValor = 0;

		// Predicado para verificar se é os dias do período são fim de semana
		Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;

		ordenarListaCheckin(checkins);

		for (Checkin checkin : checkins) {
			double valorParcial = 0;
			LocalDate startDate, endDate;
			startDate = checkin.getDataEntrada().toLocalDate();
			endDate = checkin.getDataSaida().toLocalDate();

			// Caso o horário do checkin passe da hora limite, uma nova diária será cobrada
			if (checkin.getDataSaida().toLocalTime().isAfter(HORA_LIMITE))
				endDate = endDate.plusDays(1);

			long totalDays = ChronoUnit.DAYS.between(startDate, endDate);

			if (totalDays == 0) // caso onde fez o checkin mas saiu no mesmo dia.
				totalDays = 1;
			else if (totalDays > 1) // se a entrada e saída não ocorreram em dias consecutivos, esse passo é
									// necessário
				totalDays++;

			long weekendDays = Stream.iterate(startDate, date -> date.plusDays(1)).limit(totalDays).filter(isWeekend)
					.count();

			long weekDays = totalDays - weekendDays;

			valorParcial = (weekDays * DIARIA_SEMANA) + (weekendDays * DIARIA_FDS);

			if (checkin.isAdicionalVeiculo()) {
				double taxaVeiculoSemana = weekDays * TAXA_CARRO_SEMANA;
				double taxaVeiculoFinalSemana = weekendDays * TAXA_CARRO_FDS;

				valorParcial += (taxaVeiculoFinalSemana + taxaVeiculoSemana);
			}

			valorTotal += valorParcial;
			ultimoValor = valorParcial;
		}

		dto.setTotal(valorTotal);
		dto.setUltima(ultimoValor);

		return dto;
	}

	private void ordenarListaCheckin(List<Checkin> lista) {
		// Ordena a lista pela data de entrada do checkin
		lista.sort((c1, c2) -> {
			LocalDate d1, d2;
			d1 = c1.getDataEntrada().toLocalDate();
			d2 = c2.getDataEntrada().toLocalDate();

			if (d1.isEqual(d2)) {
				return 0;
			} else if (d1.isBefore(d2)) {
				return -1;
			}

			return 1;
		});
	}

	private HospedeDTO entityToDTO(Hospede entity) {
		HospedeDTO dto = new HospedeDTO();

		dto.setId(entity.getId());
		dto.setDocumento(entity.getDocumento());
		dto.setNome(entity.getNome());
		dto.setTelefone(entity.getTelefone());

		return dto;
	}

	private Hospede DTOToEntity(HospedeDTO dto) {
		Hospede entity = new Hospede();

		entity.setId(dto.getId());
		entity.setDocumento(dto.getDocumento());
		entity.setNome(dto.getNome());
		entity.setTelefone(dto.getTelefone());

		return entity;
	}

	private HospedeDTO MapAndCalcDTO(Hospede entity) {
		HospedeDTO dto = entityToDTO(entity);
		calcularValor(dto);

		return dto;
	}

	@Override
	public HospedeDTO save(HospedeDTO hospede) {
		return entityToDTO(hospedeRepo.save(DTOToEntity(hospede)));
	}

	@Override
	public List<HospedeDTO> findByNomeLike(String nome) {
		return hospedeRepo.findByNomeLike(nome).stream().map(e -> MapAndCalcDTO(e)).collect(Collectors.toList());
	}

	@Override
	public HospedeDTO findByDocumentoEquals(String documento) {
		Optional<Hospede> ret = hospedeRepo.findByDocumentoEquals(documento);

		if (ret.isPresent())
			return MapAndCalcDTO(ret.get());

		return null;
	}

	@Override
	public HospedeDTO findByTelefoneEquals(String telefone) {
		Optional<Hospede> ret = hospedeRepo.findByTelefoneEquals(telefone);

		if (ret.isPresent())
			return MapAndCalcDTO(ret.get());

		return null;
	}

}
