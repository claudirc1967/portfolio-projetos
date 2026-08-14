package com.empresa.desafio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelatorioProjetosDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List porStatus = new ArrayList();
	private Double mediaDuracaoDiasEncerrados;
	private long membrosUnicosAlocados;

	public List getPorStatus() { return porStatus; }
	public void setPorStatus(List porStatus) { this.porStatus = porStatus; }
	public Double getMediaDuracaoDiasEncerrados() { return mediaDuracaoDiasEncerrados; }
	public void setMediaDuracaoDiasEncerrados(Double mediaDuracaoDiasEncerrados) {
		this.mediaDuracaoDiasEncerrados = mediaDuracaoDiasEncerrados;
	}
	public long getMembrosUnicosAlocados() { return membrosUnicosAlocados; }
	public void setMembrosUnicosAlocados(long membrosUnicosAlocados) {
		this.membrosUnicosAlocados = membrosUnicosAlocados;
	}
}