package br.com.don.util;

/* 
CREATED BY: Diego Bloise o mestre dos magos
*/

public class Jix {

    private String nome;

    private String chavepix;

    private String valor;

    private String cidade;

    private String txtId;


    public Jix(String nome, String chavepix, String valor, String cidade, String txtId) {
        this.nome = nome;
        this.chavepix = chavepix;
        this.valor = valor.replace(",", ".");
        this.cidade = cidade;
        this.txtId = txtId;
    }


    public String gerarPayload() {
        String payloadFormat       = "000201";

        String merchantAccountData = "0014BR.GOV.BCB.PIX01" + this.chavepix.length() + this.chavepix;
        String merchantAccount     = "26" + merchantAccountData.length() + merchantAccountData;

        String merchantCategCode   = "52040000";
        String transactionCurrency = "5303986";
        String transactionAmount   = "54" + String.format("%02d", this.valor.length()) + this.valor;
        String countryCode         = "5802BR";
        String merchantName        = "59" + String.format("%02d", this.nome.length()) + this.nome;
        String merchantCity        = "60" + String.format("%02d", this.cidade.length()) + this.cidade;

        String addDataFieldData    = "05" + String.format("%02d", this.txtId.length()) + this.txtId;
        String addDataField        = "62" + addDataFieldData.length() + addDataFieldData;

        String crc16               = "6304";

        String payload = payloadFormat +
                         merchantAccount +
                         merchantCategCode +
                         transactionCurrency +
                         transactionAmount +
                         countryCode +
                         merchantName +
                         merchantCity +
                         addDataField +
                         crc16;

        String crc16Code = Integer.toHexString(calculateCRC16(payload.getBytes())).toUpperCase();

        crc16Code = String.format("%4s", crc16Code).replace(' ', '0');

        String payloadCompleta = payload + crc16Code;

        return payloadCompleta;
    }


    private static int calculateCRC16(byte[] bytes) {
        int crc = 0xFFFF;
        int polynomial = 0x1021;

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }

        return crc & 0xFFFF;
    }


    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getChavepix() {
        return chavepix;
    }


    public void setChavepix(String chavepix) {
        this.chavepix = chavepix;
    }


    public String getValor() {
        return valor;
    }


    public void setValor(String valor) {
        this.valor = valor;
    }


    public String getCidade() {
        return cidade;
    }


    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    public String getTxtId() {
        return txtId;
    }


    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }


    @Override
    public String toString() {
        return "Jix [nome=" + nome + ", chavepix=" + chavepix + ", valor=" + valor + ", cidade=" + cidade + ", txtId="
                + txtId + "]";
    }
}
