/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.converters;

import br.ufscar.dc.o3ctool.model.CCC;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Paulo Júnior
 */
@FacesConverter(value = "cccConverter", forClass = CCC.class)
public class CCCConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null) {
            return uiComponent.getAttributes().get(value);            
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null && value instanceof CCC) {
            CCC ccc = (CCC) value;
            uiComponent.getAttributes().put(ccc.getId().toString(), ccc);
            return ccc.getName();
        }
        return "";
    }
}
