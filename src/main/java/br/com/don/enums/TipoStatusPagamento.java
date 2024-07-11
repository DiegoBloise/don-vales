package br.com.don.enums;

public enum TipoStatusPagamento {
	PAGO("Pago"),
	PENDENTE("Pendente");

	private String descricao;


    TipoStatusPagamento(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}
