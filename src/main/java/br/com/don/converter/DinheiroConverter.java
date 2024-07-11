package br.com.don.converter;

import java.math.BigDecimal;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("dinheiroConverter")
public class DinheiroConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty() || value.equals("R$ 0,00")) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(value.replace("R$ ", "").replace(",", "."));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BigDecimal value) {
        if (value == null) {
            return "";
        }

        return "R$ " + value.toString();
    }
}
