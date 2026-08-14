package com.empresa.desafio.entity;

import junit.framework.TestCase;

import com.empresa.desafio.entity.StatusProjeto;

public class StatusProjetoTest extends TestCase {

	public void testSequenciaValida() {
		assertTrue(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.ANALISE_REALIZADA));
		assertTrue(StatusProjeto.ANALISE_REALIZADA.podeTransitarPara(StatusProjeto.ANALISE_APROVADA));
		assertTrue(StatusProjeto.ANALISE_APROVADA.podeTransitarPara(StatusProjeto.INICIADO));
		assertTrue(StatusProjeto.INICIADO.podeTransitarPara(StatusProjeto.PLANEJADO));
		assertTrue(StatusProjeto.PLANEJADO.podeTransitarPara(StatusProjeto.EM_ANDAMENTO));
		assertTrue(StatusProjeto.EM_ANDAMENTO.podeTransitarPara(StatusProjeto.ENCERRADO));
	}

	public void testNaoPulaStatus() {
		assertFalse(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.INICIADO));
		assertFalse(StatusProjeto.ANALISE_APROVADA.podeTransitarPara(StatusProjeto.PLANEJADO));
	}

	public void testMesmoStatusNaoTransita() {
		assertFalse(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.EM_ANALISE));
	}

	public void testCanceladoAPartirDeQualquerUmExcetoEncerrado() {
		assertTrue(StatusProjeto.EM_ANALISE.podeTransitarPara(StatusProjeto.CANCELADO));
		assertTrue(StatusProjeto.INICIADO.podeTransitarPara(StatusProjeto.CANCELADO));
		assertTrue(StatusProjeto.EM_ANDAMENTO.podeTransitarPara(StatusProjeto.CANCELADO));
		assertFalse(StatusProjeto.ENCERRADO.podeTransitarPara(StatusProjeto.CANCELADO));
		assertFalse(StatusProjeto.CANCELADO.podeTransitarPara(StatusProjeto.CANCELADO));
	}

	public void testEncerradoNaoSai() {
		assertFalse(StatusProjeto.ENCERRADO.podeTransitarPara(StatusProjeto.EM_ANDAMENTO));
		assertFalse(StatusProjeto.ENCERRADO.podeTransitarPara(StatusProjeto.EM_ANALISE));
	}

	public void testExclusaoBloqueada() {
		assertFalse(StatusProjeto.INICIADO.isExclusaoPermitida());
		assertFalse(StatusProjeto.EM_ANDAMENTO.isExclusaoPermitida());
		assertFalse(StatusProjeto.ENCERRADO.isExclusaoPermitida());
	}

	public void testExclusaoPermitida() {
		assertTrue(StatusProjeto.EM_ANALISE.isExclusaoPermitida());
		assertTrue(StatusProjeto.CANCELADO.isExclusaoPermitida());
	}

	public void testAtivoParaAlocacao() {
		assertTrue(StatusProjeto.INICIADO.isAtivoParaAlocacao());
		assertFalse(StatusProjeto.ENCERRADO.isAtivoParaAlocacao());
		assertFalse(StatusProjeto.CANCELADO.isAtivoParaAlocacao());
	}
}