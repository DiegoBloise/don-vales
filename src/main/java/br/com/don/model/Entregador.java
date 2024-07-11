package br.com.don.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.don.enums.TipoColaborador;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Entregador extends Colaborador {

    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Entrega> entregas = new ArrayList<>();

	@Transient
	private Integer qtdTotalDias;

	@Transient
	private Integer qtdEntregas;

	@Transient
	private BigDecimal valorTotalEntregas;

	@Transient
	private BigDecimal valorTotalIfood;

	@Transient
	private BigDecimal valorTotalDiarias;


	public Entregador() {
		this.tipo = TipoColaborador.ENTREGADOR;
	}


    public Entregador(Colaborador colaborador) {
        this.nome = colaborador.getNome();
		this.telefone = colaborador.getTelefone();
		this.dataNascimento = colaborador.getDataNascimento();
		this.tipo = colaborador.getTipo();
		if(colaborador.getPix().getTipo() != null) {
			this.pix = new Pix(this);
			this.pix.setTipo(colaborador.getPix().getTipo());
			this.pix.setChave(colaborador.getPix().getChave());
		}
    }


    public void calcularAsParadas(List<Entrega> entregas) {

	}


    public void adicionarEntrega(Entrega entrega) {
        this.entregas.add(entrega);
        entrega.setEntregador(this);
    }


    public void adicionarEntrega() {
        Entrega entrega = new Entrega();
        this.adicionarEntrega(entrega);
    }


    public void removerEntrega(Entrega entrega) {
        this.entregas.remove(entrega);
    }
}