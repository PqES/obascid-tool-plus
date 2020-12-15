package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.MainConcernDAO;
import br.ufscar.dc.o3ctool.dao.jpa.MainConcernJPA;
import br.ufscar.dc.o3ctool.model.IdentificationUnit;
import br.ufscar.dc.o3ctool.model.MainConcern;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "mcbean")
@ViewScoped
public class MainConcernBean implements Serializable {

    private MainConcern mc;
    private final MainConcernDAO mcDAO;

    public MainConcernBean() {
        mc = Session.getActivSettings().getMainConcern();
        mcDAO = new MainConcernJPA();
    }

    public String saveMainConcern() {
        IdentificationUnit activeIU = Session.getActiveIU();
        if (mc.getId() == null) {
            if (mcDAO.alreadyExistingMainConcern(activeIU.getId(),
                    mc.getRequirement().getId(),
                    mc.getMainConcern().getId())) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_main_concern"),
                        null);
                return null;
            }
            if (mcDAO.getByRequirement(activeIU.getId(),
                    mc.getRequirement()) != null) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_more_than_one_main_concern"),
                        null);
                return null;
            }
        }

        mc.setIu(activeIU);
        mcDAO.save(mc);
        mc = Session.getActivSettings().getMainConcern();
        return "mainConcern.xhtml?faces-redirect=true";
    }

    public String removeMainConcern() {
        mcDAO.remove(mc);
        mc = Session.getActivSettings().getMainConcern();
        return "mainConcern.xhtml?faces-redirect=true";
    }

    public List<MainConcern> getAllMainConcerns() {
        return mcDAO.getAllMainConcerns(Session.getActiveIU().getId());
    }

    public MainConcern getMc() {
        return mc;
    }

    public void setMc(MainConcern mc) {
        this.mc = mc;
    }
}
