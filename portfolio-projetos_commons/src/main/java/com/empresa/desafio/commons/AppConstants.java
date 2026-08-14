/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.empresa.desafio.commons;

import com.powerlogic.jcompany.commons.PlcConstants;

/**
 * Constantes específicas da aplicação de portfólio de projetos.
 */
public interface AppConstants extends PlcConstants {

	String ATRIBUICAO_FUNCIONARIO = "funcionário";

	int ALOCACOES_MINIMAS_POR_PROJETO = 1;
	int ALOCACOES_MAXIMAS_POR_PROJETO = 10;
	int PROJETOS_ATIVOS_MAXIMOS_POR_MEMBRO = 3;
}