package br.com.florencio.qb;

public abstract class Acao {
	final boolean especial;
	final int grupo;

	public Acao(int grupo) {
		this(grupo, false);
	}

	public Acao(int grupo, boolean especial) {
		this.especial = especial;
		this.grupo = grupo;
	}

	public abstract void executar();
}