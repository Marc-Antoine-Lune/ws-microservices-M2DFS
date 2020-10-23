package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    private static  List<Product> productList = new ArrayList<Product>() {

        {
            add(new Product(1, "Ordinateur portable", 350, 120));
            add(new Product(2, "Aspirateur Robot", 500, 200));
            add(new Product(3, "Table de ping pong", 750, 400));
            add(new Product(4, "Table de ping pong", 759, 450));

        }
    };


    //Récupérer la liste des produits
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }


    //Récupérer un produit par son id
    @RequestMapping(value = "/getById/{productId}", method = RequestMethod.GET )
    public Product afficherUnProduit(@PathVariable int productId) {
        Product response = null;
        for(Product product : productList) {
            if (product.getId() == (productId)) {
                response = product;
            }
        }
        return response;
    }




    //ajouter un produit
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
        //System.out.println(product.);

        /*Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();*/
        //productList.add(new Product(product.getId(), product.getNom(), product.getPrixAchat(), product.getPrix()));
    return null;
    }

    // supprimer un produit
    @DeleteMapping("deleteById/{productId}")
    public void supprimerProduit(@PathVariable int productId) {
        for(Product product : productList) {
            if (product.getId() == (productId)) {
                productList.remove(product);
            }
        }

    }

    // Mettre à jour un produit
    @PutMapping("/updateById/{productId}")
    public void updateProduit(@PathVariable int productId, @RequestBody Product product) {

        for(Product p : productList) {
            if (p.getId() == (productId)) {
                p.setId(product.getId());
                p.setNom(product.getNom());
                p.setPrix(product.getPrix());
                p.setPrixAchat(product.getPrixAchat());
            }
        }
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }



}
