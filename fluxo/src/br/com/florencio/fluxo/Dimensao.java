package br.com.florencio.fluxo;

public class Dimensao {
	public static final byte TAMANHO_ICONE = 10;
	public static final byte METADE_ICONE = TAMANHO_ICONE / 2;
	public static final byte ALTURA_PADRAO = 20;
	public static final byte MARGEM_ICONE = (ALTURA_PADRAO - TAMANHO_ICONE) / 2;
	public static final byte MARGEM_DIREITA = 40;
	public static final byte MARGEM_INFERIOR = 3;
	public static int larguraTotal;
	public static int alturaTotal;
	int largura;
	int altura;

	public Dimensao() {
	}

	public Dimensao(int largura, int altura) {
		this.largura = largura;
		this.altura = altura;
	}

	public int getLargura() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}
}