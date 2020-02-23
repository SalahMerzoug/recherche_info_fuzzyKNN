/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pp;

/**
 *
 * @author Salah_Mer
 */
public class reslt {
     public String document;
    public String dossier;
    public String percent;

    public reslt(String document, String dossier, String percent) {
        this.document = document;
        this.dossier = dossier;
        this.percent = percent;
    }

    public reslt(String dossier, String percent) {
        this.dossier = dossier;
        this.percent = percent;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
    
}
