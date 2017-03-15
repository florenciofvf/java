package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Peca {
	private final List<Celula> celulas;
	private final Forma forma;
	private byte estado;

	public Peca(Forma forma, Color cor, int x, int y) {
		celulas = forma.criarCelulas(this, cor, x, y);
		this.forma = forma;
	}

	public void desenhar(Graphics2D g2) {
		for (Celula c : celulas) {
			c.desenhar(g2);
		}
	}

	public synchronized void deslocar(byte direcao) {
		for (Celula c : celulas) {
			c.deslocar(direcao);
		}
	}

	public synchronized void girar(List<Celula> outras, byte sentido) {
		boolean desfazer = false;

		for (Celula c : celulas) {
			c.criarMemento();
		}

		forma.girar(this, sentido);

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
			for (Celula c : celulas) {
				c.restaurarMemento();
			}
		}
	}

	public synchronized boolean podeDeslocar(List<Celula> outras, byte direcao) {
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

	public byte getEstado() {
		return estado;
	}

	public void setEstado(byte estado) {
		this.estado = estado;
	}
}