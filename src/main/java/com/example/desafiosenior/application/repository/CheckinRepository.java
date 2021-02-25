package com.example.desafiosenior.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.desafiosenior.application.models.Checkin;

public interface CheckinRepository extends CrudRepository<Checkin, Long> {
	
	@Query(
			value="SELECT hospede_id\r\n"
					+ "FROM public.checkin\r\n"
					+ "WHERE \r\n"
					+ "	data_saida is null\r\n"
					+ "	or data_saida > now()",
			nativeQuery = true)
	List<Long> findHospedesIdsEstaoNoHotel();
	
	@Query(
			value="SELECT a.hospede_id\r\n"
					+ "FROM public.checkin a\r\n"
					+ "WHERE \r\n"
					+ "	a.data_saida is not null\r\n"
					+ "	and a.data_saida < now()\r\n"
					+ "	and a.hospede_id NOT IN (\r\n"
					+ "		SELECT b.hospede_id\r\n"
					+ "		FROM public.checkin b\r\n"
					+ "		WHERE \r\n"
					+ "			b.data_saida is null\r\n"
					+ "			or b.data_saida > now()			\r\n"
					+ "	);",
			nativeQuery = true)
	List<Long> findHospedesIdsNaoEstaoNoHotel();
	
	@Query(
			value = "select *\r\n"
					+ "from public.checkin c\r\n"
					+ "where \r\n"
					+ "	c.hospede_id = ?1",
			nativeQuery = true)
	List<Checkin> getCheckinsByHospedeId(long id);

}
