package br.com.don.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.don.enums.TipoVale;
import br.com.don.model.Colaborador;
import br.com.don.model.Vale;
import br.com.don.repository.ValeRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
public class ValeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ValeRepository repository;


	/* public List<Vale> findVales() {
        String jpql = "SELECT NEW br.com.don.model.Vale(v.id, v.nome) FROM Vale v";
        return findDTOs(jpql, Cliente.class);
    } */


	public List<Vale> listar() {
		return repository.findAll();
	}


	public List<Vale> listarOrdenadoPorData() {
		return repository.listarOrdenadoPorData();
	}


	public List<Vale> listarOrdenadoPorId() {
		return repository.listarOrdenadoPorId();
	}


	public void cadastrarVale(Vale vale) {
		repository.save(vale);
	}


	public void atualizarVale(Vale vale) {
		repository.update(vale);
	}


	public List<Vale> buscarPorColaborador(Colaborador colaborador){
		return repository.findAllByProperty("colaborador", colaborador);
	}


	public List<Vale> buscarPorColaboradorDataTipo(Colaborador colaborador, LocalDate data,TipoVale tipo){
		return repository.buscarPorColaboradorDataTipo(colaborador, data, tipo);
	}


	public List<Vale> buscarPorData(LocalDate data){
		return repository.buscarPorData(data);
	}


	public List<Vale> buscarPorColaboradorDataInicioFim(Colaborador colaborador,LocalDate dataInicio,LocalDate dataFim){
		return repository.buscarPorColaboradorDataInicioFim(colaborador, dataInicio, dataFim);
	}


	public List<Vale> buscarPorColaboradorDataInicioFimTipo(Colaborador colaborador, LocalDate dataInicio, LocalDate dataFim, TipoVale tipo){
		return repository.buscarPorColaboradorDataInicioFimTipo(colaborador, dataInicio, dataFim, tipo);
	}


	public BigDecimal buscarTotalDiaColaborador(LocalDate data, Colaborador colaborador) {
		return repository.buscarTotalDiaColaborador(data, colaborador);
	}


	public void deletarVale(Vale vale) {
		repository.delete(vale);
	}
}
