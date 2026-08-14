package com.empresa.desafio.entity;

/**
 * Status fixos do projeto, na ordem de transição do desafio.
 * CANCELADO pode ser aplicado a qualquer momento.
 */
public enum StatusProjeto {

	EM_ANALISE("{statusProjeto.EM_ANALISE}"),
	ANALISE_REALIZADA("{statusProjeto.ANALISE_REALIZADA}"),
	ANALISE_APROVADA("{statusProjeto.ANALISE_APROVADA}"),
	INICIADO("{statusProjeto.INICIADO}"),
	PLANEJADO("{statusProjeto.PLANEJADO}"),
	EM_ANDAMENTO("{statusProjeto.EM_ANDAMENTO}"),
	ENCERRADO("{statusProjeto.ENCERRADO}"),
	CANCELADO("{statusProjeto.CANCELADO}");

	private String label;

	private StatusProjeto(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * Projetos iniciado, em andamento ou encerrado não podem ser excluídos.
	 */
	public boolean isExclusaoPermitida() {
		return this != INICIADO && this != EM_ANDAMENTO && this != ENCERRADO;
	}

	/**
	 * Conta como projeto ativo na regra de no máximo 3 alocações simultâneas.
	 */
	public boolean isAtivoParaAlocacao() {
		return this != ENCERRADO && this != CANCELADO;
	}

	public StatusProjeto proximoSequencial() {
		switch (this) {
		case EM_ANALISE:
			return ANALISE_REALIZADA;
		case ANALISE_REALIZADA:
			return ANALISE_APROVADA;
		case ANALISE_APROVADA:
			return INICIADO;
		case INICIADO:
			return PLANEJADO;
		case PLANEJADO:
			return EM_ANDAMENTO;
		case EM_ANDAMENTO:
			return ENCERRADO;
		default:
			return null;
		}
	}

	/**
	 * Transição deve respeitar a sequência; cancelado é exceção.
	 */
	public boolean podeTransitarPara(StatusProjeto destino) {
		if (destino == null || this == destino) {
			return false;
		}
		if (destino == CANCELADO) {
			return this != CANCELADO && this != ENCERRADO;
		}
		return destino == proximoSequencial();
	}
}