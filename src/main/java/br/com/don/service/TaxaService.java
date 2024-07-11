package br.com.don.service;

import java.io.Serializable;
import java.util.List;

import br.com.don.model.Taxa;
import br.com.don.repository.TaxaRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class TaxaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TaxaRepository repository;


	public List<Taxa> getTaxas() {
		return repository.getTaxas();
	}


	public void cadastrarTaxa(Taxa taxa) {
		repository.save(taxa);
	}


	public void atualizarTaxa(Taxa taxa) {
		repository.update(taxa);
	}


	public void deletarTaxa(Taxa taxa) {
		repository.delete(taxa);
	}
}
