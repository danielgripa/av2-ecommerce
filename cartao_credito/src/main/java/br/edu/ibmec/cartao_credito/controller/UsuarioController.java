package br.edu.ibmec.cartao_credito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.cartao_credito.model.Cartao;
import br.edu.ibmec.cartao_credito.model.Usuario;
import br.edu.ibmec.cartao_credito.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario usuario) throws Exception { 
        usuarioService.criarUsuario(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PostMapping("{id}")
    public ResponseEntity<Usuario> associarCartao(@PathVariable("id") int id, @RequestBody Cartao cartao) throws Exception { 
        usuarioService.associarCartao(cartao, id);
        Usuario usuario = usuarioService.buscaUsuario(id);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }
   
}
