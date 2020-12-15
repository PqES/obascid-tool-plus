package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.ResearcherDAO;
import br.ufscar.dc.o3ctool.dao.jpa.ResearcherJPA;
import br.ufscar.dc.o3ctool.model.Researcher;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "resbean")
@SessionScoped
public class ResearcherBean implements Serializable {

    private Researcher res = new Researcher();
    private Researcher resSession = null;
    private final ResearcherDAO resDAO = new ResearcherJPA();
    private String confirmPassword;

    public String logoff() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.invalidate();
        resSession = null;
        res = new Researcher();
        return "index.xhtml?faces-redirect=true";
    }

    public boolean isLogged() {
        return (resSession != null);
    }

    public String saveResearcher() {
        if (res.getId() == null) {
            if (resDAO.alreadyExistingResearcher(res)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_researcher"),
                        null);
                return null;
            } else if (!res.getPassword().equals(confirmPassword)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_confirm_password"),
                        null);
                return null;
            }
        }
        resDAO.save(res);
        res = new Researcher();
        return null;
    }

    public void updateProfile() {
        if (!resDAO.alreadyExistingResearcher(resSession)) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_login"),
                    null);
        } 
        resDAO.save(resSession);
    }

    public String login() {
        String email = res.getEmail();
        String password = res.getPassword();
        resSession = resDAO.getByEmail(email);
        res = new Researcher();
        if (resSession == null || !resSession.getPassword().equals(password)) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_login"),
                    null);
            resSession = null;
            return null;
        }
        return "index.xhtml?faces-redirect=true";
    }

    public Researcher getRes() {
        return res;
    }

    public void setRes(Researcher res) {
        this.res = res;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Researcher getResSession() {
        return resSession;
    }

    public void setResSession(Researcher resSession) {
        this.resSession = resSession;
    }

}
