package br.com.don.repository;

import java.time.LocalDate;
import java.util.List;

import br.com.don.model.Entregador;
import jakarta.inject.Named;
import jakarta.persistence.NoResultException;

@Named
public class EntregadorRepository extends GenericRepository<Entregador, Long> {

    public List<Entregador> listarPorNome() {
        String jpql = "SELECT e FROM Entregador e ORDER BY e.nome";
        return findDTOs(jpql, Entregador.class);
    }


    public List<Entregador> listarEntregadoresPorData(LocalDate data) {
        String jpql = "SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data = :data";
        try {
			return entityManager.createQuery(jpql, Entregador.class)
				.setParameter("data", data)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
    }


    public List<Entregador> listarEntregadoresPorDataInicioFim(LocalDate dataInicio, LocalDate dataFim) {
        String jpql = "SELECT DISTINCT e.entregador FROM Entrega e WHERE e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.entregador.nome";
        try {
			return entityManager.createQuery(jpql, Entregador.class)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
    }
}
