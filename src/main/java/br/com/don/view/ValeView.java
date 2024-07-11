package br.com.don.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;

import br.com.don.enums.TipoColaborador;
import br.com.don.enums.TipoVale;
import br.com.don.model.Colaborador;
import br.com.don.model.Vale;
import br.com.don.service.ColaboradorService;
import br.com.don.service.ValeService;
import br.com.don.session.UserFiles;
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
public class ValeView implements Serializable {

	private static final long serialVersionUID = 1L;

	private Vale valeSelecionado;

	private Colaborador colaboradorSelecionado;

	private List<Colaborador> colaboradores;

	private List<Vale> vales;

	private List<Vale> filteredVales;

	private List<FilterMeta> filterBy;

	private BigDecimal totalVales = new BigDecimal(0);

	@Inject
	private ValeService valeService;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private UserFiles userFiles;


    @PostConstruct
	public void init() {
		colaboradores = colaboradorService.listarPorNome();

		vales = valeService.listar();

		filterBy = new ArrayList<>();

		//.filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7))))

		LocalTime horaAtual = LocalTime.now();
		LocalDate data;
		if (horaAtual.isBefore(LocalTime.of(1, 20))) {
			data = LocalDate.now().minusDays(1);
		} else {
			data = LocalDate.now();
		}

        filterBy.add(FilterMeta.builder()
                .field("data")
                .filterValue(new ArrayList<>(Arrays.asList(data, data)))
                .matchMode(MatchMode.BETWEEN)
                .build());
	}


	public List<TipoVale> getTiposVales() {
		return Arrays.asList(TipoVale.values());
	}


	public List<String> getTiposColaboradores() {
		return Arrays.asList(TipoColaborador.values()).stream()
		.map(TipoColaborador::getDescricao)
		.toList();
	}


	public void adicionarVale(Colaborador colaborador) {
		colaboradorSelecionado = colaborador;
		valeSelecionado = new Vale();
	}


	public void adicionarSaldo(Colaborador colaborador) {
		adicionarVale(colaborador);
		valeSelecionado.setTipo(TipoVale.SALDO);
	}


	public void salvarVale() {
		try {
			valeSelecionado.setColaborador(colaboradorSelecionado);

			vales();

	        prepararValeImpressao();

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		if (valeSelecionado.getId() == null) {
            vales.add(valeSelecionado);

			valeService.cadastrarVale(valeSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Lançado", "Valor: R$ " + valeSelecionado.getValor().toString().replace(".", ","));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
			valeService.atualizarVale(valeSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Atualizado", "Valor: R$ " + valeSelecionado.getValor().toString().replace(".", ","));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

		PrimeFaces.current().executeScript("PF('valeDialog').hide();");

		PrimeFaces.current().ajax().update("@root:@id(dt-vales)");

        init();
    }


	public void deletarValeSelecionado() {
        vales.remove(valeSelecionado);

        valeService.deletarVale(valeSelecionado);

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Removido", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("@root:@id(dt-vales)");

        valeSelecionado = null;
    }


	public void vales(){
		valeSelecionado.setColaborador(colaboradorSelecionado);

		vales = valeService.buscarPorColaborador(colaboradorSelecionado);

		calcularTotalVale(vales);
	}


	public void calcularTotalVale(List<Vale> vales) {
		this.totalVales = new BigDecimal(0);

		for (Vale vale : vales) {
			this.totalVales = vale.getValor().add(this.totalVales);
		}
	}


	public void cancelarVale() {
		valeSelecionado = null;
	}


	public void prepararValeImpressao() {
		StringBuilder conteudo = new StringBuilder()
		.append(valeSelecionado.getColaborador().getNome())
		.append(System.lineSeparator())
		.append(valeSelecionado.getValor())
		.append(System.lineSeparator())
		.append(valeSelecionado.getDataFormatada());

		InputStream inputStream = new ByteArrayInputStream(conteudo.toString().getBytes());

		userFiles.setInputStream(inputStream);
	}


	public List<Colaborador> completeColaborador(String query) {
        String queryLowerCase = query.toLowerCase();
        return colaboradores.stream().filter(t -> t.getNome().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }
}