package com.empresa.desafio.entity;

import junit.framework.TestCase;

import com.empresa.desafio.entity.MembroEntity;

public class MembroFuncionarioTest extends TestCase {

	public void testFuncionarioComAcento() {
		MembroEntity m = new MembroEntity();
		m.setAtribuicao("funcionário");
		assertTrue(m.isFuncionario());
	}

	public void testFuncionarioMaiusculo() {
		MembroEntity m = new MembroEntity();
		m.setAtribuicao("FUNCIONÁRIO");
		assertTrue(m.isFuncionario());
	}

	public void testEstagiarioNao() {
		MembroEntity m = new MembroEntity();
		m.setAtribuicao("estagiário");
		assertFalse(m.isFuncionario());
	}

	public void testNuloNao() {
		MembroEntity m = new MembroEntity();
		assertFalse(m.isFuncionario());
	}
}