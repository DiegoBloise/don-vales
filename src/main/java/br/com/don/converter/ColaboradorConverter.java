package br.com.don.converter;

import java.util.Map;

import br.com.don.model.Colaborador;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("colaboradorConverter")
public class ColaboradorConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return this.getAttributesFrom(component).get(value);
            } catch (Exception e) {
                throw new ConverterException("Erro ao converter colaborador: ", e);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            Colaborador colaborador = (Colaborador) value;
            this.addAttribute(component, colaborador);
            return String.valueOf(colaborador.getId());
        }
        return null;
    }

    private Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }

    private void addAttribute(UIComponent component, Colaborador colaborador) {
        this.getAttributesFrom(component).put(String.valueOf(colaborador.getId()), colaborador);
    }
}
