package br.com.don.repository;

import java.time.LocalDate;
import java.util.List;

import br.com.don.model.Entrega;
import br.com.don.model.Entregador;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Named
public class EntregaRepository extends GenericRepository<Entrega, Long> {

	@Inject
    private EntityManager entityManager;


	public Entrega buscarPorPedidoDataEntregador(Integer pedido, LocalDate data, Entregador entregador) {
		String jpql = "SELECT e from Entrega e where e.pedido = :pedido AND e.data = :data AND e.entregador = :entregador";
		try {
			return (Entrega) entityManager.createQuery(jpql)
				.setParameter("pedido", pedido)
				.setParameter("data", data)
				.setParameter("entregador", entregador)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Entrega> buscarPorEntregadorData(Entregador entregador, LocalDate data){
		String jpql = "select e from Entrega e where e.entregador = :entregador AND e.data = :data";
		try {
			return entityManager.createQuery(jpql, Entrega.class)
				.setParameter("entregador", entregador)
				.setParameter("data", data).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Entrega> buscarPorEntregadorDataInicioDataFim(Entregador entregador, LocalDate dataInicio, LocalDate dataFim){
		String jpql = "select e from Entrega e where e.entregador = :entregador AND e.data >= :dataInicio AND e.data <= :dataFim order by e.data";
		try {
			return entityManager.createQuery(jpql, Entrega.class)
				.setParameter("entregador", entregador)
				.setParameter("dataInicio", dataInicio)
				.setParameter("dataFim", dataFim)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Entrega> buscarPorData(LocalDate data){
		String jpql = "select e from Entrega e where e.data = :data";
		try {
			return entityManager.createQuery(jpql, Entrega.class)
				.setParameter("data", data)
				.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public Long buscarMovimento(LocalDate data) {
		String jpql = "select count(e) from Entrega e where e.data = :data";
		try {
			return (Long) entityManager.createQuery(jpql)
				.setParameter("data", data)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public Integer deletarMovimento(LocalDate data) {
		String jpql = "delete from Entrega e where e.data = :data";
		try {
			Integer retorno;
			entityManager.getTransaction().begin();
			retorno = entityManager.createQuery(jpql).setParameter("data", data).executeUpdate();
			entityManager.getTransaction().commit();
			return retorno;
		} catch (NoResultException e) {
			return null;
		}
	}
}
