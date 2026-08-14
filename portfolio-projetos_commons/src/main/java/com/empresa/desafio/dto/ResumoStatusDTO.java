package com.empresa.desafio.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResumoStatusDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;
	private long quantidade;
	private BigDecimal orcamentoTotal;

	public ResumoStatusDTO() {
	}

	public ResumoStatusDTO(String status, long quantidade, BigDecimal orcamentoTotal) {
		this.status = status;
		this.quantidade = quantidade;
		this.orcamentoTotal = orcamentoTotal != null ? orcamentoTotal : BigDecimal.ZERO;
	}

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public long getQuantidade() { return quantidade; }
	public void setQuantidade(long quantidade) { this.quantidade = quantidade; }
	public BigDecimal getOrcamentoTotal() { return orcamentoTotal; }
	public void setOrcamentoTotal(BigDecimal orcamentoTotal) { this.orcamentoTotal = orcamentoTotal; }
}