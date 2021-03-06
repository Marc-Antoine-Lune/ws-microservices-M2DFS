package com.ecommerce.microcommerce.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
//@JsonFilter("monFiltreDynamique")
public class Product {

    @Id
    @GeneratedValue
    @ApiModelProperty(
            value= "id du produit",
            name = "id",
            dataType = "Int",
            required = true
    )
    private int id;

    @ApiModelProperty(
            value= "Nom du produit",
            name = "nom",
            dataType = "String",
            required = true
    )
    @Length(min=3, max=20, message = "Nom trop long ou trop court. Et oui messages sont plus stylés que ceux de Spring")
    private String nom;

    @ApiModelProperty(
            value= "prix du produit",
            name = "prix",
            dataType = "int",
            required = true
    )
    //@Min(value = 1) Mis en commentaire car la validation s'effectue avant la levée de l'exception
    private int prix;

    //information que nous ne souhaitons pas exposer
    @ApiModelProperty(
            value= "Prix d'achat du produit",
            name = "prixAchat",
            dataType = "int",
            required = true
    )
    private int prixAchat;

    //constructeur par défaut
    public Product() {
    }

    //constructeur pour nos tests
    public Product(int id, String nom, int prix, int prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(int prixAchat) {
        this.prixAchat = prixAchat;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
