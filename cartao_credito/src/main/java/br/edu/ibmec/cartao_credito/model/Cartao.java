package br.edu.ibmec.cartao_credito.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column
    public Boolean ativo;

    @Column
    public double limite;
    
    @Column
    public String numero;
    
    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "cartao_id")
    public List<Transacao> transacoes;
}

