/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.testes.nicholas.superloja.controller;

import br.edu.ifrs.restinga.testes.nicholas.superloja.erro.NaoEncontrado;
import br.edu.ifrs.restinga.testes.nicholas.superloja.erro.RequisicaoInvalida;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao.GeneroDAO;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao.ModeloDAO;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao.ProdutoDAO;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Genero;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Modelo;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Produto;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jezer
 */

@RestController
@RequestMapping("/api")
public class Produtos {
    
    @Autowired
    ProdutoDAO produtoDAO;
    
    @Autowired
    ModeloDAO modeloDAO;
    
    @Autowired
    GeneroDAO generoDAO;
    
    @RequestMapping(path = "/produtos/pesquisar/valor", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Produto> pesquisaValor(
            @RequestParam float inicio,
            @RequestParam float fim
            ) {
        return produtoDAO.findByValorBetween(inicio, fim);
    }
    
    
    @RequestMapping(path = "/produtos/pesquisar/genero", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Produto> pesquisaGenero(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Boolean perecivel) {
        if(perecivel!=null&&id!=null) {
            throw new RequisicaoInvalida("Informar ou id ou perecivel!");
        }
        if(perecivel!=null) {
            return produtoDAO.findByGeneroPerecivel(perecivel);
        } else if(id!=null) {
            Optional<Genero> optionalGenero = generoDAO.findById(id);
            if(!optionalGenero.isPresent())
                throw new NaoEncontrado("ID não encontrado!");
            Genero genero = optionalGenero.get();
            return produtoDAO.findByGenero(genero);
        } else {
            throw new RequisicaoInvalida("Informar id ou perecivel!");
        }
    }
    

    @RequestMapping(path = "/produtos/pesquisar/nome", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Produto> pesquisaNome(
            @RequestParam(required = false) String contem,
            @RequestParam(required = false) String comeca
            ) {
        if(contem!=null)
            return produtoDAO.findByNomeContaining(contem);
        else if(comeca!=null)
            return produtoDAO.findByNomeStartingWith(comeca);
        else 
            throw new RequisicaoInvalida("Indicar contem ou comeca");
    }

    
    
    @RequestMapping(path = "/produtos/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Produto> listar() {
        return produtoDAO.findAll();    
    }
    
    @RequestMapping(path = "/produtos/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Produto buscar(@PathVariable int id) {
        final Optional<Produto> findById = produtoDAO.findById(id);
        if(findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("ID não encontrada!");
        }
    }
    
    @RequestMapping(path="/produtos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apagar(@PathVariable int id) {
        if(produtoDAO.existsById(id)) {
            produtoDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("ID não encontrada!");
        }
                
        
    }
    
    @RequestMapping(path="/produtos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable int id, @RequestBody Produto produto) {
        final Produto produtoBanco = this.buscar(id);

        if(produto.getValor()<0) {
            throw new RequisicaoInvalida("Valor do produto deve ser maior que 0");
        }
        
        produtoBanco.setNome(produto.getNome());
        produtoBanco.setValor(produto.getValor());

        produtoBanco.setGenero(produto.getGenero());
        produtoDAO.save(produtoBanco);
    }
    

    @RequestMapping(path = "/produtos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Produto cadastrar(@RequestBody Produto produto) {
        if(produto.getValor()<0) {
            throw new RequisicaoInvalida("Valor do produto deve ser maior que 0");
        } else {
            
            Produto produtoBanco = produtoDAO.save(produto);
            return produtoBanco;
        }
    }

    @RequestMapping(path = "/produtos/{idProduto}/modelos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Modelo cadastrarModelo(@PathVariable int idProduto, @RequestBody Modelo modelo) {
        Produto produtoBanco = this.buscar(idProduto);
        modelo.setId(0);
        Modelo modeloBanco=modeloDAO.save(modelo);
        produtoBanco.getModelos().add(modeloBanco);
        produtoDAO.save(produtoBanco);
        return modeloBanco;
    } 

    @RequestMapping(path = "/produtos/{idProduto}/modelos/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Modelo> listarModelo(@PathVariable int idProduto) {
        return this.buscar(idProduto).getModelos();
    }

  
}
