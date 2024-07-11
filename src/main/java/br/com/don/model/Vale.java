package br.com.don.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import br.com.don.enums.TipoVale;
import br.com.don.session.UserSession;
import br.com.don.util.Util;
import jakarta.inject.Inject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vales")
public class Vale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private BigDecimal valor;

	private LocalDate data;

	private TipoVale tipo;

	@ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;

	@Transient
	@Inject
	private UserSession userSession;


	public Vale() {
		this.valor = new BigDecimal(0);
		this.tipo = TipoVale.DINHEIRO;
		this.setData(LocalDate.now());
	}


	public void setData(LocalDate data) {
		if (this.userSession != null && this.userSession.getUsuario().isAdmin()) {
			this.data = data;
		} else {
			LocalTime horaAtual = LocalTime.now();

			if (horaAtual.isBefore(LocalTime.of(1, 20))) {
				this.data = data.minusDays(1);
			} else {
				this.data = data;
			}
		}
	}


	@Transient
	public String getDataFormatada() {
		return Util.localDateFormatado(this.data);
	}


	@Transient
	public String getDataSemanaFormatada() {
		return Util.dataSemanaFormatada(this.data);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Vale other = (Vale) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Vale [id=" + id + ", valor=" + valor + ", data=" + data + ", tipo=" + tipo + "]";
	}
}
