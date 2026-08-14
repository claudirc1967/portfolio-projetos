package com.empresa.desafio.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.empresa.desafio.commons.AppConstants;

/**
 * Membro obtido pela API REST mockada (nome e atribuição/cargo).
 * Não deve haver CRUD JSF desta entidade.
 */
@MappedSuperclass
public abstract class Membro extends AppBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_MEMBRO")
	private Long id;

	@NotNull
	@Size(max = 100)
	@Column(length = 100)
	private String nome;

	@NotNull
	@Size(max = 40)
	@Column(length = 40)
	private String atribuicao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAtribuicao() {
		return atribuicao;
	}

	public void setAtribuicao(String atribuicao) {
		this.atribuicao = atribuicao;
	}

	/**
	 * Somente membros com atribuição "funcionário" podem ser associados a projetos.
	 */
	@Transient
	public boolean isFuncionario() {
		if (atribuicao == null) {
			return false;
		}
		String valor = atribuicao.trim();
		return AppConstants.ATRIBUICAO_FUNCIONARIO.equalsIgnoreCase(valor)
				|| "funcionário".equalsIgnoreCase(valor);
	}
}