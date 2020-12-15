/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 *
 * @author Paulo
 */
public class Messages {

    public static void addMessage(Severity sev, String msg, String compoenent) {
        FacesMessage fm = new FacesMessage(sev, msg, msg);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(compoenent, fm);
    }
}
