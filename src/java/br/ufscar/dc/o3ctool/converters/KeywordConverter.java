/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.converters;

import br.ufscar.dc.o3ctool.dao.jpa.KeywordDAO;
import br.ufscar.dc.o3ctool.dao.jpa.KeywordJPA;
import br.ufscar.dc.o3ctool.model.Keyword;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Paulo Júnior
 */
@FacesConverter(value = "kwConverter", forClass = Keyword.class)
public class KeywordConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null) {
            return uiComponent.getAttributes().get(value);            
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value != null && value instanceof Keyword) {
            Keyword kw = (Keyword) value;
            uiComponent.getAttributes().put(kw.getName(), kw);
            return kw.getName();
        }
        return "";
    }
}
