package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.CCCDAO;
import br.ufscar.dc.o3ctool.dao.jpa.CCCJPA;
import br.ufscar.dc.o3ctool.dao.jpa.DependencyDAO;
import br.ufscar.dc.o3ctool.dao.jpa.DependencyJPA;
import br.ufscar.dc.o3ctool.dao.jpa.KeywordDAO;
import br.ufscar.dc.o3ctool.dao.jpa.KeywordJPA;
import br.ufscar.dc.o3ctool.dao.jpa.OntologyDAO;
import br.ufscar.dc.o3ctool.dao.jpa.OntologyJPA;
import br.ufscar.dc.o3ctool.dao.jpa.ProjectDAO;
import br.ufscar.dc.o3ctool.dao.jpa.ProjectJPA;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipJPA;
import br.ufscar.dc.o3ctool.dao.jpa.RequirementDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RequirementJPA;
import br.ufscar.dc.o3ctool.dao.jpa.SourceDAO;
import br.ufscar.dc.o3ctool.dao.jpa.SourceJPA;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.Dependency;
import br.ufscar.dc.o3ctool.model.Keyword;
import br.ufscar.dc.o3ctool.model.Ontology;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Relationship;
import br.ufscar.dc.o3ctool.model.Requirement;
import br.ufscar.dc.o3ctool.model.Researcher;
import br.ufscar.dc.o3ctool.model.Source;
import br.ufscar.dc.o3ctool.model.Standard;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "repobean")
@ViewScoped
public class RepositoryBean implements Serializable {

    private final OntologyDAO ontoDAO;
    private final CCCDAO cccDAO;
    private final RequirementDAO reqDAO;
    private final DependencyDAO depDAO;
    private final KeywordDAO kwDAO;
    private final SourceDAO sourceDAO;
    private final RelationshipDAO relatDAO;
    private final ProjectDAO projDAO;
    private Ontology onto;
    private Project proj;
    private final int ONTOLOGIES_BY_PAGE = 10;
    private final int PROJECTS_BY_PAGE = 10;
    private int pageOntologies;
    private int pageProjects;
    private String searchOntologies;
    private String searchProjects;

    public RepositoryBean() {
        ontoDAO = new OntologyJPA();
        cccDAO = new CCCJPA();
        reqDAO = new RequirementJPA();
        depDAO = new DependencyJPA();
        kwDAO = new KeywordJPA();
        sourceDAO = new SourceJPA();
        relatDAO = new RelationshipJPA();
        projDAO = new ProjectJPA();
        pageOntologies = 1;
        pageProjects = 1;
        searchProjects = "";
        searchOntologies = "";
        onto = new Ontology();
        proj = new Project();
    }

    public List<Ontology> getAllOntologies() {
        return ontoDAO.searchOntologies(searchOntologies, pageOntologies * ONTOLOGIES_BY_PAGE);
    }

    public List<Project> getAllProjects() {
        return projDAO.searchProjects(searchProjects, pageProjects * PROJECTS_BY_PAGE);
    }

    private Ontology saveOntology() {
        Researcher activeResearcher = Session.getActivResearcher();
        Ontology newOnto = new Ontology(onto, activeResearcher);
        if (ontoDAO.alreadyExistingOntology(activeResearcher.getId(), newOnto)) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_de_ontology"),
                    null);
            return null;
        }
        ontoDAO.save(newOnto);
        return newOnto;
    }

    private CCC saveCCC(CCC ccc, Ontology onto) {
        CCC newCCC = new CCC(ccc, onto);
        cccDAO.save(newCCC);
        return newCCC;
    }

    private void saveRelationship(Relationship relat, Ontology newOnto) {
        CCC newSource = cccDAO.getCCCByName(newOnto.getId(), relat.getSource().getName());
        CCC newTarget = cccDAO.getCCCByName(newOnto.getId(), relat.getTarget().getName());;
        Relationship newRelat = new Relationship(relat, newSource, newTarget, newOnto);
        relatDAO.save(newRelat);
    }

    private void saveKeyword(Keyword kw, CCC ccc) {
        Keyword newKw = new Keyword(kw, ccc);
        kwDAO.save(newKw);
    }

    private void importKeywords(CCC ccc, CCC newCCC) {
        List<Keyword> lstKws = kwDAO.getAllKeywords(ccc.getId());
        for (Keyword kw : lstKws) {
            saveKeyword(kw, newCCC);
        }
    }

    private void saveSource(Source source, CCC ccc) {
        Source newSource = new Source(source, ccc);
        sourceDAO.save(newSource);
    }

    private void importSources(CCC ccc, CCC newCCC) {
        List<Source> lstSources = sourceDAO.getAllSources(ccc.getId());
        for (Source source : lstSources) {
            saveSource(source, newCCC);
        }
    }

    private void importCCC(Ontology newOnto) {
        List<CCC> lstCCC = cccDAO.getAllCCC(onto.getId());
        for (CCC ccc : lstCCC) {
            CCC newCCC = saveCCC(ccc, newOnto);
            importKeywords(ccc, newCCC);
            importSources(ccc, newCCC);
        }
    }

    private void importRelationships(Ontology newOnto) {
        List<Relationship> lstRelationships = relatDAO.getAllRelationships(onto.getId());
        for (Relationship relat : lstRelationships) {
            saveRelationship(relat, newOnto);
        }
    }

    public String importOntology() {
        Ontology newOnto = saveOntology();
        if (newOnto != null) {
            importCCC(newOnto);
            importRelationships(newOnto);
            onto = Session.getActivSettings().getOntology();
            return "ontoRepository.xhtml?faces-redirect=true";
        }
        return null;
    }

    private Project saveProject() {
        Researcher activeResearcher = Session.getActivResearcher();
        Project newProject = new Project(proj, activeResearcher);
        if (projDAO.alreadyExistingProject(activeResearcher.getId(), newProject)) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_de_project"),
                    null);
            return null;
        }
        projDAO.save(newProject);
        return newProject;
    }

    private void saveRequirement(Requirement req, Project project) {
        Requirement newRequirement = new Requirement(req, project);
        reqDAO.save(newRequirement);
    }

    private void importRequirements(Project newProject) {
        List<Requirement> lstReqs = reqDAO.getAllRequirements(proj.getId());
        for (Requirement req : lstReqs) {
            saveRequirement(req, newProject);
        }
    }

    private void saveDependency(Dependency dep, Project project) {
        Requirement newSource = reqDAO.getByName(project.getId(), dep.getSource().getName());
        Requirement newTarget = reqDAO.getByName(project.getId(), dep.getTarget().getName());
        Dependency newDep = new Dependency(dep, newSource, newTarget, project);
        depDAO.save(newDep);
    }

    private void importDependencies(Project newProject) {
        List<Dependency> lstDeps = depDAO.getAllDependencies(proj.getId());
        for (Dependency dep : lstDeps) {
            saveDependency(dep, newProject);
        }
    }

    public String importProject() {
        Project newProject = saveProject();
        if (newProject != null) {
            importRequirements(newProject);
            importDependencies(newProject);
            proj = Session.getActivSettings().getProject();
            return "projectRepository.xhtml?faces-redirect=true";
        }
        return null;

    }

    public void moreOntologies() {
        pageOntologies++;
    }

    public void moreProjects() {
        pageProjects++;
    }

    public String getSearchOntologies() {
        return searchOntologies;
    }

    public void setSearchOntologies(String searchOntologies) {
        this.searchOntologies = searchOntologies;
    }

    public String getSearchProjects() {
        return searchProjects;
    }

    public void setSearchProjects(String searchProjects) {
        this.searchProjects = searchProjects;
    }

    public Ontology getOnto() {
        return onto;
    }

    public void setOnto(Ontology onto) {
        this.onto = onto;
    }

    public Project getProj() {
        return proj;
    }

    public void setProj(Project proj) {
        this.proj = proj;
    }

}
