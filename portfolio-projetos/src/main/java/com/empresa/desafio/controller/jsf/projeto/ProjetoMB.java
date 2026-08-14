package com.empresa.desafio.controller.jsf.projeto;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.empresa.desafio.entity.ProjetoEntity;
import com.empresa.desafio.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;


import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
		entity = com.empresa.desafio.entity.ProjetoEntity.class

		,details = { 		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.empresa.desafio.entity.AlocacaoEntity.class,
								collectionName = "alocacoes", numNew = 4,onDemand = false)
			

		}
	)
	



@PlcConfigForm (
	selection = @PlcConfigSelection(apiQuerySel = "querySel2"),
	formPattern=FormPattern.Mdt,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/projeto")
	
	
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("projeto")
@PlcHandleException
public class ProjetoMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("projeto")
	public ProjetoEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new ProjetoEntity();
              this.newEntity();
        }
        return (ProjetoEntity)this.entityPlc;     	
	}
		
}
