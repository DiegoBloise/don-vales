package br.com.don.repository;

import java.util.List;

import br.com.don.model.Taxa;
import jakarta.inject.Named;
import jakarta.persistence.NoResultException;

@Named
public class TaxaRepository extends GenericRepository<Taxa, Long> {

    public List<Taxa> getTaxas(){
		String jpql = "SELECT t from Taxa t order by t.qtdEntregasPersonalizado desc";
		try {
			return entityManager.createQuery(jpql, Taxa.class)
				.getResultList();
		} catch (NoResultException e) {
            return null;
        }
	}
}
