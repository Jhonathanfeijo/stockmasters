package StockMaster.service.exception;

public class ProdutoNotFoundException extends RuntimeException {

	public ProdutoNotFoundException() {
		super("Produto não encontrado");
	}
}
