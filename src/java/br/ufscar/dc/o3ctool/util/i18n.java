/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.util;

import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 *
 * @author Paulo
 */
public class i18n {

    public static String getProperty(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ResourceBundle bundle = app.getResourceBundle(context, "msgs");
        return bundle.getString(key);
    }
}
