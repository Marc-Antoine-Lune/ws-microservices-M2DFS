package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.implementation.bytecode.Throw;
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

@Api( description="API Tp MicroService M2DFS")
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

    @ApiResponses({
            @ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })


    //Récupérer la liste des produits
    @ApiOperation(value = "Récupère la liste de tous les produits")
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
    @ApiOperation(value = "Récupère un produit grâce à son id")
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
    @ApiOperation(value = "Ajoute un produit")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
        Product productAdded =  productDao.save(product);
        System.out.println(productAdded.getPrix());
        System.out.println(productAdded.getNom());
        if(productAdded.getPrix() == 0 ){
            throw new ProduitGratuitException("Erreur de requete: Le prix de vente ne peut pas être egal à 0 Rien n'est gratuit");
        }

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // supprimer un produit
    @ApiOperation(value = "Supprime un produit")

    @DeleteMapping("deleteById/{productId}")
    public void supprimerProduit(@PathVariable int productId) {
        for(Product product : productList) {
            if (product.getId() == (productId)) {
                productList.remove(product);
            }
        }

    }

    // Mettre à jour un produit
    @ApiOperation(value = "Met à jour un produit")
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

    //Calculer la marge d'un produit
    @ApiOperation(value = "Calculer la marge d'un produit")
    @GetMapping("/AdminProduits")
    public Map<String, Integer> calculerMargeProduit(){
        Map<String, Integer> response = new HashMap<String, Integer>();
        for(Product p : productList){
            response.put(p.toString(), p.getPrix()-p.getPrixAchat());
        }

        return response;
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }

    @ApiOperation(value = "Recupere les produits par ordre alphabetique")
    @GetMapping("/productsOrder")
    public List<Product> trierProduitsParOrdreAlphabetique(){
        return productDao.trierProduitsParOrdreAlphabetique();
    }


}
