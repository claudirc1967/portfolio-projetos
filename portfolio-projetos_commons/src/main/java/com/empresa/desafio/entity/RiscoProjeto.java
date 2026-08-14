package com.empresa.desafio.entity;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum RiscoProjeto {
    
	BAIXO_RISCO("{riscoProjeto.BAIXO_RISCO}"),
	MEDIO_RISCO("{riscoProjeto.MEDIO_RISCO}"),
	ALTO_RISCO("{riscoProjeto.ALTO_RISCO}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private RiscoProjeto(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
