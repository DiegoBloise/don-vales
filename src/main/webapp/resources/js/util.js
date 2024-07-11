$(document).ready(function () {
    // Configuração do widget para InputNumber
    $('.moneyInput').on('input', function (event) {
        formatMoneyInput(event.target);
    });
});



function formatMoneyInput(input) {
    var value = input.value.replace(/[^\d]/g, '');

    // Remove zeros à esquerda
    value = value.replace(/^0+/, '');

    // Adiciona zeros à esquerda, se necessário
    while (value.length < 3) {
        value = '0' + value;
    }

    // Divide o valor em parte inteira e decimal
    var integerPart = value.slice(0, -2) || '0';
    var decimalPart = value.slice(-2);

    // Formata o valor com vírgula e adiciona o símbolo de moeda
    var formattedValue = 'R$ ' + integerPart + ',' + decimalPart;

    // Atualiza o valor no campo
    input.value = formattedValue;
}