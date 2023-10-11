package StockMaster.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import StockMaster.model.produto.Produto;
import StockMaster.model.produto.ProdutoRepository;
import jakarta.transaction.Transactional;

@RestControllerAdvice
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@PostMapping
	@Transactional
	public ResponseEntity<Produto> adicionarProduto(@RequestBody Produto produto, UriComponentsBuilder builder) {
		produto = produtoRepository.save(produto);
		URI uri = builder.path("/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(produto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> obterProdutoPorId(@PathVariable("id") Long idProduto) {
		Produto produto = produtoRepository.getReferenceById(idProduto);
		return ResponseEntity.ok(produto);
	}

}
