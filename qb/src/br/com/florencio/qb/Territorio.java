package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Territorio extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Random random = new Random();
	private final FilaEvento filaEvento;
	private final List<Celula> celulas;
	private final List<Forma> formas;
	private final Ouvinte ouvinte;
	private final Visao visao;
	static int grupoVigente;
	private int totalPecas;
	private THREAD thread;
	private int intervalo;
	private Peca peca;
	private int y;

	public Territorio(Visao visao) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		filaEvento = new FilaEvento();
		celulas = new ArrayList<>();
		formas = new ArrayList<>();
		setBackground(Color.WHITE);
		this.ouvinte = visao;
		inicializarFormas();
		this.visao = visao;
	}

	private void inicializarFormas() {
//		formas.add(new _QuadradoOco());
//		formas.add(new _Barra());
//		formas.add(new _MiniBarra());
//		formas.add(new _Ponto());
//		formas.add(new _Quadrado());
//		formas.add(new _Estrela());
		
//		formas.add(new L());
//		formas.add(new J());
//		formas.add(new MiniU());
//		formas.add(new MiniL());
//		formas.add(new MiniT());
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

	public void pausarReiniciar() {
		filaEvento.adicionar(new Acao(grupoVigente) {
			public void executar() {
				if (thread == null) {
					thread = new THREAD();
					thread.start();
				} else {
					thread.desativar();
					thread = null;
				}
			}
		});
	}

	public void girar(byte sentido) {
		filaEvento.adicionar(new Acao(grupoVigente) {
			public void executar() {
				if (peca == null) {
					return;
				}
				peca.girar(celulas, sentido);
				repaint();
			}
		});
	}

	public void deslocar(byte direcao) {
		filaEvento.adicionar(new Acao(grupoVigente) {
			public void executar() {
				if (peca == null) {
					return;
				}
				if (peca.podeDeslocar(celulas, direcao)) {
					peca.deslocar(direcao);
					repaint();
				}
			}
		});
	}

	public void ini() {
		intervalo = Constantes.INTERVALO_MOVIMENTO;
		final boolean LIMITE_OPACO = false;
		celulas.clear();
		totalPecas = 0;
		if (thread != null) {
			thread.desativar();
		}
		thread = null;

		List<Celula> colunaEsquerd = new ArrayList<>();
		List<Celula> colunaDireita = new ArrayList<>();
		List<Celula> camadaInferio = new ArrayList<>();

		int x = Constantes.DESLOCAMENTO_X_TERRITORIO;
		y = Constantes.DESLOCAMENTO_Y_TERRITORIO;

		for (int i = 0; i < Constantes.TOTAL_CAMADAS; i++) {
			colunaEsquerd.add(new Celula(Color.BLACK, x, y, LIMITE_OPACO));
			colunaDireita.add(new Celula(Color.BLACK, x, y, LIMITE_OPACO));
			y += Constantes.LADO_QUADRADO;
		}

		for (Celula c : colunaDireita) {
			for (int i = 0; i <= Constantes.TOTAL_COLUNAS; i++) {
				c.leste();
			}
		}

		y -= Constantes.LADO_QUADRADO;

		for (int i = 1; i <= Constantes.TOTAL_COLUNAS; i++) {
			Celula c = new Celula(Color.BLACK, x, y, LIMITE_OPACO);
			camadaInferio.add(c);

			for (int j = 0; j < i; j++) {
				c.leste();
			}
		}

		celulas.addAll(colunaEsquerd);
		celulas.addAll(colunaDireita);
		celulas.addAll(camadaInferio);
		
		criarPecaAleatoria();
	}

	public void criarPecaAleatoria() {
		int x = Constantes.DESLOCAMENTO_X_TERRITORIO + Constantes.LADO_QUADRADO;
		Color cor = Constantes.CORES[random.nextInt(Constantes.CORES.length)];
		Forma forma = formas.get(random.nextInt(formas.size()));

		for (int y = 0; y < Constantes.TOTAL_COLUNAS / 2; y++) {
			x += Constantes.LADO_QUADRADO;
		}

		peca = new Peca(forma, cor, x, Constantes.DESLOCAMENTO_Y_TERRITORIO);
		repaint();
	}

	private void processar() {
		if (peca == null) {
			return;
		}

		if (peca.podeDeslocar(celulas, Constantes.DIRECAO_SUL)) {
			peca.deslocar(Constantes.DIRECAO_SUL);

		} else {
			boolean limiteUltrapassado = false;

			for (Celula c : peca.getCelulas()) {
				if (c.y == Constantes.DESLOCAMENTO_Y_TERRITORIO) {
					limiteUltrapassado = true;
					break;
				}
			}

			if (limiteUltrapassado) {
				filaEvento.adicionar(new Acao(grupoVigente, true) {
					public void executar() {
						thread.desativar();
						ouvinte.limiteUltrapassado();
					}
				});
			} else {
				loopRecortar();
			}
		}

		repaint();
	}

	private void loopRecortar() {
		for (Celula c : peca.getCelulas()) {
			celulas.add(c.clonar());
		}

		peca = null;
		totalPecas++;
		visao.setTitulo("Tamanho = " + totalPecas + " de " + Constantes.TOTAL_PECAS);

		if (totalPecas % Constantes.TOTAL_POR_FASE == 0) {
			intervalo -= Constantes.INTERVALO_DECREMENTO;
		}

		if (totalPecas >= Constantes.TOTAL_PECAS) {
			filaEvento.adicionar(new Acao(grupoVigente, true) {
				public void executar() {
					thread.desativar();
					ouvinte.tamanhoCompletado();
				}
			});
		} else {
			loop();
			criarPecaAleatoria();
		}
	}

	private void loop() {
		int indiceValido = Constantes.TOTAL_CAMADAS * 2 + Constantes.TOTAL_COLUNAS;
		List<Celula> marcados = new ArrayList<>();

		int y = this.y;

		while (y >= Constantes.DESLOCAMENTO_Y_TERRITORIO) {
			marcados.clear();

			for (int i = indiceValido; i < celulas.size(); i++) {
				Celula c = celulas.get(i);

				if (c.y == y) {
					marcados.add(c);
				}
			}

			if (marcados.size() > Constantes.TOTAL_COLUNAS) {
				throw new IllegalStateException();
			}

			if (marcados.size() == Constantes.TOTAL_COLUNAS) {
				celulas.removeAll(marcados);

				List<Celula> celulasAcima = new ArrayList<>();
				int yAcima = y - Constantes.LADO_QUADRADO;

				for (int i = indiceValido; i < celulas.size(); i++) {
					Celula c = celulas.get(i);

					if (c.y <= yAcima) {
						celulasAcima.add(c);
					}
				}

				for (Celula c : celulasAcima) {
					c.sul();
				}

				y += Constantes.LADO_QUADRADO;
			}

			y -= Constantes.LADO_QUADRADO;
		}
	}

	class THREAD extends Thread {
		boolean ativo = true;

		void desativar() {
			ativo = false;
		}

		public void run() {
			while (ativo) {
				filaEvento.adicionar(new Acao(grupoVigente) {
					public void executar() {
						processar();
					}
				});

				try {
					Thread.sleep(intervalo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}