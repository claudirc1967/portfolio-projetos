package com.empresa.desafio.entity;


import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name="MEMBRO")
@SequenceGenerator(name="SE_MEMBRO", sequenceName="SE_MEMBRO")
@Access(AccessType.FIELD)


@NamedQueries({
	@NamedQuery(name="MembroEntity.querySelLookup", query="select id as id, nome as nome, atribuicao as atribuicao from MembroEntity where id = ? order by id asc")
})
public class MembroEntity extends Membro {

	private static final long serialVersionUID = 1L;
 	
    /*
     * Construtor padrao
     */
    public MembroEntity() {
    }
	@Override
	public String toString() {
		return getNome();
	}

}
