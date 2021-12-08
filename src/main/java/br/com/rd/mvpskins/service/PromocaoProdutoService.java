package br.com.rd.mvpskins.service;

import br.com.rd.mvpskins.model.dto.ProdutoDTO;
import br.com.rd.mvpskins.model.dto.PromocaoDTO;
import br.com.rd.mvpskins.model.dto.PromocaoProdutoCompositeKeyDTO;
import br.com.rd.mvpskins.model.dto.PromocaoProdutoDTO;
import br.com.rd.mvpskins.model.embeddable.PromocaoProdutoCompositeKey;
import br.com.rd.mvpskins.model.entity.Produto;
import br.com.rd.mvpskins.model.entity.Promocao;
import br.com.rd.mvpskins.model.entity.PromocaoProduto;
import br.com.rd.mvpskins.repository.contract.ProdutoRepository;
import br.com.rd.mvpskins.repository.contract.PromocaoProdutoRepository;
import br.com.rd.mvpskins.repository.contract.PromocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromocaoProdutoService {
    @Autowired
    PromocaoProdutoRepository promocaoProdutoRepository;
    @Autowired
    ProdutoService produtoService;
    @Autowired
    PromocaoService promocaoService;
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    PromocaoRepository promocaoRepository;

    private PromocaoProduto dtoToBusiness(PromocaoProdutoDTO dto) {
        PromocaoProdutoCompositeKey id = new PromocaoProdutoCompositeKey();
        Produto a = produtoRepository.getById(dto.getId().getProduto().getId());
        Promocao b = promocaoRepository.getById(dto.getId().getProduto().getId());
        LocalDate d = dto.getId().getDataInicio();

        id.setProduto(a);
        id.setPromocao(b);
        id.setDataInicio(d);

        PromocaoProduto p = new PromocaoProduto();
        p.setId(id);

        return p;
    }


    private PromocaoProdutoDTO businessToDto(PromocaoProduto p) {
    PromocaoProdutoCompositeKeyDTO id = new PromocaoProdutoCompositeKeyDTO();
    ProdutoDTO a = produtoService.getProductById(p.getId().getProduto().getId());
    PromocaoDTO b = promocaoService.searchPromotionById(p.getId().getPromocao().getCodigoPromocao());
    LocalDate d = p.getId().getDataInicio();

    id.setProduto(a);
    id.setPromocao(b);
    id.setDataInicio(d);

        PromocaoProdutoDTO dto = new PromocaoProdutoDTO();
        dto.setId(id);

        return dto;
    }

    private List<PromocaoProdutoDTO> listToDTO(List<PromocaoProduto> listP) {
        List<PromocaoProdutoDTO> listDTO = new ArrayList<>();

        for (PromocaoProduto p : listP) {
            listDTO.add(this.businessToDto(p));
        }
        return listDTO;
    }

    public PromocaoProdutoDTO createPromotion(PromocaoProdutoDTO promocaoProdutoDTO) {
        PromocaoProduto promocaoProduto = dtoToBusiness(promocaoProdutoDTO);
        promocaoProduto = promocaoProdutoRepository.save(promocaoProduto);
        return businessToDto(promocaoProduto);
    }

    public List<PromocaoProdutoDTO> searchAll() {
        List<PromocaoProduto> lista = promocaoProdutoRepository.findAll();
        return listToDTO(lista);
    }

    public PromocaoProdutoDTO searchById(Long idProduto, Long idPromocao) {
        if (produtoRepository.existsById(idProduto) && promocaoRepository.existsById(idPromocao)) {
            PromocaoProdutoCompositeKey id = new PromocaoProdutoCompositeKey();
            id.setProduto(produtoRepository.getById(idProduto));
            id.setPromocao(promocaoRepository.getById(idPromocao));
            if(promocaoProdutoRepository.existsById(id)){
                return businessToDto(promocaoProdutoRepository.getById(id));
            }

        }
        return null;
    }

    public PromocaoProdutoDTO updatePromotion(PromocaoProdutoDTO dto, Long idProduto, Long idPromocao) {
        if (produtoRepository.existsById(idProduto)) {
            PromocaoProdutoCompositeKeyDTO id = new PromocaoProdutoCompositeKeyDTO();
            id.setProduto(produtoService.getProductById(idProduto));
            id.setPromocao(promocaoService.searchPromotionById(idPromocao));
            dto.setId(id);

            PromocaoProduto promocaoProduto = dtoToBusiness(dto);
            Optional<PromocaoProduto> opt = promocaoProdutoRepository.findById(promocaoProduto.getId());

            if (opt.isPresent()) {
                PromocaoProduto update = opt.get();

                promocaoProdutoRepository.save(update);
                return businessToDto(update);
            }
        }
        return null;
    }

    public void delete (Long idProduto, Long idPromocao){
        PromocaoProdutoCompositeKey id = new PromocaoProdutoCompositeKey();
        id.setProduto(produtoRepository.getById(idProduto));
        id.setPromocao(promocaoRepository.getById(idPromocao));
        if(promocaoProdutoRepository.existsById(id)){
            promocaoProdutoRepository.deleteById(id);
        }
    }
}










