package com.empresa.desafio.controller.rest;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.empresa.desafio.commons.AppBaseContextVO;
import com.empresa.desafio.dto.RelatorioProjetosDTO;
import com.empresa.desafio.dto.ResumoStatusDTO;
import com.empresa.desafio.entity.StatusProjeto;
import com.empresa.desafio.persistence.jpa.projeto.ProjetoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

@Path("/relatorios/projetos")
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioRest {

	@Context
	private HttpServletRequest request;

	@GET
	public Response gerar() {
		PlcBaseContextVO context = getContext();
		ProjetoDAO dao = getDao();
		
		RelatorioProjetosDTO dto = new RelatorioProjetosDTO();
		dto.setPorStatus(montarPorStatus(dao.resumirPorStatus(context)));
		dto.setMediaDuracaoDiasEncerrados(calcularMediaDias(dao.datasEncerrados(context)));
		Long unicos = dao.contarMembrosUnicosAlocados(context);
		dto.setMembrosUnicosAlocados(unicos == null ? 0L : unicos.longValue());

		return Response.ok(toJson(dto)).type(MediaType.APPLICATION_JSON).build();
	}

	private List montarPorStatus(List linhas) {
		Map mapa = new HashMap();
		if (linhas != null) {
			for (int i = 0; i < linhas.size(); i++) {
				Object[] linha = (Object[]) linhas.get(i);
				StatusProjeto status = (StatusProjeto) linha[0];
				long qtde = linha[1] == null ? 0L : ((Number) linha[1]).longValue();
				BigDecimal orcamento = (BigDecimal) linha[2];
				if (status != null) {
					mapa.put(status, new ResumoStatusDTO(status.name(), qtde, orcamento));
				}
			}
		}
		List resultado = new java.util.ArrayList();
		StatusProjeto[] todos = StatusProjeto.values();
		for (int i = 0; i < todos.length; i++) {
			ResumoStatusDTO item = (ResumoStatusDTO) mapa.get(todos[i]);
			if (item == null) {
				item = new ResumoStatusDTO(todos[i].name(), 0L, BigDecimal.ZERO);
			}
			resultado.add(item);
		}
		return resultado;
	}

	private Double calcularMediaDias(List pares) {
		if (pares == null || pares.isEmpty()) {
			return null;
		}
		long soma = 0L;
		int qtde = 0;
		for (int i = 0; i < pares.size(); i++) {
			Object[] par = (Object[]) pares.get(i);
			Date inicio = (Date) par[0];
			Date fim = (Date) par[1];
			if (inicio == null || fim == null) {
				continue;
			}
			soma += diferencaEmDias(inicio, fim);
			qtde++;
		}
		if (qtde == 0) {
			return null;
		}
		return new Double((double) soma / (double) qtde);
	}

	private long diferencaEmDias(Date inicio, Date fim) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(inicio);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(fim);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		return (c2.getTimeInMillis() - c1.getTimeInMillis()) / (24L * 60L * 60L * 1000L);
	}

	private String toJson(RelatorioProjetosDTO dto) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"porStatus\":[");
		List lista = dto.getPorStatus();
		for (int i = 0; i < lista.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			ResumoStatusDTO item = (ResumoStatusDTO) lista.get(i);
			sb.append("{");
			sb.append("\"status\":\"").append(item.getStatus()).append("\",");
			sb.append("\"quantidade\":").append(item.getQuantidade()).append(",");
			sb.append("\"orcamentoTotal\":").append(
					item.getOrcamentoTotal() == null ? "0" : item.getOrcamentoTotal().toPlainString());
			sb.append("}");
		}
		sb.append("],");
		sb.append("\"mediaDuracaoDiasEncerrados\":");
		if (dto.getMediaDuracaoDiasEncerrados() == null) {
			sb.append("null");
		} else {
			sb.append(dto.getMediaDuracaoDiasEncerrados());
		}
		sb.append(",\"membrosUnicosAlocados\":").append(dto.getMembrosUnicosAlocados());
		sb.append("}");
		return sb.toString();
	}

	private ProjetoDAO getDao() {
		return PlcCDIUtil.getInstance().getInstanceByType(ProjetoDAO.class);		
	}

	private PlcBaseContextVO getContext() {
		PlcBaseContextVO context = (PlcBaseContextVO) request.getAttribute(PlcConstants.CONTEXT);
		if (context == null) {
			context = new AppBaseContextVO();
		}
		return context;
	}
}