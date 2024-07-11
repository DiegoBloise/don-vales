package br.com.don.enums;

public enum TipoUsuario {
    ADMIN("Administrador"),
    GERENTE("Gerente"),
    FUNCIONARIO("Funcionário");

    private String descricao;


    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }


    public String getDescricao() {
        return this.descricao;
    }
}