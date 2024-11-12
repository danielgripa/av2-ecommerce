package br.edu.ibmec.cartao_credito.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.cartao_credito.exception.UsuarioException;
import br.edu.ibmec.cartao_credito.model.Cartao;
import br.edu.ibmec.cartao_credito.model.Transacao;
import br.edu.ibmec.cartao_credito.model.Usuario;
import br.edu.ibmec.cartao_credito.request.TransacaoRequest;
import br.edu.ibmec.cartao_credito.request.TransacaoResponse;
import br.edu.ibmec.cartao_credito.service.TransacaoService;
import br.edu.ibmec.cartao_credito.service.UsuarioService;

@RestController
@RequestMapping("/autorizar")
public class TransacaoController {
    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<TransacaoResponse> transacionar(@RequestBody TransacaoRequest request) throws Exception {

        // Buscando o usuario
        Usuario user = this.usuarioService.buscaUsuario(request.getIdUsuario());

        if (user == null)
            throw new UsuarioException("Usuario não encontrado, verifique o identificador");

        Optional<Cartao> optCartao = user.getCartoes()
                .stream()
                .filter(x -> x.numero.equals(request.getNumeroCartao()))
                .findFirst();
        // Não achei o cartao de credito do usuário
        if (optCartao.isPresent() == false)
            throw new UsuarioException(
                    "Não encontrei o cartão associado a esse usuário com o numero " + request.getNumeroCartao());

        // Cartao do usuário
        Cartao cartao = optCartao.get();
        TransacaoResponse response = new TransacaoResponse();

        try {
            Transacao transacao = this.transacaoService.autorizacaoTransacao(cartao, request.getValor(),
                    request.getComerciante());
            // Objeto de resposta do usuario
            response.setDataTransacao(transacao.getDataTransacao());
            response.setStatus("APROVADO");
            response.setValor(transacao.getValor());
            response.setCodigoAutorizacao(UUID.randomUUID());

        } catch (Exception e) {
            response.setStatus("REPROVADO:" + e.getMessage());
            response.setDataTransacao(LocalDateTime.now());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<Transacao>> extratoCartao(@PathVariable("id") int id, String numeroCartao)
            throws Exception {
        // Buscando o usuario
        Usuario user = this.usuarioService.buscaUsuario(id);

        if (user == null)
            throw new UsuarioException("Usuario não encontrado, verifique o identificador");

        Optional<Cartao> optCartao = user.getCartoes()
                .stream()
                .filter(x -> x.numero.equals(numeroCartao))
                .findFirst();

        // Não achei o cartao de credito do usuário
        if (optCartao.isPresent() == false)
            throw new UsuarioException(
                    "Não encontrei o cartão associado a esse usuário com o numero " + numeroCartao);
        
        // Pega as transações corrente do cartão de credito pelo ano e mes
        List<Transacao> transacaos = optCartao.get()
                                              .getTransacoes()
                                              .stream()
                                              .filter(x -> x.dataTransacao.getMonth() == LocalDateTime.now().getMonth() && x.dataTransacao.getYear() == LocalDateTime.now().getYear())
                                              .toList();

        return new ResponseEntity<>(transacaos, HttpStatus.OK);                    
    }

}
