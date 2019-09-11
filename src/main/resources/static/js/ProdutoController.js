class ProdutoController {
    carregarLista(lista) {
        let corpoTabela = document.getElementById("corpoTabela");
        corpoTabela.innerHTML = "";
        for (let i = 0; i < lista.length; i++) {
            let linha = corpoTabela.insertRow();

            let idCell = linha.insertCell();
            idCell.innerHTML = lista[i].id;

            let nomeCell = linha.insertCell();
            nomeCell.innerHTML = lista[i].nome;

            let valorCell = linha.insertCell();
            valorCell.innerHTML = lista[i].valor;

            let estoqueCell = linha.insertCell();
            estoqueCell.innerHTML = lista[i].estoque;

            let tipoCell = linha.insertCell();
            tipoCell.innerHTML = lista[i].tipo;

            let apagarCell = linha.insertCell();
            apagarCell.innerHTML = `<button 
                         onclick="produtoController.apagar(${lista[i].id})">Apagar</button>`;

            let editarCell = linha.insertCell();
            editarCell.innerHTML = `<button 
                         onclick="produtoController.editarItem(${lista[i].id})">Editar</button>`;
        }
    }

    editarItem(id) {
        fetch(`api/produtos/${id}`, {
            method: "GET"
        }).then((resposta) => {
            if (resposta.ok) {
                resposta.json().then(
                    (item) => {
                        this.atribuirItem(item);
                    }
                );
            }
        });
    }
    atribuirItem(item) {
        document.getElementById("id").value = item.id;
        document.getElementById("nome").value = item.nome;
        document.getElementById("valor").value = item.valor;
        document.getElementById("estoque").value = item.estoque;
        document.getElementById("tipo").value = item.tipo;
    }

    apagar(id) {
        fetch(`api/produtos/${id}`, {
            method: "DELETE"
        }
        ).then((resposta) => {
            if (resposta.ok) {
                this.listar();
            } else {
                console.log("Erro ao apagar");
            }
        });
    }

    pesquisar() {
        let pesquisa = document.getElementById("pesquisar").value;
        fetch(`/api/produtos/pesquisar/nome/?contem=${pesquisa}`, { method: "GET" })
            .then((resultado) => {
                if (resultado.ok) {
                    // retorno ok
                    resultado.json().then(
                        (lista) => {
                            this.carregarLista(lista);
                            console.log(lista);
                        }
                    );
                } else {
                    // tratar o erro 
                    console.log("Erro na excecução");
                }
            });
    }

    listar() {
        fetch("api/produtos/", { method: "GET" })
            .then((resultado) => {
                if (resultado.ok) {
                    // retorno ok
                    resultado.json().then(
                        (lista) => {
                            this.carregarLista(lista);
                            console.log(lista);
                        }
                    );
                } else {
                    // tratar o erro 
                    console.log("Erro na excecução");
                }
            });
    }

    confirmar() {
        let id = document.getElementById("id").value;
        let nome = document.getElementById("nome").value;
        let valor = document.getElementById("valor").valueAsNumber;
        let estoque = document.getElementById("estoque").value;
        let tipo = document.getElementById("tipo").value;

        let item = {
            nome: nome,
            valor: valor,
            estoque: estoque,
            tipo: tipo
        };
        if (id == "") {
            this.inserir(item);
        } else {
            this.editar(id, item);
        }
    }

    editar(id, item) {
        fetch(`api/produtos/${id}`, {
            method: "PUT",
            headers: new Headers({
                'Content-Type': 'application/json'
            }),
            body: JSON.stringify(item)
        }).then((resultado) => {
            if (resultado.ok) {
                this.limpar();
                this.listar();
            }
        });
    }

    limpar() {
        document.getElementById("id").value = "";
        document.getElementById("nome").value = "";
        document.getElementById("valor").value = "";
        document.getElementById("estoque").value = "";
        document.getElementById("tipo").value = "";
    }

    inserir(item) {
        fetch("api/produtos/", {
            method: "POST",
            headers: new Headers({
                'Content-Type': 'application/json'
            }),
            body: JSON.stringify(item)
        }).then((resultado) => {
            if (resultado.ok) {
                this.listar();
            } else {
                console.log("Erro na execução");
            }
        });
    }
}
