package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.CCCDAO;
import br.ufscar.dc.o3ctool.dao.jpa.CCCJPA;
import br.ufscar.dc.o3ctool.dao.jpa.DependencyDAO;
import br.ufscar.dc.o3ctool.dao.jpa.DependencyJPA;
import br.ufscar.dc.o3ctool.dao.jpa.IdentificationUnitDAO;
import br.ufscar.dc.o3ctool.dao.jpa.IdentificationUnitJPA;
import br.ufscar.dc.o3ctool.dao.jpa.KeywordDAO;
import br.ufscar.dc.o3ctool.dao.jpa.KeywordJPA;
import br.ufscar.dc.o3ctool.dao.jpa.MainConcernDAO;
import br.ufscar.dc.o3ctool.dao.jpa.MainConcernJPA;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipJPA;
import br.ufscar.dc.o3ctool.dao.jpa.RequirementDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RequirementJPA;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.CCCIndication;
import br.ufscar.dc.o3ctool.model.Dependency;
import br.ufscar.dc.o3ctool.model.IdentificationUnit;
import br.ufscar.dc.o3ctool.model.Keyword;
import br.ufscar.dc.o3ctool.model.MainConcern;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Relationship;
import br.ufscar.dc.o3ctool.model.Requirement;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

@ManagedBean(name = "iubean")
@SessionScoped
public class IdentificationUnitBean implements Serializable {

    private IdentificationUnit iu;
    private final IdentificationUnitDAO iuDAO;
    private final RelationshipDAO relatDAO;
    private final CCCDAO cccDAO;
    private final RequirementDAO reqDAO;
    private final MainConcernDAO mcDAO;
    private final DependencyDAO depDAO;
    private final KeywordDAO kwDAO;
    private Map<CCC, List<Keyword>> selectedCCC;
    private final Map<Long, List<CCC>> crosscuttingConcerns;
    private final Map<CCC, Boolean> nonFunctionalConcerns;
    private final List<List<Boolean>> crosscuttingMatrix;
    private final List<List<String>> contributionMatrix;
    private Long mainConcernId;
    private Long cccId;
    private List<Requirement> selectedRequirements;
    private final Set<CCC> identifiedCCC;
    private List<CCCIndication> indications;
    private List<CCCIndication> filteredIndications;
    private int numOfIdentifiedCCC;
    private int numOfAffectedRequirements;
    private List<String> errorMessages;
    private List<String> warningMessages;

    // Lucene objects
    private final String SEARCH_FIELD_NAME = "requirementDesc";

    public IdentificationUnitBean() {
        iu = Session.getActivSettings().getIdentificationUnit();
        iuDAO = new IdentificationUnitJPA();
        relatDAO = new RelationshipJPA();
        cccDAO = new CCCJPA();
        reqDAO = new RequirementJPA();
        kwDAO = new KeywordJPA();
        mcDAO = new MainConcernJPA();
        depDAO = new DependencyJPA();
        selectedCCC = new HashMap<CCC, List<Keyword>>();
        crosscuttingConcerns = new HashMap<Long, List<CCC>>();
        nonFunctionalConcerns = new HashMap<CCC, Boolean>();
        crosscuttingMatrix = new ArrayList<List<Boolean>>();
        contributionMatrix = new ArrayList<List<String>>();
        selectedRequirements = new ArrayList<Requirement>();
        identifiedCCC = new HashSet<CCC>();
        indications = new ArrayList<CCCIndication>();
        filteredIndications = new ArrayList<CCCIndication>();
        warningMessages = new ArrayList<String>();
        errorMessages = new ArrayList<String>();
        numOfIdentifiedCCC = 0;
        numOfAffectedRequirements = 0;
        mainConcernId = null;
        cccId = null;
    }

    public String buildCrosscuttingMatrix() {
        List<CCC> allCCC = getSelectedCCC();
        crosscuttingMatrix.clear();
        int amountOfCCC = allCCC.size();
        for (int i = 0; i < amountOfCCC; i++) {
            crosscuttingMatrix.add(new ArrayList<Boolean>());
            CCC mainConcern = allCCC.get(i);
            if (!crosscuttingConcerns.containsKey(mainConcern.getId())) {
                for (int j = 0; j < amountOfCCC; j++) {
                    crosscuttingMatrix.get(i).add(false);
                }
            } else {
                for (int j = 0; j < amountOfCCC; j++) {
                    if (i == j) {
                        crosscuttingMatrix.get(i).add(false);
                        continue;
                    }
                    CCC affectedConcern = allCCC.get(j);
                    if (crosscuttingConcerns.get(mainConcern.getId()).contains(affectedConcern)) {
                        crosscuttingMatrix.get(i).add(true);
                    } else {
                        crosscuttingMatrix.get(i).add(false);
                    }
                }
            }
        }
        return "crosscuttingMatrix.xhtml?faces-redirect=true";
    }

    public String buildContributionMatrix() {
        List<CCC> allCCC = getSelectedCCC();
        contributionMatrix.clear();
        int amountOfCCC = allCCC.size();
        for (int i = 0; i < amountOfCCC; i++) {
            contributionMatrix.add(new ArrayList<String>());
            CCC mainConcern = allCCC.get(i);
            if (!crosscuttingConcerns.containsKey(mainConcern.getId())) {
                for (int j = 0; j < amountOfCCC; j++) {
                    contributionMatrix.get(i).add("");
                }
            } else {
                for (int j = 0; j < amountOfCCC; j++) {
                    if (i == j) {
                        contributionMatrix.get(i).add("");
                        continue;
                    }
                    CCC affectedConcern = allCCC.get(j);
                    if (crosscuttingConcerns.get(mainConcern.getId()).contains(affectedConcern)) {
                        List<Relationship> relats = relatDAO.getAllRelationshipsByTarget(iu.getOntology().getId(), affectedConcern.getId());
                        boolean added = false;
                        StringBuilder strContributions = new StringBuilder();
                        for (Relationship r : relats) {
                            if (crosscuttingConcerns.get(mainConcern.getId()).contains(r.getSource())) {
                                if (r.isPositiveContribution()) {
                                    strContributions.append(getSelectedCCC().indexOf(r.getSource()) + 1).append(" (+)<br/>");
                                } else if (r.isNegativeContribution()) {
                                    if (r.getSource().comparePriority(affectedConcern.getPriority()) == CCC.PriorityComparision.NOT_SPECIFIED) {
                                        strContributions.append(getSelectedCCC().indexOf(r.getSource()) + 1).append(" (-)<br/>");
                                    } else if (r.getSource().comparePriority(affectedConcern.getPriority()) == CCC.PriorityComparision.LOWER) {
                                        strContributions.append(getSelectedCCC().indexOf(r.getSource()) + 1).append(" (<)<br/>");
                                    } else if (r.getSource().comparePriority(affectedConcern.getPriority()) == CCC.PriorityComparision.HIGHER) {
                                        strContributions.append(getSelectedCCC().indexOf(r.getSource()) + 1).append(" (>)<br/>");
                                    } else {
                                        strContributions.append(r.getSource().getName()).append(" (=)<br/>");
                                    }
                                }
                                added = true;
                            }
                        }
                        if (!added) {
                            contributionMatrix.get(i).add("");
                        } else {
                            contributionMatrix.get(i).add(strContributions.toString());
                        }
                    } else {
                        contributionMatrix.get(i).add("");
                    }
                }
            }
        }
        return "contributionMatrix.xhtml?faces-redirect=true";
    }

    public int getNumOfSelectedCCC() {
        return selectedCCC.entrySet().size();
    }

    public int getNumOfRequirements() {
        return selectedRequirements.size();
    }

//    private void checkConflicts() {
//        for (CCC ccc : identifiedCCC) {
//            List<Relationship> relats
//                    = relatDAO.getAllRelationshipsByTargetAndType(iu.getOntology().getId(), ccc.getId(), Relationship.RelationshipType.NEGATIVE_CONTRIBUTION);
//            StringBuilder strNegativeContributions = new StringBuilder();
//            boolean areThereConflicts = false;
//            for (Relationship relat : relats) {
//                strNegativeContributions.append(relat.getName()).append(", ");
//                if (!relatDAO.getAllRelationshipsByTargetAndType(iu.getOntology().getId(), ccc.getId(), Relationship.RelationshipType.POSITIVE_CONTRIBUTION).isEmpty()) {
//                    errorMessages.add(i18n.getProperty("pos_neg_contribution_code") + " - "
//                            + i18n.getProperty("err_pos_neg_contribution")
//                            + ": "
//                            + ccc.getName()
//                            + ".");
//                }
//                areThereConflicts = true;
//            }
//            if (areThereConflicts) {
//                strNegativeContributions.deleteCharAt(strNegativeContributions.lastIndexOf(", "));
//                errorMessages.add(i18n.getProperty("neg_contribution_code") + " - "
//                        + i18n.getProperty("err_neg_contribution")
//                        + ": "
//                        + strNegativeContributions.toString()
//                        + ".");
//            }
//        }
//    }
    private void checkPositiveContributions() {
        for (CCC ccc : identifiedCCC) {
            List<Relationship> relationships = relatDAO.getAllRelationshipsByTargetAndType(ccc.getOntology().getId(), ccc.getId(), Relationship.RelationshipType.POSITIVE_CONTRIBUTION);
            Iterator<Relationship> it = relationships.iterator();
            boolean nonIdentifiedCCCError = false;
            StringBuilder strNonIdentifiedCCCError = new StringBuilder();
            while (it.hasNext()) {
                Relationship relat = it.next();
                if (!identifiedCCC.contains(relat.getSource())) {
                    nonIdentifiedCCCError = true;
                    strNonIdentifiedCCCError.append(relat.getName()).append(", ");
                }
            }
            if (nonIdentifiedCCCError) {
                strNonIdentifiedCCCError.deleteCharAt(strNonIdentifiedCCCError.lastIndexOf(", "));
                warningMessages.add(i18n.getProperty("err_pos_contribution_nonidentified_ccc")
                        + ": "
                        + strNonIdentifiedCCCError.toString().trim()
                        + ".");
            }
        }
    }

//    private void checkRequirements() {
//        boolean nonIdentifiedCCCError = false;
//        StringBuilder strNonIdentifiedCCCError = new StringBuilder();
//        for (CCCIndication indication : indications) {
//            if (indication.getCccIndications().isEmpty()) {
//                nonIdentifiedCCCError = true;
//                strNonIdentifiedCCCError.append(indication.getReq().getName()).append(", ");
//            }
//        }
//        if (nonIdentifiedCCCError) {
//            strNonIdentifiedCCCError.deleteCharAt(strNonIdentifiedCCCError.lastIndexOf(", "));
//            errorMessages.add(i18n.getProperty("err_nonidentified_ccc")
//                    + ": "
//                    + strNonIdentifiedCCCError.toString().trim()
//                    + ".");
//        }
//    }
    private List<CCC> getIdentifiedCCC(Requirement req) {
        for (CCCIndication indication : indications) {
            if (indication.getReq().getId().equals(req.getId())) {
                return indication.getCccIndications();
            }
        }
        return null;
    }

    private List<CCC> mergeCCCList(List<CCC> target, List<CCC> source) {
        List<CCC> newList = new ArrayList<CCC>(target);
        for (CCC ccc : source) {
            if (!target.contains(ccc)) {
                newList.add(ccc);
            }
        }
        return newList;
    }

    private void checkRequirementDependencies() {
        for (CCCIndication indication : indications) {
            List<Dependency> dependencies = depDAO.getBySource(iu.getProject().getId(), indication.getReq().getId());
            if (dependencies.isEmpty()) {
                continue;
            }
            for (Dependency dep : dependencies) {
                List<CCC> relatedCCC = getIdentifiedCCC(dep.getTarget());
                if (!relatedCCC.isEmpty()) {
                    if (indication.getCccIndications().isEmpty()) {
                        numOfAffectedRequirements++;
                    }

                    indication.setCccIndications(mergeCCCList(indication.getCccIndications(), relatedCCC));
                    setMainConcern(indication.getCccIndications(), indication);
                }
            }
        }
    }

    private void checkNonCrosscuttingNonFunctionalConcerns() {
        for (CCCIndication indication : indications) {
            for (CCC ccc : indication.getCccIndications()) {
                if (ccc.isFunctional()) {
                    continue;
                }
                if ((nonFunctionalConcerns.containsKey(ccc)) && (nonFunctionalConcerns.get(ccc))) {
                    continue;
                }
                if (indication.getReq()
                        .isNonFunctional()) {
                    nonFunctionalConcerns.put(ccc, Boolean.FALSE);
                } else {
                    nonFunctionalConcerns.put(ccc, Boolean.TRUE);
                }
            }
        }

        boolean error = false;
        StringBuilder strError = new StringBuilder();
        for (Entry<CCC, Boolean> entry : nonFunctionalConcerns.entrySet()) {
            if (!entry.getValue()) {
                error = true;
                strError.append(entry.getKey().getName()).append(", ");
            }
        }
        if (error) {
            strError.deleteCharAt(strError.lastIndexOf(", "));
            warningMessages.add(i18n.getProperty("err_noncrosscutting_nonfunctionalconcern")
                    + ": "
                    + strError.toString().trim()
                    + ".");
        }
    }

    private void checkMainConcerns() {
        boolean error = false;
        StringBuilder strError = new StringBuilder();
        for (CCCIndication indication : indications) {
            if (indication.getMainConcern() == null) {
                error = true;
                strError.append(indication.getReq().getName()).append(", ");
            }
        }
        if (error) {
            strError.deleteCharAt(strError.lastIndexOf(", "));
            errorMessages.add(i18n.getProperty("err_nonmainconcern")
                    + ": "
                    + strError.toString().trim()
                    + ".");
        }
    }

    private void checkConcernDependencies() {
        for (CCC ccc : identifiedCCC) {
            List<Relationship> relationships = relatDAO.getAllRelationshipsBySourceAndType(ccc.getOntology().getId(), ccc.getId(), Relationship.RelationshipType.DEPENDENCY);
            boolean nonIdentifiedCCCError = false;
            StringBuilder strNonIdentifiedCCCError = new StringBuilder();
            for (Relationship relat : relationships) {
                if (!identifiedCCC.contains(relat.getTarget())) {
                    nonIdentifiedCCCError = true;
                    strNonIdentifiedCCCError.append(relat.getName()).append(", ");
                }
            }
            if (nonIdentifiedCCCError) {
                strNonIdentifiedCCCError.deleteCharAt(strNonIdentifiedCCCError.lastIndexOf(", "));
                errorMessages.add(i18n.getProperty("err_dependency_nonidentified_ccc")
                        + ": "
                        + strNonIdentifiedCCCError.toString()
                        + ".");
            }
        }
    }

    public Map<CCC, List<Keyword>> getSelectedCCCWithKeywords() {
        final Map<CCC, List<Keyword>> selectedCCCWithKeywords = new HashMap<CCC, List<Keyword>>();
        final List<CCC> cccList = getAllIdentificationUnitCCC();
        for (CCC ccc : cccList) {
            List<Keyword> kws = kwDAO.getAllKeywords(ccc.getId());
            selectedCCCWithKeywords.put(ccc, kws);
        }
        return selectedCCCWithKeywords;
    }

    private void setMainConcern(List<CCC> identifiedConcerns, CCCIndication indication) {
        if (indication.getMainConcern() == null) {
            MainConcern mainConcern = mcDAO.getByRequirement(iu.getId(), indication.getReq());
            if (mainConcern != null) {
                indication.setMainConcern(mainConcern.getMainConcern());
                List<CCC> auxCCC = new ArrayList<CCC>(identifiedConcerns);
                auxCCC.remove(mainConcern.getMainConcern());
                if (!crosscuttingConcerns.containsKey(mainConcern.getMainConcern().getId())) {
                    crosscuttingConcerns.put(mainConcern.getMainConcern().getId(), auxCCC);
                } else {
                    crosscuttingConcerns.put(mainConcern.getMainConcern().getId(),
                            mergeCCCList(crosscuttingConcerns.get(mainConcern.getMainConcern().getId()), auxCCC));
                }
            }
        } else if (indication.getMainConcern() != null) {
            List<CCC> auxCCC = new ArrayList<CCC>(identifiedConcerns);
            crosscuttingConcerns.put(indication.getMainConcern().getId(),
                    mergeCCCList(crosscuttingConcerns.get(indication.getMainConcern().getId()), auxCCC));

        }
    }

    private CCCIndication identifyCCC(Requirement requirement) throws IOException, ParseException, InvalidTokenOffsetsException {
        final CCCIndication indication = new CCCIndication();
        final List<CCC> identifiedCCCByRequirement = new ArrayList<CCC>();

        for (Entry<CCC, List<Keyword>> entry : selectedCCC.entrySet()) {
            StringBuilder kwStr = new StringBuilder();
            for (Keyword kw : entry.getValue()) {
                kwStr.append(kw.getName()).append(", ");
            }
            String result = areThereKeywordsInRequirement(kwStr.toString(), requirement);
            if (result != null) {
                CCC ccc = entry.getKey();
                CCC cloneCCC = new CCC();
                cloneCCC.setId(ccc.getId());
                cloneCCC.setName(ccc.getName());
                cloneCCC.setFragments(result);
                identifiedCCCByRequirement.add(cloneCCC);
                if (identifiedCCC.add(ccc)) {
                    numOfIdentifiedCCC++;
                }
            }
        }

        if (!identifiedCCCByRequirement.isEmpty()) {
            numOfAffectedRequirements++;
        }
        indication.setReq(requirement);
        indication.setCccIndications(identifiedCCCByRequirement);
        setMainConcern(identifiedCCCByRequirement, indication);
        return indication;
    }

    private String areThereKeywordsInRequirement(String kws, Requirement requirement) throws IOException, ParseException, InvalidTokenOffsetsException {
        StringBuilder strQuery = new StringBuilder();
        StringTokenizer stk = new StringTokenizer(kws, ",");
        while (stk.hasMoreTokens()) {
            String kw = stk.nextToken().trim();
            if (kw.isEmpty()) {
                break;
            }
            strQuery.append(kw).append(" ");
        }
        if (strQuery.length() == 0) {
            return null;
        }

        Analyzer analyzer = new StandardAnalyzer();
        Query query = new QueryParser(SEARCH_FIELD_NAME, analyzer).parse(strQuery.toString());
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
        return highlighter.getBestFragment(analyzer, SEARCH_FIELD_NAME, requirement.getDescription());
    }

    private void beforeExecuting() throws IOException {
        selectedCCC = getSelectedCCCWithKeywords();
        selectedRequirements = getAllIdentificationUnitRequirements();
        numOfAffectedRequirements = 0;
        numOfIdentifiedCCC = 0;
        identifiedCCC.clear();
        crosscuttingConcerns.clear();
        nonFunctionalConcerns.clear();
        indications.clear();
        errorMessages.clear();
        warningMessages.clear();
    }

    private void afterExecuting() throws IOException {
        checkRequirementDependencies();
        checkMainConcerns();
        checkConcernDependencies();
        checkPositiveContributions();
        checkNonCrosscuttingNonFunctionalConcerns();
    }

    public String executeIdentificationUnit() {
        try {
            beforeExecuting();
            for (Requirement req : selectedRequirements) {
                CCCIndication indication = identifyCCC(req);
                indications.add(indication);
            }
            afterExecuting();
            return "iuResult.xhtml?faces-redirect=true";
        } catch (ParseException ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_lucene"),
                    null);
            return null;
        } catch (IOException ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_lucene"),
                    null);
            return null;
        } catch (InvalidTokenOffsetsException ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_lucene"),
                    null);
            return null;
        }
    }

    public String backToIdentificationUnits() {
        iu = Session.getActivSettings().getIdentificationUnit();
        return "iu.xhtml?faces-redirect=true";
    }

    public String saveIdentificationUnit() {
        Project activeProject = Session.getActiveProject();
        if (iu.getId() == null) {
            if (iuDAO.alreadyExistingIU(activeProject.getId(), iu)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_iu"),
                        null);
                return null;
            }
        }
        iu.setProject(activeProject);
        iuDAO.save(iu);
        iu = Session.getActivSettings().getIdentificationUnit();
        return "iu.xhtml?faces-redirect=true";
    }

    public String removeIdentificationUnit() {
        try {
            iuDAO.remove(iu);
            iu = Session.getActivSettings().getIdentificationUnit();
            return "iu.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_constraint_exception"),
                    null);
            return null;
        }
    }

    public void clearFilter() {
        filteredIndications.clear();
    }

    public void filter() {
        filteredIndications.clear();
        CCC mc = cccDAO.getCCCById(mainConcernId);
        CCC ccc = cccDAO.getCCCById(cccId);
        for (CCCIndication cccIndication : indications) {
            if (!cccIndication.getMainConcern().equals(mc)) {
                continue;
            }
            if (cccIndication.getCccIndications().contains(ccc)) {
                filteredIndications.add(cccIndication);
            }
        }
    }

    public List<IdentificationUnit> getAllIdentificationUnits() {
        return iuDAO.getAllIdentificationUnits(Session.getActiveProject().getId());
    }

    public List<CCC> getAllIdentificationUnitCCC() {
        return cccDAO.getAllCCC(iu.getOntology().getId());
    }

    public List<Requirement> getAllIdentificationUnitRequirements() {
        return reqDAO.getAllRequirements(iu.getProject().getId());
    }

    public IdentificationUnit getIu() {
        return iu;
    }

    public void setIu(IdentificationUnit iu) {
        this.iu = iu;
    }

    public List<CCCIndication> getIndications() {
        if (filteredIndications.isEmpty()) {
            return indications;
        }
        return filteredIndications;
    }

    public void setIndications(List<CCCIndication> indications) {
        this.indications = indications;
    }

    public int getNumOfIdentifiedCCC() {
        return numOfIdentifiedCCC;
    }

    public void setNumOfIdentifiedCCC(int numOfIdentifiedCCC) {
        this.numOfIdentifiedCCC = numOfIdentifiedCCC;
    }

    public int getNumOfAffectedRequirements() {
        return numOfAffectedRequirements;
    }

    public void setNumOfAffectedRequirements(int numOfAffectedRequirements) {
        this.numOfAffectedRequirements = numOfAffectedRequirements;
    }

    public List<CCC> getIdentifiedCCC() {
        return new ArrayList<CCC>(identifiedCCC);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }

    public void setWarningMessages(List<String> warningMessages) {
        this.warningMessages = warningMessages;
    }

    public List<CCC> getSelectedCCC() {
        return new ArrayList<CCC>(selectedCCC.keySet());
    }

    public void setSelectedCCC(Map<CCC, List<Keyword>> selectedCCC) {
        this.selectedCCC = selectedCCC;
    }

    public List<Requirement> getSelectedRequirements() {
        return selectedRequirements;
    }

    public void setSelectedRequirements(List<Requirement> selectedRequirements) {
        this.selectedRequirements = selectedRequirements;
    }

    public List<List<Boolean>> getCrosscuttingMatrix() {
        return crosscuttingMatrix;
    }

    public List<List<String>> getContributionMatrix() {
        return contributionMatrix;
    }

    public Long getMainConcernId() {
        return mainConcernId;
    }

    public void setMainConcernId(Long mainConcernId) {
        this.mainConcernId = mainConcernId;
    }

    public Long getCccId() {
        return cccId;
    }

    public void setCccId(Long cccId) {
        this.cccId = cccId;
    }

}
