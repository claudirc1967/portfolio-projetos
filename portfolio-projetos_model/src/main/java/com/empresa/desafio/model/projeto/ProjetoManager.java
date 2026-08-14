package com.empresa.desafio.model.projeto;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;

import com.empresa.desafio.commons.AppConstants;
import com.empresa.desafio.entity.Alocacao;
import com.empresa.desafio.entity.ProjetoEntity;
import com.empresa.desafio.entity.StatusProjeto;
import com.empresa.desafio.persistence.jpa.projeto.ProjetoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

/**
 * Classe de Modelo gerada pelo assistente
 */

@SPlcRepository 
@PlcAggregationIoC(clazz=ProjetoEntity.class)
public class ProjetoManager extends PlcBaseRepository {
	@Inject
	private ProjetoDAO projetoDAO;

	@Override
	public Object insert(PlcBaseContextVO context, Object entidade) throws PlcException, Exception {
		ProjetoEntity projeto = (ProjetoEntity) entidade;
		validar(context, projeto, true);
		return super.insert(context, entidade);
	}

	@Override
	public Object update(PlcBaseContextVO context, Object entidade) {
		ProjetoEntity projeto = (ProjetoEntity) entidade;
		validar(context, projeto, false);
		return super.update(context, entidade);
	}
	
	@Override
	public void delete(PlcBaseContextVO context, Object entidade) {
		ProjetoEntity projeto = (ProjetoEntity) entidade;

		StatusProjeto statusBanco = null;
		if (projeto.getId() != null) {
			statusBanco = projetoDAO.buscarStatusPorId(context, projeto.getId());
		} else {
			statusBanco = projeto.getStatus();
		}

		if (statusBanco == null || !statusBanco.isExclusaoPermitida()) {
			throw new PlcException("{projeto.erro.exclusao}");
		}

		super.delete(context, entidade);
	}
	
	private void validar(PlcBaseContextVO context, ProjetoEntity projeto, boolean inclusao) {

		validarStatus(context, projeto, inclusao);
		validarSomenteFuncionario(projeto);

		if (projeto.getStatus() == null || projeto.getStatus().isAtivoParaAlocacao()) {
			validarMaximoProjetosAtivos(context, projeto);
		}
	}

	private void validarStatus(PlcBaseContextVO context, ProjetoEntity projeto, boolean inclusao) {

		if (inclusao) {
			if (projeto.getStatus() == null) {
				projeto.setStatus(StatusProjeto.EM_ANALISE);
				return;
			}
			if (projeto.getStatus() != StatusProjeto.EM_ANALISE) {
				throw new PlcException("{projeto.erro.status.inicial}");
			}
			return;
		}

		if (projeto.getId() == null) {
			return;
		}

		StatusProjeto anterior = projetoDAO.buscarStatusPorId(context, projeto.getId());
		StatusProjeto atual = projeto.getStatus();

		if (anterior == null || atual == null || anterior == atual) {
			return;
		}

		if (!anterior.podeTransitarPara(atual)) {
			throw new PlcException("{projeto.erro.status.transicao}");
		}
	}

	private void validarSomenteFuncionario(ProjetoEntity projeto) {

		if (projeto.getGerente() != null && !projeto.getGerente().isFuncionario()) {
			throw new PlcException("{projeto.erro.somente.funcionario}");
		}

		if (projeto.getAlocacoes() == null) {
			return;
		}

		for (Alocacao alocacao : projeto.getAlocacoes()) {
			if (alocacao == null || alocacao.getMembro() == null) {
				continue;
			}
			if (!alocacao.getMembro().isFuncionario()) {
				throw new PlcException("{projeto.erro.somente.funcionario}");
			}
		}
	}

	private void validarMaximoProjetosAtivos(PlcBaseContextVO context, ProjetoEntity projeto) {

		Set idsMembros = new HashSet();

		if (projeto.getGerente() != null && projeto.getGerente().getId() != null) {
			idsMembros.add(projeto.getGerente().getId());
		}

		if (projeto.getAlocacoes() != null) {
			for (Alocacao alocacao : projeto.getAlocacoes()) {
				if (alocacao == null || alocacao.getMembro() == null || alocacao.getMembro().getId() == null) {
					continue;
				}
				idsMembros.add(alocacao.getMembro().getId());
			}
		}

		Iterator it = idsMembros.iterator();
		while (it.hasNext()) {
			Long idMembro = (Long) it.next();
			Long qtde = projetoDAO.contarProjetosAtivosDoMembro(context, idMembro, projeto.getId());
			if (qtde != null && qtde.longValue() >= AppConstants.PROJETOS_ATIVOS_MAXIMOS_POR_MEMBRO) {
				throw new PlcException("{projeto.erro.alocacao.maximo.ativos}");
			}
		}
	}
}
