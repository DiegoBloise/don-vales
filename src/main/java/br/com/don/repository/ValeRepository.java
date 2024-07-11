package br.com.don.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.don.enums.TipoVale;
import br.com.don.model.Colaborador;
import br.com.don.model.Vale;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Named
public class ValeRepository extends GenericRepository<Vale, Long> {

	@Inject
    private EntityManager entityManager;


	public List<Vale> listarOrdenadoPorData(){
		String jpql = "select v from Vale v order by v.data desc, id";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> listarOrdenadoPorId(){
		String jpql = "select v from Vale v order by v.id desc";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorColaboradorDataTipo(Colaborador colaborador, LocalDate data, TipoVale tipo){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data = :data AND v.tipo = :tipo";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("data", data)
				.setParameter("tipo", tipo)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorData(LocalDate data){
		String jpql = "select v from Vale v where v.data = :data";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("data", data)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data >= :dataInicio AND v.data <= :dataFim";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public List<Vale> buscarPorColaboradorDataInicioFimTipo(Colaborador colaborador, LocalDate dataInicio, LocalDate dataFim, TipoVale tipo){
		String jpql = "select v from Vale v where v.colaborador = :colaborador AND v.data >= :dataInicio AND v.data <= :dataFim AND v.tipo = :tipo";
		try {
			return entityManager.createQuery(jpql, Vale.class)
				.setParameter("colaborador", colaborador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.setParameter("tipo", tipo)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}


	public BigDecimal buscarTotalDiaColaborador(LocalDate data, Colaborador colaborador) {
		String jpql = "select sum(v.valor) from Vale v where v.colaborador = :colaborador AND v.data = :data AND v.tipo = :tipo";
		BigDecimal total = null;

		try {
			total = entityManager.createQuery(jpql, BigDecimal.class)
				.setParameter("data", data)
				.setParameter("colaborador", colaborador)
				.setParameter("tipo", TipoVale.DINHEIRO)
				.getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return total != null ? total : new BigDecimal(0);
	}

}
