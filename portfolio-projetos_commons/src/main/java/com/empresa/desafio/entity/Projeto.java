package com.empresa.desafio.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.empresa.desafio.commons.AppConstants;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public abstract class Projeto extends AppBaseEntity {

	private static final BigDecimal ORCAMENTO_BAIXO = new BigDecimal("100000");
	private static final BigDecimal ORCAMENTO_MEDIO_MIN = new BigDecimal("100001");
	private static final BigDecimal ORCAMENTO_ALTO = new BigDecimal("500000");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_PROJETO")
	private Long id;

	@NotNull
	@Size(max = 100)
	@Column(length = 100)
	private String nome;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date previsaoTermino;

	@Temporal(TemporalType.DATE)
	private Date dataRealTermino;

	@Size(max = 2000)
	@Column(length = 2000)
	private String descricao;

	@NotNull
	@Digits(integer = 12, fraction = 2)
	private BigDecimal orcamentoTotal;

	@ManyToOne(targetEntity = MembroEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_PROJETO_GERENTE")
	@NotNull
	@JoinColumn(name = "ID_GERENTE")
	private Membro gerente;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 30)
	private StatusProjeto status;
	//private StatusProjeto status = StatusProjeto.EM_ANALISE;
	
	@OneToMany(targetEntity = AlocacaoEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "projeto")
	@ForeignKey(name = "FK_ALOCACAO_PROJETO")
	@PlcValMultiplicity(min = AppConstants.ALOCACOES_MINIMAS_POR_PROJETO, max = AppConstants.ALOCACOES_MAXIMAS_POR_PROJETO, referenceProperty = "membro", message = "{projeto.alocacao.multiplicidade}")
	@PlcValDuplicity(property = "membro")
	@Valid
	private List<Alocacao> alocacoes = new ArrayList<Alocacao>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getPrevisaoTermino() {
		return previsaoTermino;
	}

	public void setPrevisaoTermino(Date previsaoTermino) {
		this.previsaoTermino = previsaoTermino;
	}

	public Date getDataRealTermino() {
		return dataRealTermino;
	}

	public void setDataRealTermino(Date dataRealTermino) {
		this.dataRealTermino = dataRealTermino;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getOrcamentoTotal() {
		return orcamentoTotal;
	}

	public void setOrcamentoTotal(BigDecimal orcamentoTotal) {
		this.orcamentoTotal = orcamentoTotal;
	}

	public Membro getGerente() {
		return gerente;
	}

	public void setGerente(Membro gerente) {
		this.gerente = gerente;
	}

	public StatusProjeto getStatus() {
		return status;
	}

	public void setStatus(StatusProjeto status) {
		this.status = status;
	}

	public List<Alocacao> getAlocacoes() {
		return alocacoes;
	}

	public void setAlocacoes(List<Alocacao> alocacoes) {
		this.alocacoes = alocacoes;
	}

	/**
	 * Classificação de risco calculada dinamicamente (não cadastrável).
	 */
	@Transient
	public RiscoProjeto getRisco() {
		return calcularRisco();
	}

	/**
	 * Ignora valor informado na tela: o risco não é cadastrável.
	 */
	public void setRisco(RiscoProjeto risco) {
	}

	public RiscoProjeto calcularRisco() {
		if (orcamentoTotal == null || dataInicio == null || previsaoTermino == null) {
			return null;
		}
		int prazoMeses = calcularPrazoEmMeses();
		boolean altoOrcamento = orcamentoTotal.compareTo(ORCAMENTO_ALTO) > 0;
		boolean altoPrazo = prazoMeses > 6;
		if (altoOrcamento || altoPrazo) {
			return RiscoProjeto.ALTO_RISCO;
		}
		boolean medioOrcamento = orcamentoTotal.compareTo(ORCAMENTO_MEDIO_MIN) >= 0
				&& orcamentoTotal.compareTo(ORCAMENTO_ALTO) <= 0;
		boolean medioPrazo = prazoMeses > 3 && prazoMeses <= 6;
		if (medioOrcamento || medioPrazo) {
			return RiscoProjeto.MEDIO_RISCO;
		}
		boolean baixoOrcamento = orcamentoTotal.compareTo(ORCAMENTO_BAIXO) <= 0;
		boolean baixoPrazo = prazoMeses <= 3;
		if (baixoOrcamento && baixoPrazo) {
			return RiscoProjeto.BAIXO_RISCO;
		}
		return RiscoProjeto.MEDIO_RISCO;
	}

	@Transient
	public boolean isExclusaoPermitida() {
		return status != null && status.isExclusaoPermitida();
	}
	
	protected int calcularPrazoEmMeses() {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(dataInicio);
		Calendar previsao = Calendar.getInstance();
		previsao.setTime(previsaoTermino);
		int meses = (previsao.get(Calendar.YEAR) - inicio.get(Calendar.YEAR)) * 12
				+ (previsao.get(Calendar.MONTH) - inicio.get(Calendar.MONTH));
		if (previsao.get(Calendar.DAY_OF_MONTH) < inicio.get(Calendar.DAY_OF_MONTH)) {
			meses--;
		}
		return meses < 0 ? 0 : meses;
	}
}