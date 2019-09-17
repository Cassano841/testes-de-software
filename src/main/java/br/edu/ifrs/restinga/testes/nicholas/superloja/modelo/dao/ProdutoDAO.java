
package br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao;

import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Produto;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoDAO extends CrudRepository<Produto, Integer>  {
    List<Produto> findByNomeContaining(String nome);
    List<Produto> findByNomeStartingWith(String nome);
    List<Produto> findByTipoContaining(String tipo);
    List<Produto> findByTipoStartingWith(String tipo);
}
