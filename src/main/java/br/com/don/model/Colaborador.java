package br.com.don.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.don.enums.TipoColaborador;
import br.com.don.util.Util;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "colaboradores")
public class Colaborador implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

	protected String nome;

	protected String telefone;

	@Column(name = "data_nascimento")
	protected LocalDate dataNascimento;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pix_id")
    protected Pix pix;

	@OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected List<Vale> vales = new ArrayList<>();

    @Column(name = "tipo_colaborador")
    protected TipoColaborador tipo;

	@Transient
	private BigDecimal valorTotalSemDesconto;

	@Transient
	private BigDecimal valorTotalComDesconto;

	@Transient
	private BigDecimal valorTotalVales;

	@Transient
	private BigDecimal valorSaldo;

	@Transient
	private Integer qtdVales;


	public Colaborador() {
		this.pix = new Pix(this);
	}


	@Transient
	public String getDataNascimentoFormatada() {
		return Util.localDateFormatado(this.dataNascimento);
	}


	public String gerarPayloadPix() {
		return this.pix.getPayload(this.nome, this.valorTotalComDesconto.toString(), "Sao Paulo", "PizzaDon");
	}


	public String getTelefone() {
        if (this.telefone == null) {
            return this.telefone;
        } else {
            return MessageFormat.format("({0}) {1}-{2}",
                this.telefone.substring(0, 2),
                this.telefone.substring(2, 7),
                this.telefone.substring(7));
        }
    }


	public String getTelefoneWhatsApp() {
        if (this.telefone == null) {
            return this.telefone;
        } else {
            return "+55" + this.telefone;
        }
    }


	public void setTelefone(String telefone) {
        this.telefone = telefone.replaceAll("[^0-9]", "");
    }


	public void adicionarVale(Vale vale) {
        this.vales.add(vale);
        vale.setColaborador(this);
    }


    public void adicionarVale() {
        Vale vale = new Vale();
        this.adicionarVale(vale);
    }


    public void removerVale(Vale vale) {
        this.vales.remove(vale);
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((pix == null) ? 0 : pix.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Colaborador other = (Colaborador) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (pix == null) {
			if (other.pix != null)
				return false;
		} else if (!pix.equals(other.pix))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Colaborador [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", dataNascimento="
				+ dataNascimento + ", pix=" + pix + "]";
	}
}