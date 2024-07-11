package br.com.don.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.don.util.Util;
import lombok.Data;

@Data
public class Acerto implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDate data;

	private Integer qtdeEntregasDia;

	private Integer qtdeIFood;

	private BigDecimal valorValeDia;

	private BigDecimal valorDiaria;

	private final String QUEBRALINHA ="\n";

	private final String TRACEJADO = "--------------------------------";


	public String getDataFormatada() {
		return Util.dataSemanaFormatada(data);
	}


	public String toString() {
		StringBuffer buffer = new StringBuffer()
		.append("*").append(Util.diaDaSemana(data.getDayOfWeek()))
		.append(" ")
		.append(Util.localDateFormatado(this.data)).append("*")
		.append(QUEBRALINHA)
		.append("Qtde de entregas: ").append(this.qtdeEntregasDia)
		.append(QUEBRALINHA)
		.append("Valor da Diária: R$ ").append(getValorDiaria())
		.append(QUEBRALINHA)
		.append("Qtde de IFood: ").append(this.qtdeIFood)
		.append(QUEBRALINHA)
		.append("Vale: R$ ").append(this.valorValeDia)
		.append(QUEBRALINHA)
		.append(TRACEJADO)
		.append(QUEBRALINHA);

		return buffer.toString();
	}
}
