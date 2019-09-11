/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.dao;

import br.edu.ifrs.restinga.testes.nicholas.superloja.modelo.entidade.Modelo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jezer
 */

@Repository
public interface ModeloDAO extends CrudRepository<Modelo, Integer> {
    
}
