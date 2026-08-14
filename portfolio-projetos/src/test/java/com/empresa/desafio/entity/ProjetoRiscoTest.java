package com.empresa.desafio.entity;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.empresa.desafio.entity.ProjetoEntity;
import com.empresa.desafio.entity.RiscoProjeto;

public class ProjetoRiscoTest extends TestCase {

	public void testBaixoRisco() {
		ProjetoEntity p = projeto(bd("80000"), meses(3));
		assertEquals(RiscoProjeto.BAIXO_RISCO, p.getRisco());
	}

	public void testMedioPorOrcamento() {
		ProjetoEntity p = projeto(bd("250000"), meses(2));
		assertEquals(RiscoProjeto.MEDIO_RISCO, p.getRisco());
	}

	public void testAltoPorOrcamento() {
		ProjetoEntity p = projeto(bd("600000"), meses(1));
		assertEquals(RiscoProjeto.ALTO_RISCO, p.getRisco());
	}

	public void testAltoPorPrazo() {
		ProjetoEntity p = projeto(bd("50000"), meses(8));
		assertEquals(RiscoProjeto.ALTO_RISCO, p.getRisco());
	}

	public void testSemDadosRetornaNulo() {
		ProjetoEntity p = new ProjetoEntity();
		assertNull(p.getRisco());
	}

	private ProjetoEntity projeto(BigDecimal orcamento, Date previsao) {
		ProjetoEntity p = new ProjetoEntity();
		p.setOrcamentoTotal(orcamento);
		p.setDataInicio(data(2026, Calendar.AUGUST, 13));
		p.setPrevisaoTermino(previsao);
		return p;
	}

	private Date meses(int qtde) {
		Calendar c = Calendar.getInstance();
		c.set(2026, Calendar.AUGUST, 13, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.MONTH, qtde);
		return c.getTime();
	}

	private Date data(int ano, int mes, int dia) {
		Calendar c = Calendar.getInstance();
		c.set(ano, mes, dia, 0, 0, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	private BigDecimal bd(String v) {
		return new BigDecimal(v);
	}
}