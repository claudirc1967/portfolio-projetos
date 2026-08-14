package com.empresa.desafio.dto;

import java.io.Serializable;

public class MembroDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String atribuicao;

	public MembroDTO() {
	}

	public MembroDTO(Long id, String nome, String atribuicao) {
		this.id = id;
		this.nome = nome;
		this.atribuicao = atribuicao;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getAtribuicao() { return atribuicao; }
	public void setAtribuicao(String atribuicao) { this.atribuicao = atribuicao; }
}