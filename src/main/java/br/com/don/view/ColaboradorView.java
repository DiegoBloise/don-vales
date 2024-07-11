package br.com.don.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.primefaces.PrimeFaces;

import br.com.don.enums.TipoChavePix;
import br.com.don.enums.TipoColaborador;
import br.com.don.enums.TipoVale;
import br.com.don.model.Colaborador;
import br.com.don.model.Entregador;
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
public class ColaboradorView implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TipoColaborador> tipoColaborador;

	private List<TipoChavePix> tipoChavePix;

	private Colaborador colaboradorSelecionado;

	private List<Colaborador> colaboradores;

	private List<Colaborador> colaboradoresSelecionados;

	private Vale valeSelecionado;

	private List<Vale> vales;

	private BigDecimal totalVales = new BigDecimal(0);

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private ValeService valeService;

	@Inject
	private UserFiles userFiles;


	@PostConstruct
	public void init() {

		tipoColaborador = Arrays.asList(TipoColaborador.values());
		tipoChavePix = Arrays.asList(TipoChavePix.values());

		colaboradores = colaboradorService.listarPorNome();
        colaboradoresSelecionados = new ArrayList<>();

		vales = valeService.listar();
	}


	public void adicionarVale(Colaborador colaborador) {
		colaboradorSelecionado = colaborador;
		valeSelecionado = new Vale();
		valeSelecionado.setData(LocalDate.now());
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

        PrimeFaces.current().ajax().update("@root:@id(dt-colaboradores)");

        init();
    }


	public void deletarValeSelecionado() {
        vales.remove(valeSelecionado);

        valeService.deletarVale(valeSelecionado);

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vale Removido", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        valeSelecionado = null;
    }


	public void deletarVale() {
		valeService.deletarVale(valeSelecionado);

		vales();
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


	public void adicionarColaborador() {
        colaboradorSelecionado = new Colaborador();
    }


    public void salvarColaborador() {
		if (colaboradorSelecionado.getId() == null) {

			if(colaboradorSelecionado.getTipo().equals(TipoColaborador.ENTREGADOR)) {
				colaboradorSelecionado = new Entregador(colaboradorSelecionado);
			}

			colaboradorService.cadastrarColaborador(colaboradorSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Cadastrado", "Colaborador: " + colaboradorSelecionado.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
			colaboradorService.atualizarColaborador(colaboradorSelecionado);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Atualizado", "Colaborador: " + colaboradorSelecionado.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        PrimeFaces.current().executeScript("PF('ColaboradorDialog').hide()");
        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        init();
    }


	public void deletarColaboradorSelecionado() {
        colaboradores.remove(colaboradorSelecionado);

        colaboradoresSelecionados.remove(colaboradorSelecionado);

		colaboradorService.removerColaborador(colaboradorSelecionado);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaborador Removido", "Colaborador: " + colaboradorSelecionado.getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");

        colaboradorSelecionado = null;
    }


	public void deletarColaboradoresSelecionados() {
        colaboradores.removeAll(colaboradoresSelecionados);

		colaboradorService.removerColaboradores(colaboradoresSelecionados);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Colaboradores Removidos", "Todas os colaboradores selecionados foram removidos.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("form:dt-colaboradores");
        PrimeFaces.current().executeScript("PF('dtColaboradores').clearFilters()");

        colaboradoresSelecionados.clear();
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


	public List<Vale>valesDoColaborador(Colaborador colaborador) {
		return valeService.buscarPorColaborador(colaborador);
	}


	public BigDecimal totalDoColaborador(Colaborador colaborador) {
		BigDecimal total = new BigDecimal(0);

		List<Vale> vales = valeService.buscarPorColaborador(colaborador);

		for (Vale vale : vales) {
			total = vale.getValor().add(total);
		}

		return total;
	}


	public void cancelarVale() {
		valeSelecionado = null;
	}


	public void cancelarColaborador() {
		colaboradorSelecionado.getPix().setTipo(null);
		colaboradorSelecionado = null;
	}


    public String getDeleteButtonMessage() {
        if (hasColaboradoresSelecionados()) {
            int size = colaboradoresSelecionados.size();
            return size > 1 ? size + " colaboradores selecionados" : "1 colaborador selecionado";
        }

        return "Remover";
    }


    public boolean hasColaboradoresSelecionados() {
        return colaboradoresSelecionados != null && !colaboradoresSelecionados.isEmpty();
    }


	public List<TipoVale> getTiposVales() {
		return Arrays.asList(TipoVale.values());
	}
}
