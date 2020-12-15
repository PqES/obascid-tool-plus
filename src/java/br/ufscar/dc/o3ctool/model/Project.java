package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({  
    @NamedQuery(name = Project.GET_ALL_BY_RESEARCHER, query = "select p from Project p where p.researcher.id = :resid"),
    @NamedQuery(name = Project.GET_BY_NAME, query = "select p from Project p where p.researcher.id = :resid and p.name = :name"),
    @NamedQuery(name = Project.SEARCH_PROJECTS, query = "select p from Project p where (p.name like :search or p.description like :search) and p.license = :license")
})    
public class Project implements Serializable {
    public static enum ProjectLicense {
        PUBLIC, PRIVATE;
    }
    
    public static final String GET_ALL_BY_RESEARCHER = "Project.GET_ALL";
    public static final String GET_BY_NAME = "Project.GET_BY_NAME";
    public static final String SEARCH_PROJECTS = "Project.SEARCH_PROJECTS";
    
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectLicense license;
    @ManyToOne(optional = false)
    private Researcher researcher;
    private int views;
    private int imports;

    public Project() {
        this.id = null;
        this.name = "";
        this.description = "";
        this.license = ProjectLicense.PRIVATE;
        this.researcher = new Researcher();
        this.views = 0;
        this.imports = 0;
    }
    
    public Project(Project prj, Researcher res) {
        this.id = null;
        this.name = prj.getName();
        this.description = prj.getDescription();
        this.license = ProjectLicense.PRIVATE;
        this.researcher = res;
        this.views = 0;
        this.imports = 0;
    }

    public boolean isLicencePublic() {
        return (ProjectLicense.PUBLIC == this.license);
    }

    public boolean isLicencePrivate() {
        return (ProjectLicense.PRIVATE == this.license);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectLicense getLicense() {
        return license;
    }

    public void setLicense(ProjectLicense license) {
        this.license = license;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getImports() {
        return imports;
    }

    public void setImports(int imports) {
        this.imports = imports;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
