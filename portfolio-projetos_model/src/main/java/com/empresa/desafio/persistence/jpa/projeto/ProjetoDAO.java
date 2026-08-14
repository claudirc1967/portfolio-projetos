package com.empresa.desafio.persistence.jpa.projeto;

import java.util.Date;
import java.util.List;

import com.empresa.desafio.entity.ProjetoEntity;
import com.empresa.desafio.entity.StatusProjeto;
import com.empresa.desafio.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(ProjetoEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class ProjetoDAO extends AppJpaDAO  {

	@PlcQuery("querySel2")
	public native List<ProjetoEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc,
			@PlcQueryLineAmount Integer numeroLinhasPlc,

			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="lower(obj.nome) like concat('%', lower(:nome), '%')") String nome,
			@PlcQueryParameter(name="status", expression="obj.status = :status") StatusProjeto status,
			@PlcQueryParameter(name="dataInicio", expression="obj.dataInicio >= :dataInicio") Date dataInicio
	);

	@PlcQuery("querySel2")
	public native Long findCount(
			PlcBaseContextVO context,

			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="lower(obj.nome) like concat('%', lower(:nome), '%')") String nome,
			@PlcQueryParameter(name="status", expression="obj.status = :status") StatusProjeto status,
			@PlcQueryParameter(name="dataInicio", expression="obj.dataInicio >= :dataInicio") Date dataInicio
	);
	
	public Long contarProjetosAtivosDoMembro(PlcBaseContextVO context, Long idMembro, Long idProjetoAtual) {
		String jpql = "select count(distinct a.projeto.id) from AlocacaoEntity a "
				+ "where a.membro.id = :idMembro "
				+ "and a.projeto.status not in (:encerrado, :cancelado)";
		if (idProjetoAtual != null) {
			jpql = jpql + " and a.projeto.id <> :idProjetoAtual";
		}
		javax.persistence.Query q = getEntityManager(context).createQuery(jpql);
		q.setParameter("idMembro", idMembro);
		q.setParameter("encerrado", StatusProjeto.ENCERRADO);
		q.setParameter("cancelado", StatusProjeto.CANCELADO);
		if (idProjetoAtual != null) {
			q.setParameter("idProjetoAtual", idProjetoAtual);
		}
		return (Long) q.getSingleResult();
	}
	
	public StatusProjeto buscarStatusPorId(PlcBaseContextVO context, Long id) {
		javax.persistence.Query q = getEntityManager(context)
				.createQuery("select p.status from ProjetoEntity p where p.id = :id");
		q.setParameter("id", id);
		q.setFlushMode(javax.persistence.FlushModeType.COMMIT);
		List resultado = q.getResultList();
		if (resultado == null || resultado.isEmpty()) {
			return null;
		}
		return (StatusProjeto) resultado.get(0);
	}
	
	public List resumirPorStatus(PlcBaseContextVO context) {
		javax.persistence.Query q = getEntityManager(context).createQuery(
				"select p.status, count(p.id), sum(p.orcamentoTotal) "
				+ "from ProjetoEntity p group by p.status");
		return q.getResultList();
	}

	public List datasEncerrados(PlcBaseContextVO context) {
		javax.persistence.Query q = getEntityManager(context).createQuery(
				"select p.dataInicio, p.dataRealTermino from ProjetoEntity p "
				+ "where p.status = :status");
		q.setParameter("status", StatusProjeto.ENCERRADO);
		return q.getResultList();
	}

	public Long contarMembrosUnicosAlocados(PlcBaseContextVO context) {
		javax.persistence.Query q = getEntityManager(context).createQuery(
				"select count(distinct a.membro.id) from AlocacaoEntity a");
		return (Long) q.getSingleResult();
	}
	
}
