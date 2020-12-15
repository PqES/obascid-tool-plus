package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.KeywordDAO;
import br.ufscar.dc.o3ctool.dao.jpa.KeywordJPA;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.Keyword;
import br.ufscar.dc.o3ctool.model.Researcher;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "kwbean")
@ViewScoped
public class KeywordBean implements Serializable {

    private Keyword kw = new Keyword();
    private final KeywordDAO kwDAO = new KeywordJPA();

    private String isAStopword(Keyword kw) {
        Researcher activeResearcher = Session.getActivResearcher();
        String sws = activeResearcher.getStopwords().getStopwords();
        StringTokenizer stkSWs = new StringTokenizer(sws, ",");
        while (stkSWs.hasMoreTokens()) {
            String activeSW = stkSWs.nextToken().trim();
            if (activeSW.isEmpty()) {
                break;
            }
            if (activeSW.equalsIgnoreCase(kw.getName())) {
                return activeSW;
            }
        }
        return null;
    }

    public String saveKeyword() {
        CCC activeCCC = Session.getActiveCCC();
        if (kw.getId() == null) {
            if (kwDAO.alreadyExistingKeyword(activeCCC.getId(), kw)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_kw"),
                        null);
                return null;
            } else {
                String sw = isAStopword(kw);
                if (sw != null) {
                    Messages.addMessage(
                            FacesMessage.SEVERITY_ERROR,
                            i18n.getProperty("err_stopword"),
                            null);
                    return null;
                }
            }
        }
        kw.setCcc(activeCCC);
        kwDAO.save(kw);
        kw = new Keyword();
        return "kw.xhtml?faces-redirect=true";
    }

    public String removeKeyword() {
        kwDAO.remove(kw);
        kw = new Keyword();
        return "kw.xhtml?faces-redirect=true";
    }

    public List<Keyword> getAllKeywords() {
        return kwDAO.getAllKeywords(Session.getActiveCCC().getId());
    }

    public List<Keyword> getAllKeywordsByCCC(CCC ccc) {
        return kwDAO.getAllKeywords(ccc.getId());
    }

    public Keyword getKw() {
        return kw;
    }

    public void setKw(Keyword kw) {
        this.kw = kw;
    }
}
