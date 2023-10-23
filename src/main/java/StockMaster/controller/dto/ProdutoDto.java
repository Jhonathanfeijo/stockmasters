package StockMaster.controller.dto;

import java.math.BigDecimal;

import StockMaster.domain.model.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoDto(Long codigo, @NotBlank String descricao, @NotNull BigDecimal valor, @NotNull int quantidade) {

	public ProdutoDto(Produto produto) {
		this(produto.getId(),produto.getDescricao(), produto.getValor(), produto.getQuantidade());
	}

	public Produto toProduto() {
		Produto produto = new Produto();
		produto.setId(codigo);
		produto.setDescricao(descricao);
		produto.setQuantidade(quantidade);
		produto.setValor(valor);
		return produto;
	}

}
