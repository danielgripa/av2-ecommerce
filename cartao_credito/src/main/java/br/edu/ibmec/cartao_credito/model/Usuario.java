package br.edu.ibmec.cartao_credito.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column
    @NotEmpty(message = "Campo nome é obrigatório")
    private String nome;
    
    @Column
    @NotNull(message = "Campo data de nascimento é obrigatório")
    private LocalDateTime dataNascimento;
    
    @Column
    @CPF(message = "Campo cpf não está em um formato correto")
    private String cpf; 

    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "usuario_id")
    private List<Cartao> cartoes;

    public void associarCartao(Cartao cartao) {
        this.cartoes.add(cartao);
    }
}
