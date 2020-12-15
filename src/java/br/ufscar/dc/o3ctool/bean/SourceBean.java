package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.SourceDAO;
import br.ufscar.dc.o3ctool.dao.jpa.SourceJPA;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.Source;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "sbean")
@ViewScoped
public class SourceBean implements Serializable {

    private Source source;
    private final SourceDAO sDAO;

    public SourceBean() {
        source = Session.getActivSettings().getSource();
        sDAO = new SourceJPA();
    }
    
    public String saveSource() {
        CCC activeCCC = Session.getActiveCCC();
        if (source.getId() == null) {
            if (sDAO.alreadyExistingSource(activeCCC.getId(), source)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_source"),
                        null);
                return null;
            } 
        }
        source.setCcc(activeCCC);
        sDAO.save(source);
        source = Session.getActivSettings().getSource();
        return "source.xhtml?faces-redirect=true";
    }

    public String removeSource() {
        sDAO.remove(source);
        source = Session.getActivSettings().getSource();
        return "source.xhtml?faces-redirect=true";
    }

    public List<Source> getAllSources() {
        return sDAO.getAllSources(Session.getActiveCCC().getId());
    }

    public List<Source> getAllSourcesByCCC(CCC ccc) {
        return sDAO.getAllSources(ccc.getId());
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
    
    
}
