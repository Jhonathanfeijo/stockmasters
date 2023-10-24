function deleteProductById(id) {
    return fetch(`http://localhost:8080/produto/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.status === 204) {
                console.log(`Produto com ID ${id} deletado com sucesso.`);
            } else {
                console.error(`Erro ao deletar produto com ID ${id}`);
            }
        })
        .catch(error => {
            console.error('Erro:', error);
        });
}
function fetchProducts() {
    var listProducts = document.querySelectorAll(".product")
    listProducts.forEach(product => {
        product.style.display = "none"
    })
    var table = $("table");
    fetch('http://localhost:8080/produto', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(res => {
            var produtos = res.content;
            produtos.forEach(function (produto) {
                var tr = $("<tr>");
                tr.addClass("product")

                var thId = $("<th>");
                thId.attr("scope", "row");
                thId.text(produto.codigo);

                var tdDescricao = $("<td>");
                tdDescricao.text(produto.descricao);

                var tdValor = $("<td>");
                tdValor.text("R$ " + parseFloat(produto.valor).toFixed(2));

                var tdQuantidade = $("<td>");
                tdQuantidade.text(produto.quantidade);

                var tdBotaoAlterar = $("<td>");
                var botaoAlterar = $("<button>");
                botaoAlterar.addClass("btn btn-primary");
                botaoAlterar.text("Alterar");
                botaoAlterar.click(() => openEditModal(produto));
                tdBotaoAlterar.append(botaoAlterar);

                var tdBotaoRemover = $("<td>");
                var botaoRemover = $("<button>");
                botaoRemover.addClass("btn btn-danger");
                botaoRemover.text("Deletar");
                botaoRemover.click(() => openDeleteModal(produto));
                tdBotaoRemover.append(botaoRemover);

                tr.append(thId);
                tr.append(tdDescricao);
                tr.append(tdValor);
                tr.append(tdQuantidade);
                tr.append(tdBotaoAlterar);
                tr.append(tdBotaoRemover);

                table.append(tr);
            });
        })
        .catch(error => {
            console.error('Erro:', error);
        });
}
function openEditModal(produto) {
    $('#editProductModal').modal('show');
    $('#productDescriptionEdit').val(produto.descricao);
    $('#productPriceEdit').val(produto.valor);
    $('#productQuantityEdit').val(produto.quantidade);

    $('#productPriceEdit').on('input', function () {
        this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
    });

    $('#productQuantityEdit').on('input', function () {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    $('#saveChangesButton').off('click').on('click', () => {
        const id = produto.codigo;
        const descricao = $('#productDescriptionEdit').val();
        const valor = $('#productPriceEdit').val();
        const quantidade = $('#productQuantityEdit').val();
        const data = { descricao, valor, quantidade };
        updateProduct(id, data);
    });
}
function openDeleteModal(produto) {
    $('#deleteProductModal').modal('show');
    $('#confirmDeleteButton').off('click').on('click', () => {
        deleteProductById(produto.codigo)
            .then(() => {
                $('#deleteProductModal').modal('hide');
                location.reload();
            });
    });
}

function updateProduct(id, data) {
    return fetch(`http://localhost:8080/produto/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (response.ok) {
                console.log('Produto atualizado com sucesso.');
                $('#editProductModal').modal('hide');
                location.reload();
            } else {
                console.error('Falha ao atualizar o produto.');
            }
        })
        .catch(error => {
            console.error('Erro:', error);
        });
}

function addProduct() {
    var productDescription = document.querySelector('#productDescription').value;
    var productPrice = document.querySelector('#productPrice').value;
    var productQuantity = document.querySelector('#productQuantity').value;

    var data = {
        descricao: productDescription,
        valor: productPrice,
        quantidade: productQuantity
    };

    fetch('http://localhost:8080/produto', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            response.json()
            if (response.status === 201) {
                location.reload();
            } else {
                alert("Já existe produto com essa descrição");
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}
function fetchProductsByDescriptionLike() {
    var description = document.querySelector("#searchInput")
    if (description.value === "" || description.value === null) {
        fetchProducts();
    } else {
        var listProducts = document.querySelectorAll(".product")
        listProducts.forEach(product => {
            product.style.display = "none"
        })
        var table = $("table");
        fetch(`http://localhost:8080/produto/contains/${description.value}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(res => {
                produtos = res.content;
                produtos.forEach(function (produto) {
                    var tr = $("<tr>");
                    tr.addClass("product")

                    var thId = $("<th>");
                    thId.attr("scope", "row");
                    thId.text(produto.codigo);

                    var tdDescricao = $("<td>");
                    tdDescricao.text(produto.descricao);

                    var tdValor = $("<td>");
                    tdValor.text("R$ " + parseFloat(produto.valor).toFixed(2));

                    var tdQuantidade = $("<td>");
                    tdQuantidade.text(produto.quantidade);

                    var tdBotaoAlterar = $("<td>");
                    var botaoAlterar = $("<button>");
                    botaoAlterar.addClass("btn btn-primary");
                    botaoAlterar.text("Alterar");
                    botaoAlterar.click(() => openEditModal(produto));
                    tdBotaoAlterar.append(botaoAlterar);

                    var tdBotaoRemover = $("<td>");
                    var botaoRemover = $("<button>");
                    botaoRemover.addClass("btn btn-danger");
                    botaoRemover.text("Deletar");
                    botaoRemover.click(() => openDeleteModal(produto));
                    tdBotaoRemover.append(botaoRemover);

                    tr.append(thId);
                    tr.append(tdDescricao);
                    tr.append(tdValor);
                    tr.append(tdQuantidade);
                    tr.append(tdBotaoAlterar);
                    tr.append(tdBotaoRemover);

                    table.append(tr);
                });
            })
            .catch(error => {
                console.error('Erro:', error);
            });

    }
}



fetchProducts();
