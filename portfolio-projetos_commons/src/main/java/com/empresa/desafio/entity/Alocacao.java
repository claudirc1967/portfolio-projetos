package com.empresa.desafio.entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

/**
 * Associação de membro ao projeto (detalhe do mestre Projeto).
 * Mínimo 1 e máximo 10 por projeto.
 */
@MappedSuperclass
public abstract class Alocacao extends AppBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ALOCACAO")
	private Long id;

	@ManyToOne(targetEntity = ProjetoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ALOCACAO_PROJETO")
	@NotNull
	@JoinColumn(name = "ID_PROJETO")
	private Projeto projeto;

	@ManyToOne(targetEntity = MembroEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ALOCACAO_MEMBRO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@JoinColumn(name = "ID_MEMBRO")
	private Membro membro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Membro getMembro() {
		return membro;
	}

	public void setMembro(Membro membro) {
		this.membro = membro;
	}
}