package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import br.com.florencio.qb.forma.J;
import br.com.florencio.qb.forma.L;
import br.com.florencio.qb.forma.MiniBarra;
import br.com.florencio.qb.forma.MiniL;
import br.com.florencio.qb.forma.Ponto;
import br.com.florencio.qb.forma.QuadradoOco;

public class Territorio extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Random random = new Random();
	private final List<Celula> celulas;
	private final List<Forma> formas;
	private final Visao visao;
	private Ouvinte ouvinte;
	private THREAD thread;
	private int intervalo;
	private Peca peca;

	public Territorio(Visao visao) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		celulas = new ArrayList<>();
		formas = new ArrayList<>();
		inicializarFormas();
		setBackground(Color.WHITE);
		this.visao = visao;
	}

	private void inicializarFormas() {
		formas.add(new QuadradoOco());
		formas.add(new MiniBarra());
		formas.add(new Ponto());
		formas.add(new L());
		formas.add(new J());
		formas.add(new MiniL());
	}
	
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		if (peca != null) {
			peca.desenhar(g2);
		}

		for (Celula c : celulas) {
			c.desenhar(g2);
		}
	}

	public void atualizarVisao() {
		repaint();
	}

	public void pausarReiniciar() {
		if (thread == null) {
			thread = new THREAD();
			thread.start();
		} else {
			thread.desativar();
			thread = null;
		}
	}

	public void girar(byte sentido) {
		if (peca != null) {
			peca.girar(celulas, sentido);
			repaint();
		}
	}

	public void deslocar(byte direcao) {
		if (peca != null) {
			if (peca.podeDeslocar(celulas, direcao)) {
				peca.deslocar(direcao);
				repaint();
			}
		}
	}

	public void ini() {
		// serpente = new Serpente();
		// serpente.setTerritorio(this);
		// serpente.setOuvinte(visao);
	}

	public void criarPecaAleatoria() {
		int pos = random.nextInt(formas.size());

		Forma forma = formas.get(pos);

		int x = Constantes.DESLOCAMENTO_X_TERRITORIO + Constantes.LADO_QUADRADO;

		for (int y = 0; y < Constantes.TOTAL_COLUNAS / 2; y++) {
			x += Constantes.LADO_QUADRADO;
		}

		peca = new Peca(forma, x, Constantes.DESLOCAMENTO_Y_TERRITORIO);
		repaint();
	}

	private void processar() {
		// cabeca.deslocar();
	}

	class THREAD extends Thread {
		private boolean ativo = true;

		void desativar() {
			ativo = false;
		}

		public void run() {
			while (ativo && celulas.size() < Constantes.TOTAL_PECAS) {
				try {
					Thread.sleep(intervalo);
					processar();
				} catch (Exception e) {
					ouvinte.limiteUltrapassado();
					ativo = false;
					break;
				}
			}

			if (ativo) {
				ouvinte.tamanhoCompletado();
			}
		}
	}
}