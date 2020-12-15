/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class Filiation implements Serializable {

    private String institution;
    private String city;
    private String state;
    private String country;

    public Filiation() {
        this.institution = "";
        this.city = "";
        this.state = "";
        this.country = "";
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
