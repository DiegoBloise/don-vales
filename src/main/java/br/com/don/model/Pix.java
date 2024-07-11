package br.com.don.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.don.enums.TipoChavePix;
import br.com.don.util.Jix;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chaves_pix")
public class Pix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private TipoChavePix tipo;

    private String chave;

	@OneToMany(mappedBy = "pix", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Colaborador> colaboradores = new ArrayList<>();


	public Pix() {

	}


	public Pix(Colaborador colaborador) {
		this.adicionarColaborador(colaborador);
	}


    public String getPayload(String nome, String valor, String cidade, String txtId) {
		return new Jix(nome, this.chave, valor, cidade, txtId).gerarPayload();
	}


	public String getChaveDebug() {
		return this.chave;
	}


	public String getChave() {
		if(this.chave == null || this.chave.isEmpty()) {
			return this.chave;
		}

		switch (this.tipo) {
			case CELULAR:
				return MessageFormat.format("({0}) {1}-{2}",
					this.chave.substring(3, 5),
					this.chave.substring(5, 10),
					this.chave.substring(10));
			case CPF:
				return MessageFormat.format("{0}.{1}.{2}-{3}",
					this.chave.substring(0, 3),
					this.chave.substring(3, 6),
					this.chave.substring(6, 9),
					this.chave.substring(9));
			case CNPJ:
				return MessageFormat.format("{0}.{1}.{2}/{3}-{4}",
					this.chave.substring(0, 2),
					this.chave.substring(2, 5),
					this.chave.substring(5, 8),
					this.chave.substring(8, 12),
					this.chave.substring(12));
			default:
				return this.chave;
		}
	}


	public void setChave(String chavePix) {
		switch (this.tipo) {
			case CELULAR:
				this.chave = "+55" + chavePix.replaceAll("[^0-9]", "");
				break;
			case CPF:
			case CNPJ:
				this.chave = chavePix.replaceAll("[^0-9]", "");
				break;
			default:
				this.chave = chavePix;
				break;
		}
	}


	public void adicionarColaborador(Colaborador colaborador) {
        this.colaboradores.add(colaborador);
        colaborador.setPix(this);
    }


    public void adicionarColaborador() {
        Colaborador colaborador = new Colaborador();
        this.adicionarColaborador(colaborador);
    }


    public void removerColaborador(Colaborador colaborador) {
        this.colaboradores.remove(colaborador);
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
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
		Pix other = (Pix) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (tipo != other.tipo)
			return false;
		if (chave == null) {
			if (other.chave != null)
				return false;
		} else if (!chave.equals(other.chave))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Pix [id=" + id + ", tipo=" + tipo + ", chave=" + chave + "]";
	}
}
