package br.com.turma_java;

import java.io.Serializable;

public class Mensagem implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int MONTAR_TELA = 1;
	public static final int REINICIAR = 2;
	public static final int CONECTADO = 3;
	public static final int EXCLUIDO = 4;
	public static final int VENCEDOR = 5;
	public static final int ABATIDO = 6;
	public static final int PROXIMO = 7;
	public static final int CLICK = 8;
	public static final int ALVO = 9;
	public static final int INI = 10;

	private String clienteAlvo;
	private int totalClientes;
	private String metaInfo;
	private String cliente;
	private int celulaAlvo;
	private boolean borda;
	private int celulas;
	private int tipo;

	public Mensagem() {
	}

	public Mensagem(int tipo) {
		this.tipo = tipo;
	}

	public Mensagem(int tipo, String cliente) {
		this.cliente = cliente;
		this.tipo = tipo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public boolean isMontarTela() {
		return tipo == MONTAR_TELA;
	}

	public boolean isReiniciar() {
		return tipo == REINICIAR;
	}

	public boolean isConectado() {
		return tipo == CONECTADO;
	}

	public boolean isExcluido() {
		return tipo == EXCLUIDO;
	}

	public boolean isVencedor() {
		return tipo == VENCEDOR;
	}

	public boolean isAbatido() {
		return tipo == ABATIDO;
	}

	public boolean isProximo() {
		return tipo == PROXIMO;
	}

	public boolean isClick() {
		return tipo == CLICK;
	}

	public boolean isAlvo() {
		return tipo == ALVO;
	}

	public boolean isIni() {
		return tipo == INI;
	}

	public int getCelulas() {
		return celulas;
	}

	public void setCelulas(int celulas) {
		this.celulas = celulas;
	}

	public int getCelulaAlvo() {
		return celulaAlvo;
	}

	public void setCelulaAlvo(int celulaAlvo) {
		this.celulaAlvo = celulaAlvo;
	}

	public int getTotalClientes() {
		return totalClientes;
	}

	public void setTotalClientes(int totalClientes) {
		this.totalClientes = totalClientes;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getClienteAlvo() {
		return clienteAlvo;
	}

	public void setClienteAlvo(String clienteAlvo) {
		this.clienteAlvo = clienteAlvo;
	}

	public boolean isBorda() {
		return borda;
	}

	public void setBorda(boolean borda) {
		this.borda = borda;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("totalClientes: " + totalClientes);
		sb.append(" cliente: " + cliente);
		sb.append(" clienteAlvo: " + clienteAlvo);
		sb.append(" celulaAlvo: " + celulaAlvo);
		sb.append(" metaInfo: " + metaInfo);
		sb.append(" celulas: " + celulas);
		sb.append(" tipo: " + descTipo());

		return sb.toString();
	}

	private String descTipo() {
		switch (tipo) {
		case MONTAR_TELA:
			return "MONTAR_TELA";
		case REINICIAR:
			return "REINICIAR";
		case CONECTADO:
			return "CONECTADO";
		case EXCLUIDO:
			return "EXCLUIDO";
		case VENCEDOR:
			return "VENCEDOR";
		case ABATIDO:
			return "ABATIDO";
		case PROXIMO:
			return "PROXIMO";
		case CLICK:
			return "CLICK";
		case ALVO:
			return "ALVO";
		case INI:
			return "INI";
		}

		return null;
	}
}