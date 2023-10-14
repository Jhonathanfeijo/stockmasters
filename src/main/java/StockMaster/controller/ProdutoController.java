package StockMaster.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import StockMaster.model.produto.Produto;
import StockMaster.model.produto.ProdutoRepository;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@PostMapping
	@Transactional
	public ResponseEntity adicionarProduto(@RequestBody Produto produto, UriComponentsBuilder builder) {
		produto = produtoRepository.save(produto);
		URI uri = builder.path("/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(null).body(produto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> obterProdutoPorId(@PathVariable("id") Long idProduto) {
		Produto produto = produtoRepository.findById(idProduto).get();
		return ResponseEntity.ok().body(produto);
	}

	@GetMapping
	public ResponseEntity<Page<Produto>> obterProdutos(@PageableDefault Pageable paginacao) {
		Page<Produto> pagina = produtoRepository.findAll(paginacao).map(produto -> {
			return produto;
		});
		return ResponseEntity.ok().body(pagina);
	}

}
