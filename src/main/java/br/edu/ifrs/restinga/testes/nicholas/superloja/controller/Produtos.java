package br.edu.ifrs.restinga.testes.nicholas.superloja.controller;

import br.edu.ifrs.restinga.testes.nicholas.superloja.erro.NaoEncontrado;
import br.edu.ifrs.restinga.testes.nicholas.superloja.erro.RequisicaoInvalida;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao.GeneroDAO;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao.ProdutoDAO;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Genero;
import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Produto;
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
    GeneroDAO generoDAO;
    
    @RequestMapping(path = "/produtos/pesquisar/tipo", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Produto> pesquisaTipo(
            @RequestParam(required = false) String contem,
            @RequestParam(required = false) String comeca
            ) {
        if(contem!=null)
            return produtoDAO.findByTipoContaining(contem);
        else if(comeca!=null)
            return produtoDAO.findByTipoStartingWith(comeca);
        else 
            throw new RequisicaoInvalida("Indicar contem ou comeca");
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

        if(produto.getValor() <= 0) {
            throw new RequisicaoInvalida("Valor do produto deve ser maior que 0");
        } else if (produto.getNome() == ""){
            throw new RequisicaoInvalida("Nome deve ser preenchido!");
        } else if (produto.getTipo() == ""){
            throw new RequisicaoInvalida("Tipo do produto deve ser preenchido!");
        } else if (produto.getEstoque() < 0){
            throw new RequisicaoInvalida("Estoque deve ser preenchido!");
        } else {
        
        produtoBanco.setNome(produto.getNome());
        produtoBanco.setValor(produto.getValor());
        produtoBanco.setTipo(produto.getTipo());
        produtoBanco.setEstoque(produto.getEstoque());
        
        }
        produtoDAO.save(produtoBanco);
    }
    

    @RequestMapping(path = "/produtos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Produto cadastrar(@RequestBody Produto produto) {
        if(produto.getValor() <= 0) {
            throw new RequisicaoInvalida("Valor do produto deve ser maior que 0");
        } else if (produto.getNome() == ""){
            throw new RequisicaoInvalida("Nome deve ser preenchido!");
        } else if (produto.getTipo() == ""){
            throw new RequisicaoInvalida("Tipo do produto deve ser preenchido!");
        } else if (produto.getEstoque() < 0){
            throw new RequisicaoInvalida("Estoque deve ser preenchido!");
        } else {
            Produto produtoBanco = produtoDAO.save(produto);
            return produtoBanco;
        }
    }
}
