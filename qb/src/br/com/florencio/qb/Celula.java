package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics2D;

public class Celula {
	private final Color cor;
	private boolean limite;
	private int xMemento;
	private int yMemento;
	int x;
	int y;

	public Celula(Color cor, int x, int y) {
		this(cor, x, y, false);
	}

	public Celula(Color cor, int x, int y, boolean limite) {
		this.limite = limite;
		this.cor = cor;
		this.x = x;
		this.y = y;
	}

	public Celula clonar() {
		return new Celula(cor, x, y, limite);
	}

	public void desenhar(Graphics2D g2) {
		g2.setColor(cor);

		if (limite) {
			g2.fillRect(x, y, Constantes.LADO_QUADRADO, Constantes.LADO_QUADRADO);

		} else {
			g2.drawRect(x, y, Constantes.LADO_QUADRADO, Constantes.LADO_QUADRADO);

			g2.fillRect(x + Constantes.BORDA_QUADRADO, y + Constantes.BORDA_QUADRADO,
					Constantes.LADO_QUADRADO - Constantes.BORDA_QUADRADO * 2,
					Constantes.LADO_QUADRADO - Constantes.BORDA_QUADRADO * 2);
		}
	}

	public void deslocar(byte direcao) {
		if (Constantes.DIRECAO_NORTE == direcao) {
			y -= Constantes.LADO_QUADRADO;

		} else if (Constantes.DIRECAO_SUL == direcao) {
			y += Constantes.LADO_QUADRADO;

		} else if (Constantes.DIRECAO_LESTE == direcao) {
			x += Constantes.LADO_QUADRADO;

		} else if (Constantes.DIRECAO_OESTE == direcao) {
			x -= Constantes.LADO_QUADRADO;

		} else {
			throw new IllegalStateException();
		}
	}

	public void norte() {
		deslocar(Constantes.DIRECAO_NORTE);
	}

	public void sul() {
		deslocar(Constantes.DIRECAO_SUL);
	}

	public void leste() {
		deslocar(Constantes.DIRECAO_LESTE);
	}

	public void oeste() {
		deslocar(Constantes.DIRECAO_OESTE);
	}

	public boolean podeDeslocar(byte direcao, Celula outra) {
		if (x == outra.x && y == outra.y) {
			throw new IllegalStateException();
		}

		if (Constantes.DIRECAO_NORTE == direcao) {
			if (x == outra.x) {
				if (outra.y > y) {
					return true;
				}
				return y - Constantes.LADO_QUADRADO > outra.y;
			}
		} else if (Constantes.DIRECAO_SUL == direcao) {
			if (x == outra.x) {
				if (outra.y < y) {
					return true;
				}
				return y + Constantes.LADO_QUADRADO < outra.y;
			}
		} else if (Constantes.DIRECAO_LESTE == direcao) {
			if (y == outra.y) {
				if (outra.x < x) {
					return true;
				}
				return x + Constantes.LADO_QUADRADO < outra.x;
			}
		} else if (Constantes.DIRECAO_OESTE == direcao) {
			if (y == outra.y) {
				if (outra.x > x) {
					return true;
				}
				return x - Constantes.LADO_QUADRADO > outra.x;
			}
		} else {
			throw new IllegalStateException();
		}

		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void criarMemento() {
		xMemento = x;
		yMemento = y;
	}

	public void restaurarMemento() {
		x = xMemento;
		y = yMemento;
	}

	public boolean isLimite() {
		return limite;
	}

	public void setLimite(boolean limite) {
		this.limite = limite;
	}
}