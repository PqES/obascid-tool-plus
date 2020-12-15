/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.model;

import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Stopwords implements Serializable {
    @Lob
    private String stopwords;

    public Stopwords() {
        this.stopwords = i18n.getProperty("stopwords");
    }
    
    public String getStopwords() {
        return stopwords;
    }

    public void setStopwords(String stopwords) {
        this.stopwords = stopwords;
    }
}
