/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/


@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="portfolio-projetos",acronym="portfolio-projetos",version=1,release=0),
	classesDiscreteDomain={com.empresa.desafio.entity.StatusProjeto.class,StatusProjeto.class, RiscoProjeto.class},
	classesLookup={com.empresa.desafio.entity.MembroEntity.class,MembroEntity.class}
)


package com.powerlogic.jcompany.config.app;

import com.empresa.desafio.entity.MembroEntity;
import com.empresa.desafio.entity.RiscoProjeto;
import com.empresa.desafio.entity.StatusProjeto;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
