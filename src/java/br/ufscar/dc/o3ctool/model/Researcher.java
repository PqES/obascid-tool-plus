package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = Researcher.GET_BY_EMAIL, query = "select r from Researcher r where r.email = :email")
public class Researcher implements Serializable {

    public static final String GET_BY_EMAIL = "Researcher.GET_BY_EMAIL";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Embedded
    private Stopwords stopwords;
    @Embedded
    private Filiation filiation;
    @Embedded
    private Settings settings;

    public Researcher() {
        this.id = null;
        this.name = "";
        this.email = "";
        this.password = "";   
        this.stopwords = new Stopwords();
        this.filiation = new Filiation();
        this.settings = new Settings();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stopwords getStopwords() {
        return stopwords;
    }

    public void setStopwords(Stopwords stopwords) {
        this.stopwords = stopwords;
    }

    public Filiation getFiliation() {
        return filiation;
    }

    public void setFiliation(Filiation filiation) {
        this.filiation = filiation;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Researcher other = (Researcher) obj;
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        return true;
    }

}
