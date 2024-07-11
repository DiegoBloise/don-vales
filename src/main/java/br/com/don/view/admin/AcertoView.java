package br.com.don.view.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.primefaces.PrimeFaces;

import br.com.don.enums.TipoColaborador;
import br.com.don.enums.TipoVale;
import br.com.don.model.Acerto;
import br.com.don.model.Colaborador;
import br.com.don.model.Entrega;
import br.com.don.model.Entregador;
import br.com.don.model.Taxa;
import br.com.don.model.Vale;
import br.com.don.service.ColaboradorService;
import br.com.don.service.EntregaService;
import br.com.don.service.EntregadorService;
import br.com.don.service.TaxaService;
import br.com.don.service.ValeService;
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
public class AcertoView implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Double TAXA_IFOOD = 3.00;

	private final String QUEBRALINHA = System.lineSeparator();

	private StringBuffer textoVale;
	private StringBuffer textoEntregas;

	private List<Vale> valesDinheiro;

	private List<Vale> valesSaldo;

	private List<Entrega> entregas;

	private List<Acerto> acertos;

	private BigDecimal salario;

	private List<TipoColaborador> tipoColaborador;

	private TipoColaborador tipoColaboradorSelecionado;

	private Colaborador colaboradorSelecionado;

	private Entregador entregadorSelecionado;

	private List<Entregador> entregadores;

	private List<Colaborador> colaboradores;

	private List<Entregador> entregadoresDt;

	private Date dataInicio;

	private Date dataFim;

	private Map<DayOfWeek, Boolean> valorDiaria;

	//private WhatsAppSender was;


	private LocalDate dataComparacao;
	private int qtdeTotalDias;
	private int qtdeIfoodDia;
	private int i;
	private int qtdeEntregaDia;
	private int qtdeTotalIFood;

	private BigDecimal valorTotalEntregas;
	private BigDecimal totalValesDia;
	private BigDecimal valorTotalDiarias;
	private BigDecimal valorTotalVales;
	private BigDecimal valorSaldo;

	private BigDecimal valorTotalIfood;
	private BigDecimal valorTotalSemDesconto;
	private BigDecimal valorTotalComDesconto;

	private List<Taxa> taxas;
	private Taxa taxaSelecionada;

	private Double valorFixoTaxa = 35.0;
	private Integer qtdEntregasFixoTaxa = 9;

	@Inject
	private EntregaService entregaService;

	@Inject
	private ColaboradorService colaboradorService;

	@Inject
	private ValeService valeService;

	@Inject
	private EntregadorService entregadorService;

	@Inject
	private TaxaService taxaService;


	@PostConstruct
	public void init() {
		entregadoresDt = entregadorService.listarPorNome();

		taxas = taxaService.getTaxas();

		valesDinheiro = new ArrayList<>();
		valesSaldo = new ArrayList<>();

		entregas = new ArrayList<>();
		acertos = new ArrayList<>();

		salario = new BigDecimal(0);

		entregadores = new ArrayList<>();
		colaboradores = new ArrayList<>();

		tipoColaboradorSelecionado = null;
		entregadorSelecionado = null;
		colaboradorSelecionado = null;

		dataInicio = new Date();
		dataFim = new Date();

		tipoColaborador = Arrays.asList(TipoColaborador.values());

		textoVale = null;
		textoEntregas = null;

		valorDiaria = new LinkedHashMap<>();
		for (DayOfWeek dia : DayOfWeek.values()) {
            valorDiaria.put(dia, false);
        }
	}


	public void realizarAcertoEntregador() {

		if(entregadorSelecionado.getPix().getChave() != null ) {

			try {
				dataComparacao = null;
				qtdeTotalDias = 0;
				qtdeIfoodDia = 0;
				i = 0;
				qtdeEntregaDia = 0;
				qtdeTotalIFood = 0;

				valorTotalEntregas = new BigDecimal(0.0);
				totalValesDia = new BigDecimal(0);
				valorTotalDiarias = new BigDecimal(0);
				valorTotalVales = new BigDecimal(0);
				valorSaldo = new BigDecimal(0);

				acertos = new ArrayList<>();
				entregas = new ArrayList<>();

				entregas = entregaService.buscarPorEntregadorDataInicioDataFim(
							(Entregador) entregadorSelecionado,
							Util.converteLocalDate(dataInicio),
							Util.converteLocalDate(dataFim));

				calcularEntregas();

				calcularVales(entregadorSelecionado);

				acertos.sort(Comparator.comparing(Acerto::getData));

				updateColaborador(entregadorSelecionado);

				createTextoVale(entregadorSelecionado);

				copyValeToClipboard();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro", e.getMessage()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Necessário cadastrar uma chave pix para continuar"));
		}
	}


	public void realizarAcertoFixo() {

		if(colaboradorSelecionado.getPix().getChave() != null ) {

			try {
				totalValesDia = new BigDecimal(0);
				valorTotalVales = new BigDecimal(0);
				valorSaldo = new BigDecimal(0);

				calcularVales(colaboradorSelecionado);

				updateColaborador(colaboradorSelecionado);

				createTextoVale(colaboradorSelecionado);

				copyValeToClipboard();

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro", e.getMessage()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Necessário cadastrar uma chave pix para continuar"));
		}
	}


	private void createTextoVale(Colaborador colaborador) {
		textoVale = new StringBuffer();

		textoVale.append("*")
				.append(colaborador.getNome())
				.append("*")
				.append(QUEBRALINHA);

		if (colaborador.getTipo() == TipoColaborador.ENTREGADOR && colaborador instanceof Entregador) {
			Entregador entregador = (Entregador) colaborador;
			for (Acerto acerto : acertos) {
				textoVale.append(acerto.toString());
			}

			textoVale
					.append(textoEntregas)
					.append("Total de Entregas: R$ ").append(entregador.getValorTotalEntregas())
					.append(QUEBRALINHA)
					.append("Total de Diárias: R$ ").append(entregador.getValorTotalDiarias())
					.append(QUEBRALINHA)
					.append("Total IFood: R$ ").append(entregador.getValorTotalIfood()).append(".00")
					.append(QUEBRALINHA);
		} else if (colaborador.getTipo() == TipoColaborador.FIXO) {
			// Se for um colaborador fixo, faça algo aqui se necessário
		}

		textoVale
				.append("Total Sem Desconto: R$ ").append(colaborador.getValorTotalSemDesconto())
				.append(QUEBRALINHA)
				.append("Total de Vales: R$ ").append(colaborador.getValorTotalVales())
				.append(QUEBRALINHA)
				.append("Saldo: R$ ").append(colaborador.getValorSaldo())
				.append(QUEBRALINHA)
				.append("*RECEBER* R$: ")
				.append(colaborador.getValorTotalComDesconto())
				.append(QUEBRALINHA).append("*OBRIGADO E DEUS ABENÇÕE*");
	}


	private void updateColaborador(Colaborador colaborador) {
		if (colaborador.getTipo() == TipoColaborador.ENTREGADOR && colaborador instanceof Entregador) {
			Entregador entregador = (Entregador) colaborador;

			valorTotalIfood = new BigDecimal(qtdeTotalIFood * TAXA_IFOOD);
			valorTotalSemDesconto = valorTotalDiarias.add(valorTotalEntregas).add(valorTotalIfood);
			valorTotalComDesconto = valorTotalSemDesconto.subtract(valorTotalVales).subtract(valorSaldo);

			entregador.setQtdTotalDias(qtdeTotalDias);
			entregador.setQtdEntregas(entregas.size());
			entregador.setValorTotalEntregas(valorTotalEntregas);
			entregador.setValorTotalIfood(valorTotalIfood);
			entregador.setValorTotalDiarias(valorTotalDiarias);
		} else if (colaborador.getTipo() == TipoColaborador.FIXO) {
			valorTotalSemDesconto = salario;
			valorTotalComDesconto = valorTotalSemDesconto.subtract(valorTotalVales).subtract(valorSaldo);
		}

		colaborador.setValorTotalSemDesconto(valorTotalSemDesconto);
		colaborador.setValorTotalComDesconto(valorTotalComDesconto);
		colaborador.setValorTotalVales(valorTotalVales);
		colaborador.setValorSaldo(valorSaldo);
	}


	public void processarValeAcertoEntregador(Vale vale) {

		Optional<Acerto> acertoEncontrado = acertos.stream()
				.filter(a -> a.getData().equals(vale.getData()))
				.findFirst();

		if (acertoEncontrado.isPresent()) {

			for (Acerto acerto : acertos) {
				if (acerto.getData().equals(vale.getData())) {
					acerto.setValorValeDia(valeService.buscarTotalDiaColaborador(vale.getData(), vale.getColaborador()));
				} else if (acerto.getValorValeDia() == null) {
					acerto.setValorValeDia(BigDecimal.valueOf(0));
				}
			}

		} else {
			Acerto novoAcerto = new Acerto();

			novoAcerto.setData(vale.getData());
			novoAcerto.setValorValeDia(valeService.buscarTotalDiaColaborador(vale.getData(), vale.getColaborador()));
			novoAcerto.setQtdeEntregasDia(0);
			novoAcerto.setQtdeIFood(0);

			acertos.add(novoAcerto);
		}
	}


	public void calcularVales(Colaborador colaborador) {
		valesDinheiro = valeService.buscarPorColaboradorDataInicioFimTipo(colaborador, Util.converteLocalDate(dataInicio), Util.converteLocalDate(dataFim), TipoVale.DINHEIRO);
		colaborador.setQtdVales(valesDinheiro.size());
		if (valesDinheiro != null) {
			for (Vale vale : valesDinheiro) {

				if (colaborador.getTipo() == TipoColaborador.ENTREGADOR && colaborador instanceof Entregador) {
					processarValeAcertoEntregador(vale);
				}

				valorTotalVales = vale.getValor().add(valorTotalVales);
			}
		}

		valesSaldo = valeService.buscarPorColaboradorDataInicioFimTipo(colaborador, Util.converteLocalDate(dataInicio), Util.converteLocalDate(dataFim), TipoVale.SALDO);
		if (null != valesSaldo) {
			for (Vale vale : valesSaldo) {
				valorSaldo = vale.getValor().add(valorSaldo);
			}
		}
	}


	public void calcularEntregas() {
		textoEntregas = new StringBuffer();
		for (Entrega ent : entregas) {
			i++;

			if (null == dataComparacao) {
				dataComparacao = ent.getData();
				++qtdeTotalDias;
			}

			if (ent.getValor().compareTo(new BigDecimal(0)) == 0) {
				++qtdeIfoodDia;
				qtdeTotalIFood++;
			}

			qtdeEntregaDia++;

			valorTotalEntregas = ent.getValor().add(valorTotalEntregas);

			if (i == entregas.size() || !dataComparacao.equals(entregas.get(i).getData())) {
				Acerto acerto = new Acerto();

				acerto.setData(ent.getData());
				acerto.setQtdeEntregasDia(qtdeEntregaDia);
				acerto.setQtdeIFood(qtdeIfoodDia);

				acertos.add(acerto);

				valorDiariaAcerto(acerto);
				valorTotalDiarias = valorTotalDiarias.add(acerto.getValorDiaria());

				dataComparacao = null;
				qtdeIfoodDia = 0;
				qtdeEntregaDia = 0;
			}
		}
	}


	public void buscarEntregadoresAcerto() {
		FacesMessage msg;

		entregadores = entregadorService.listarEntregadoresPorDataInicioFim(Util.converteLocalDate(dataInicio),
			Util.converteLocalDate(dataFim));

		if (entregadores.isEmpty()) {
			msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum entregador encontrado", "Não foi possível encontrar entregadores no período especificado");
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, entregadores.size() + " entregadores encontrados", "Selecione um entregador para prosseguir");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	public void buscarColaboradores() {
		if(tipoColaboradorSelecionado == TipoColaborador.FIXO) {
			colaboradores = colaboradorService.listarPorTipoOrderByNome(TipoColaborador.FIXO);
		}
	}


	public void cancelar() {
		init();
	}


	public void copyValeToClipboard() {
		/*
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(texto.toString());
		clipboard.setContents(selection, null);
		*/

		String texto = textoVale.toString();

		texto = texto.replace("\n", "\\n").replace("\r", "\\r").replace("'", "\\'");

		String script = "navigator.clipboard.writeText('" + texto + "');";

		PrimeFaces.current().executeScript(script);

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Recibo copiado", "Recibo copiado para a área de transferência.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	public List<Colaborador> completeColaborador(String query) {
        String queryLowerCase = query.toLowerCase();
        return colaboradores.stream().filter(t -> t.getNome().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }


	public void salvarDiaria() {

	}


	public void cancelarDiaria() {

	}


	public String diaFormatado(DayOfWeek dia) {
		return Util.diaDaSemana(dia);
	}


	public void valorDiariaAcerto(Acerto acerto) {
		acerto.setValorDiaria(new BigDecimal("0.00"));

		for(Taxa taxa : taxas) {
			if (valorDiaria.get(acerto.getData().getDayOfWeek())) {

				if (acerto.getQtdeEntregasDia() > taxa.getQtdEntregasPersonalizado()) {
					acerto.setValorDiaria(new BigDecimal(taxa.getValorPersonalizado()));
					break;
				}

			} else {

				if (acerto.getQtdeEntregasDia() > qtdEntregasFixoTaxa) {
					acerto.setValorDiaria(new BigDecimal(valorFixoTaxa));
					break;
				}

			}
		}
	}


	public void setValorDiaria(Map<DayOfWeek, Boolean> valorDiaria) {
        this.valorDiaria = valorDiaria;
    }


    public Boolean getValorDiaria(DayOfWeek dia) {
        return valorDiaria.get(dia);
    }


	public void salvarTaxa() {
		if (taxaSelecionada.getId() == null) {
            taxas.add(taxaSelecionada);

			taxaService.cadastrarTaxa(taxaSelecionada);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Taxa Cadastrada", "Valor: R$ " + taxaSelecionada.getValorPersonalizado().toString().replace(".", ","));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
			taxaService.atualizarTaxa(taxaSelecionada);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Taxa Atualizada", "Valor: R$ " + taxaSelecionada.getValorPersonalizado().toString().replace(".", ","));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

		PrimeFaces.current().executeScript("PF('taxaDialog').hide();");

		PrimeFaces.current().ajax().update("@root:@id(dt-taxas)");

        taxas = taxaService.getTaxas();
		taxaSelecionada = null;
	}


	public void deletarTaxaSelecionada() {
		taxas.remove(taxaSelecionada);

        taxaService.deletarTaxa(taxaSelecionada);

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Taxa Removida", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        PrimeFaces.current().ajax().update("@root:@id(dt-taxas)");

        taxaSelecionada = null;
	}


	public void adicionarTaxa() {
		taxaSelecionada = new Taxa();
	}


	public void cancelarTaxa() {
		taxaSelecionada = null;
	}
}


	/* if(valorDiaria.get(acerto.getData().getDayOfWeek())) {

		if (acerto.getQtdeEntregasDia() > 40) {
			acerto.setValorDiaria(new BigDecimal("50.00"));
		} else if(acerto.getQtdeEntregasDia() > 30) {
			acerto.setValorDiaria(new BigDecimal("45.00"));
		} else if(acerto.getQtdeEntregasDia() > 20) {
			acerto.setValorDiaria(new BigDecimal("40.00"));
		} else if(acerto.getQtdeEntregasDia() > 9) {
			acerto.setValorDiaria(new BigDecimal("35.00"));
		} else {
			acerto.setValorDiaria(new BigDecimal("0.00"));
		}

	} else {

		if (acerto.getQtdeEntregasDia() > 9) {
			acerto.setValorDiaria(new BigDecimal("35.00"));
		} else {
			acerto.setValorDiaria(new BigDecimal("0.00"));
		}

	} */


	/* @PreDestroy
	public void destroy() {
		if(was != null) {
			was.cleanup();
			was = null;
		}
	} */


	/* public void sendValeToWhatsapp() {
		was = new WhatsAppSender();

		if(was.setup()) {
			was.sendText(entregadorSelecionado.getTelefoneWhatsApp(), textoVale.toString());
		}
	} */
