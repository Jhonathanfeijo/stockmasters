package StockMaster.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import StockMaster.controller.dto.ProdutoDto;
import StockMaster.domain.model.Produto;
import StockMaster.domain.repository.ProdutoRepository;
import StockMaster.service.exception.ProdutoNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto")
@CrossOrigin("*")
public class ProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;

	@PostMapping
	@Transactional
	public ResponseEntity<ProdutoDto> adicionarProduto(@RequestBody @Valid ProdutoDto produtoDto,
			UriComponentsBuilder builder) {
		Produto produto = produtoRepository.save(produtoDto.toProduto());
		URI uri = builder.path("/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(new ProdutoDto(produto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDto> obterProdutoPorId(@PathVariable("id") Long idProduto) {
		Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new ProdutoNotFoundException());
		return ResponseEntity.ok().body(new ProdutoDto(produto));
	}

	@GetMapping
	public ResponseEntity<Page<ProdutoDto>> obterProdutos(@PageableDefault Pageable paginacao) {
		Page<ProdutoDto> pagina = produtoRepository.findAll(paginacao).map(produto -> new ProdutoDto(produto));
		return ResponseEntity.ok().body(pagina);
	}

	@GetMapping("/contains/{descricao}")
	public ResponseEntity<Page<ProdutoDto>> obterProdutosPorDescricaoLike(@PathVariable("descricao") String descricao) {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Produto> produtosDescricaoLikePage = produtoRepository.findByDescricaoContainingCustomQuery(descricao,
				pageable);
		Page<ProdutoDto> produtoDtoPage = produtosDescricaoLikePage.map(ProdutoDto::new);
		System.out.println(produtoDtoPage.getContent());
		return ResponseEntity.ok(produtoDtoPage);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ProdutoDto> atualizarProduto(@PathVariable("id") Long id,
			@RequestBody @Valid ProdutoDto produtoDto) {
		if (!produtoRepository.existsById(id))
			throw new ProdutoNotFoundException();
		Produto produto = produtoDto.toProduto();
		produto.setId(id);
		produto = produtoRepository.save(produto);
		return ResponseEntity.ok(new ProdutoDto(produto));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<ProdutoDto> deletarProduto(@PathVariable("id") Long id) {
		if (!produtoRepository.existsById(id))
			throw new ProdutoNotFoundException();
		produtoRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
