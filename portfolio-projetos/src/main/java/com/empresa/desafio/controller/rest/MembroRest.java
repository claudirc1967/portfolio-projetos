package com.empresa.desafio.controller.rest;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.empresa.desafio.commons.AppBaseContextVO;
import com.empresa.desafio.dto.MembroDTO;
import com.empresa.desafio.entity.MembroEntity;
import com.empresa.desafio.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;

@Path("/membros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MembroRest {

	@Context
	private HttpServletRequest request;

	@POST
	public Response criar(String json) {
		MembroDTO dto = fromJson(json);
		if (dto == null || isBlank(dto.getNome()) || isBlank(dto.getAtribuicao())) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("nome e atribuicao sao obrigatorios")
					.build();
		}

		MembroEntity entidade = new MembroEntity();
		entidade.setNome(dto.getNome().trim());
		entidade.setAtribuicao(dto.getAtribuicao().trim());

		PlcBaseContextVO context = getContext();
		MembroEntity gravado = (MembroEntity) getFacade().saveObject(context, entidade);
		atualizarCacheLookup(context);

		return Response.status(Response.Status.CREATED)
				.type(MediaType.APPLICATION_JSON)
				.entity(toJson(toDTO(gravado)))
				.build();
	}

	@GET
	public Response listar(@QueryParam("atribuicao") String atribuicao) {
		PlcBaseContextVO context = getContext();
		MembroEntity filtro = new MembroEntity();
		if (!isBlank(atribuicao)) {
			filtro.setAtribuicao(atribuicao.trim());
		}

		Collection encontrados = getFacade().findList(context, filtro, "nome asc", 0, 200);
		StringBuffer sb = new StringBuffer("[");
		boolean primeiro = true;
		if (encontrados != null) {
			for (Object item : encontrados) {
				if (!primeiro) {
					sb.append(",");
				}
				sb.append(toJson(toDTO((MembroEntity) item)));
	 			primeiro = false;
			}
		}
		sb.append("]");
		return Response.ok(sb.toString()).type(MediaType.APPLICATION_JSON).build();
	}

	private MembroDTO fromJson(String json) {
		if (isBlank(json)) {
			return null;
		}
		MembroDTO dto = new MembroDTO();
		dto.setNome(campoJson(json, "nome"));
		dto.setAtribuicao(campoJson(json, "atribuicao"));
		return dto;
	}

	private String toJson(MembroDTO dto) {
		if (dto == null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"id\":").append(dto.getId() == null ? "null" : dto.getId().toString());
		sb.append(",\"nome\":").append(aspas(dto.getNome()));
		sb.append(",\"atribuicao\":").append(aspas(dto.getAtribuicao()));
		sb.append("}");
		return sb.toString();
	}

	private String campoJson(String json, String campo) {
		String chave = "\"" + campo + "\"";
		int i = json.indexOf(chave);
		if (i < 0) {
			return null;
		}
		int doisPontos = json.indexOf(':', i + chave.length());
		int ini = json.indexOf('"', doisPontos + 1);
		if (ini < 0) {
			return null;
		}
		int fim = json.indexOf('"', ini + 1);
		if (fim < 0) {
			return null;
		}
		return json.substring(ini + 1, fim);
	}

	private String aspas(String valor) {
		if (valor == null) {
			return "null";
		}
		return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}
	
	private IAppFacade getFacade() {
		return PlcCDIUtil.getInstance().getInstanceByType(
				IAppFacade.class, QPlcDefaultLiteral.INSTANCE);
	}

	private PlcBaseContextVO getContext() {
		PlcBaseContextVO context = (PlcBaseContextVO) request.getAttribute(PlcConstants.CONTEXT);
		if (context == null) {
			context = new AppBaseContextVO();
		}
		return context;
	}

	private void atualizarCacheLookup(PlcBaseContextVO context) {
		Collection todos = getFacade().findList(context, new MembroEntity(), "nome asc", 0, 200);
		PlcClassLookupUtil lookupUtil = PlcCDIUtil.getInstance().getInstanceByType(
				PlcClassLookupUtil.class, QPlcDefaultLiteral.INSTANCE);
		lookupUtil.storeClassLookup(MembroEntity.class, todos);
	}

	private MembroDTO toDTO(MembroEntity entidade) {
		if (entidade == null) {
			return null;
		}
		return new MembroDTO(entidade.getId(), entidade.getNome(), entidade.getAtribuicao());
	}

	private boolean isBlank(String valor) {
		return valor == null || valor.trim().length() == 0;
	}
}