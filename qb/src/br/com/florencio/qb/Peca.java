package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Peca {
	private final List<Celula> celulas;
	private byte orientacaoMemento;
	private byte direcaoMemento;
	private final Forma forma;
	private short grauMemento;
	private byte orientacao;
	private final Color cor;
	private byte direcao;
	private short grau;

	public Peca(Forma forma, Color cor, int x, int y) {
		celulas = forma.criarCelulas(this, cor, x, y);
		this.forma = forma;
		this.cor = cor;
	}

	public void desenhar(Graphics2D g2) {
		for (Celula c : celulas) {
			c.desenhar(g2);
		}
	}

	public void deslocar(byte direcao) {
		for (Celula c : celulas) {
			c.deslocar(direcao);
		}
	}

	public void girar(List<Celula> outras, byte sentido) {
		criarMemento();
		forma.girar(this, sentido);
		boolean desfazer = false;

		externo: for (int i = 0; i < outras.size(); i++) {
			Celula outra = outras.get(i);

			for (Celula c : celulas) {
				if (c.x == outra.x && c.y == outra.y) {
					desfazer = true;
					break externo;
				}
			}
		}

		if (desfazer) {
			restaurarMemento();
		}
	}

	public boolean podeDeslocar(List<Celula> outras, byte direcao) {
		for (int i = 0; i < outras.size(); i++) {
			Celula outra = outras.get(i);

			for (Celula c : celulas) {
				if (!c.podeDeslocar(direcao, outra)) {
					return false;
				}
			}
		}

		return true;
	}

	public List<Celula> getCelulas() {
		return celulas;
	}

	public byte getOrientacao() {
		return orientacao;
	}

	public void setOrientacao(byte orientacao) {
		this.orientacao = orientacao;
	}

	public short getGrau() {
		return grau;
	}

	public void setGrau(short grau) {
		this.grau = grau;
	}

	public Forma getForma() {
		return forma;
	}

	public void criarMemento() {
		orientacaoMemento = orientacao;
		direcaoMemento = direcao;
		grauMemento = grau;
		for (Celula c : celulas) {
			c.criarMemento();
		}
	}

	public void restaurarMemento() {
		orientacao = orientacaoMemento;
		direcao = direcaoMemento;
		grau = grauMemento;
		for (Celula c : celulas) {
			c.restaurarMemento();
		}
	}

	public Color getCor() {
		return cor;
	}
}