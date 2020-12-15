/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.util;

import br.ufscar.dc.o3ctool.bean.CCCBean;
import br.ufscar.dc.o3ctool.bean.IdentificationUnitBean;
import br.ufscar.dc.o3ctool.bean.OntologyBean;
import br.ufscar.dc.o3ctool.bean.ProjectBean;
import br.ufscar.dc.o3ctool.bean.ResearcherBean;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.IdentificationUnit;
import br.ufscar.dc.o3ctool.model.Ontology;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Researcher;
import br.ufscar.dc.o3ctool.model.Settings;
import javax.faces.context.FacesContext;

/**
 *
 * @author Paulo
 */
public class Session {

    public static Researcher getActivResearcher() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResearcherBean resbean = (ResearcherBean) context.getELContext().getELResolver().getValue(context.getELContext(), null, "resbean");
        if (resbean != null) {
            return resbean.getResSession();
        }
        return null;
    }

    public static Settings getActivSettings() {
        Researcher res = getActivResearcher();
        if (res != null)
            return res.getSettings();
        return null;
    }

    public static Ontology getActiveOntology() {
        FacesContext context = FacesContext.getCurrentInstance();
        OntologyBean ontobean = (OntologyBean) context.getELContext().getELResolver().getValue(context.getELContext(), null, "ontobean");
        if (ontobean != null) {
            return ontobean.getOnto();
        }
        return null;
    }

    public static Project getActiveProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        ProjectBean projbean = (ProjectBean) context.getELContext().getELResolver().getValue(context.getELContext(), null, "projbean");
        if (projbean != null) {
            return projbean.getProj();
        }
        return null;
    }

    public static IdentificationUnit getActiveIU() {
        FacesContext context = FacesContext.getCurrentInstance();
        IdentificationUnitBean iubean = (IdentificationUnitBean) context.getELContext().getELResolver().getValue(context.getELContext(), null, "iubean");
        if (iubean != null) {
            return iubean.getIu();
        }
        return null;
    }
    
    public static CCC getActiveCCC() {
        FacesContext context = FacesContext.getCurrentInstance();
        CCCBean cccbean = (CCCBean) context.getELContext().getELResolver().getValue(context.getELContext(), null, "cccbean");
        if (cccbean != null) {
            return cccbean.getCcc();
        }
        return null;
    }
}
