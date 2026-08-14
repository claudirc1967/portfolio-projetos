package com.empresa.desafio.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "PROJETO")
@SequenceGenerator(name = "SE_PROJETO", sequenceName = "SE_PROJETO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="ProjetoEntity.querySel2", query="select obj.id as id, obj.nome as nome, obj.status as status, obj.dataInicio as dataInicio from ProjetoEntity obj order by obj.id asc"),
	@NamedQuery(name = "ProjetoEntity.queryMan", query = "from ProjetoEntity"),
	@NamedQuery(name = "ProjetoEntity.querySel", query = "select obj.id as id, obj.nome as nome, obj.dataInicio as dataInicio, obj.previsaoTermino as previsaoTermino, obj.orcamentoTotal as orcamentoTotal, obj.status as status from ProjetoEntity obj order by obj.nome asc"),
	@NamedQuery(name = "ProjetoEntity.querySelLookup", query = "select id as id, nome as nome from ProjetoEntity where id = ? order by id asc"),
	@NamedQuery(name = "ProjetoEntity.queryEdita", query = "from ProjetoEntity obj where obj.id = ?")
})
public class ProjetoEntity extends Projeto {

	private static final long serialVersionUID = 1L;

	public ProjetoEntity() {
	}

	@Override
	public String toString() {
		return getNome();
	}
}