package br.com.don.view.admin;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import br.com.don.model.Entrega;
import br.com.don.model.Entregador;
import br.com.don.service.EntregaService;
import br.com.don.service.UploadService;
import br.com.don.util.Util;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

@Data
@Named
@ViewScoped
public class UploadView implements Serializable {

	private static final long serialVersionUID = 1L;

    private Date dataSelecionada;

    private LocalDate dataMovimento;

    private List<Entrega> entregas;

    private List<Entregador> entregadores;

	@Inject
	private UploadService uploadService;

	@Inject
	private EntregaService entregaService;


    @PostConstruct
	public void init() {
        entregas = entregaService.listar();
        entregadores = new ArrayList<>();

		dataSelecionada = new Date();
        dataMovimento = Util.converteLocalDate(dataSelecionada);
	}


    public void apagarMovimento() {
		Integer retorno = 0;
		retorno = uploadService.deleterMovimento(dataMovimento);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Data do movimento apagada!",
						Util.localDateFormatado(dataMovimento) + System.lineSeparator() + retorno
								+ " registros foram apagados!"));
	}


    public void fileUpload(FileUploadEvent event) {

		try {

			Long verificaData = uploadService.buscarMovimento(dataMovimento);

			if (verificaData != null && verificaData > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Já existe um movimento com a data selecionada!", Util.localDateFormatado(dataMovimento)));
			} else {
				UploadedFile file = event.getFile();

				Integer entregasSalvas = uploadService.trataXML2(file.getInputStream());

				entregas = uploadService.buscarPorData(dataMovimento);
				entregadores = uploadService.listarEntregadoresPorData(dataMovimento);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Foram salvos "+ (entregasSalvas + " registros"), Util.localDateFormatado(dataMovimento)));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


   /*  public void onDateSelect(SelectEvent<Date> event) {
		dataMovimento = dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		entregadores = entregaService.listarEntregadoresPorData(dataMovimento);
		/*
		 * dataMovimento =
		 * dataSelecionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		 * System.out.println("Local date " + dataMovimento);
		 * System.out.println(getDataMovimentoFormatado()); FacesContext facesContext =
		 * FacesContext.getCurrentInstance(); SimpleDateFormat format = new
		 * SimpleDateFormat("dd/MM/yyyy"); facesContext.addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_INFO, "Data Selecioanda ",
		 * format.format(event.getObject())));
		 */
		//entregas = entregaService.buscarPorEntregador(getEntregadorSelecionado());
		//entregadores = entregaService.listarEntregadores();

	//} */
}
