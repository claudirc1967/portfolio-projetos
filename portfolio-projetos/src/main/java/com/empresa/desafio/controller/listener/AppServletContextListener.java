/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.empresa.desafio.controller.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.empresa.desafio.entity.MembroEntity;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.listener.PlcServletContextListener;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;

/**
 * Classe destinada a programações em tempo de inicialização  da aplicação
 */
public class AppServletContextListener extends PlcServletContextListener {
	
	protected static final Logger log = Logger.getLogger(AppServletContextListener.class.getCanonicalName());

	@Override
	public void cdAoEncerrarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.info( "Encerrando a Aplicacao");

	}

	@Override
	public void ciAoInicializarAplicacao(ServletContextEvent event)
			throws PlcException {
		log.info( "Tratamento da Aplicacao: Inicializando a Aplicacao");
	}
	
	@Override
	protected void aposCarregaClasseLookup(Class[] classesLookup, String[] classesLookupOrderby) {
		PlcClassLookupUtil lookupUtil = this.classeLookupUtil;
		if (lookupUtil == null) {
			lookupUtil = PlcCDIUtil.getInstance().getInstanceByType(
					PlcClassLookupUtil.class, QPlcDefaultLiteral.INSTANCE);
		}

		List origem = lookupUtil.getListFromCache(MembroEntity.class);
		List funcionarios = new ArrayList();
		if (origem != null) {
			for (Object item : origem) {
				MembroEntity membro = (MembroEntity) item;
				if (membro.isFuncionario()) {
					funcionarios.add(membro);
				}
			}
		}
		lookupUtil.storeClassLookup(MembroEntity.class, funcionarios);
	}
}
