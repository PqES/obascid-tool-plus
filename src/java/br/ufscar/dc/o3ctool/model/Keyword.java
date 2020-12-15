/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Paulo
 */
@Entity
@NamedQueries({
    @NamedQuery(name = Keyword.GET_ALL, query = "select kw from Keyword kw where kw.ccc.id = :cccid"),
    @NamedQuery(name = Keyword.GET_BY_NAME, query = "select kw from Keyword kw where kw.ccc.id = :cccid and kw.name = :name")
})
public class Keyword implements Serializable {

    public static final String GET_ALL = "Keyword.GET_ALL";
    public static final String GET_BY_NAME = "Keyword.ALREADY_EXISTING_KEYWORD";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional = false)
    private CCC ccc;

    public Keyword() {
        this.id = null;
        this.name = "";
        this.ccc = new CCC();
    }

    public Keyword(Keyword kw, CCC ccc) {
        this.id = null;
        this.name = kw.getName();
        this.ccc = ccc;
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

    public CCC getCcc() {
        return ccc;
    }

    public void setCcc(CCC ccc) {
        this.ccc = ccc;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        final Keyword other = (Keyword) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }

}
