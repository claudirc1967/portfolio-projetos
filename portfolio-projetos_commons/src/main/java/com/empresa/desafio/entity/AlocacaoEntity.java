package com.empresa.desafio.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ALOCACAO")
@SequenceGenerator(name = "SE_ALOCACAO", sequenceName = "SE_ALOCACAO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = "AlocacaoEntity.querySelLookup", query = "select id as id from AlocacaoEntity where id = ? order by id asc")
})
public class AlocacaoEntity extends Alocacao {

	private static final long serialVersionUID = 1L;

	public AlocacaoEntity() {
	}

	@Override
	public String toString() {
		return getMembro() != null ? getMembro().toString() : "";
	}

	@Transient
	private transient String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}
}