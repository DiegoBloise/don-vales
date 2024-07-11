package br.com.don.service;

import java.io.Serializable;
import java.util.List;

import br.com.don.enums.TipoColaborador;
import br.com.don.model.Colaborador;
import br.com.don.repository.ColaboradorRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class ColaboradorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ColaboradorRepository repository;


	public List<Colaborador> listar(){
		return repository.findAll();
	}


	public List<Colaborador> listarPorNome() {
        String jpql = "SELECT e FROM Colaborador e ORDER BY e.nome";
        return repository.findDTOs(jpql, Colaborador.class);
    }


	public Colaborador buscar(Colaborador colaborador){
		return repository.find(colaborador);
	}


	public List<Colaborador> buscarPorNome(String nome){
		return repository.findAllByProperty("nome", nome);
	}


	public List<Colaborador> listarPorTipo(TipoColaborador tipo) {
        return repository.findAllByProperty("tipo", tipo);
    }


	public List<Colaborador> listarPorTipoOrderByNome(TipoColaborador tipo) {
        String jpql = "SELECT c FROM Colaborador c WHERE c.tipo = "+tipo+" ORDER BY c.nome";
		return repository.findDTOs(jpql, Colaborador.class);
    }


	public void cadastrarColaborador(Colaborador colaborador) {
		repository.save(colaborador);
	}


	public void atualizarColaborador(Colaborador colaborador) {
		repository.update(colaborador);
	}


	public void removerColaborador(Colaborador colaborador) {
		repository.delete(colaborador);
	}


	public void removerColaboradores(List<Colaborador> colaboradores) {
		for (Colaborador colaborador : colaboradores) {
			this.removerColaborador(colaborador);
		}
	}
}
