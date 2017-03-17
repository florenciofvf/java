package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import br.com.florencio.qb.forma.Barra;
import br.com.florencio.qb.forma.Cruz;
import br.com.florencio.qb.forma.Estrela;
import br.com.florencio.qb.forma.J;
import br.com.florencio.qb.forma.L;
import br.com.florencio.qb.forma.MiniBarra;
import br.com.florencio.qb.forma.MiniL;
import br.com.florencio.qb.forma.MiniT;
import br.com.florencio.qb.forma.MiniU;
import br.com.florencio.qb.forma.MiniUPonta;
import br.com.florencio.qb.forma.MiniZJ;
import br.com.florencio.qb.forma.MiniZL;
import br.com.florencio.qb.forma.Ponto;
import br.com.florencio.qb.forma.Quadrado;
import br.com.florencio.qb.forma.QuadradoOco;

public class Territorio extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Random random = new Random();
	private final FilaEvento filaEvento;
	private final List<Celula> celulas;
	public static List<Forma> formas;
	private final Ouvinte ouvinte;
	private final Visao visao;
	private Peca pecaProxima;
	private int totalPecas;
	private THREAD thread;
	private int intervalo;
	private Peca peca;
	private int xPos;
	private int y;

	public Territorio(Visao visao) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		filaEvento = new FilaEvento();
		celulas = new ArrayList<>();
		setBackground(Color.WHITE);
		this.ouvinte = visao;
		this.visao = visao;
	}

	static {
		formas = new ArrayList<>();
		formas.add(new QuadradoOco());
		formas.add(new Barra());
		formas.add(new MiniBarra());
		formas.add(new Ponto());
		formas.add(new Quadrado());
		formas.add(new Estrela());
		formas.add(new J());
		formas.add(new L());
		formas.add(new MiniL());
		formas.add(new MiniT());
		formas.add(new MiniU());
		formas.add(new MiniZJ());
		formas.add(new MiniZL());
		formas.add(new Cruz());
		formas.add(new MiniUPonta());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		if (Constantes.DESENHAR_PECA_CIRCULAR) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}

		if (peca != null) {
			peca.desenhar(g2);
		}

		if (pecaProxima != null) {
			pecaProxima.desenhar(g2);
		}

		for (Celula c : celulas) {
			c.desenhar(g2);
		}
	}

	public void pausarReiniciar() {
		filaEvento.adicionar(new Acao("PAUSAR_REINICIAR") {
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
		filaEvento.adicionar(new Acao("GIRAR=" + sentido) {
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
		filaEvento.adicionar(new Acao("DESLOCAR=" + direcao) {
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
		peca = null;
		pecaProxima = null;

		if (thread != null) {
			thread.desativar();
		}
		thread = null;

		intervalo = Constantes.INTERVALO_MOVIMENTO;
		celulas.clear();
		totalPecas = 0;

		List<Celula> colunaEsquerd = new ArrayList<>();
		List<Celula> colunaDireita = new ArrayList<>();
		List<Celula> camadaInferio = new ArrayList<>();

		int x = Constantes.DESLOCAMENTO_X_TERRITORIO;
		y = Constantes.DESLOCAMENTO_Y_TERRITORIO;

		for (int i = 0; i < Constantes.TOTAL_CAMADAS; i++) {
			colunaEsquerd.add(new Celula(Color.BLACK, x, y, Constantes.LIMITE_OPACO));
			colunaDireita.add(new Celula(Color.BLACK, x, y, Constantes.LIMITE_OPACO));
			y += Constantes.LADO_QUADRADO;
		}

		for (Celula c : colunaDireita) {
			for (int i = 0; i <= Constantes.TOTAL_COLUNAS; i++) {
				c.leste();
			}
		}

		y -= Constantes.LADO_QUADRADO;

		for (int i = 1; i <= Constantes.TOTAL_COLUNAS; i++) {
			Celula c = new Celula(Color.BLACK, x, y, Constantes.LIMITE_OPACO);
			camadaInferio.add(c);

			for (int j = 0; j < i; j++) {
				c.leste();
			}
		}

		celulas.addAll(colunaEsquerd);
		celulas.addAll(colunaDireita);
		celulas.addAll(camadaInferio);

		xPos = colunaDireita.get(0).x + Constantes.DESLOCAMENTO_X_POS_TERRITORIO;

		criarPecaAleatoria();
		filaEvento.ativar();
	}

	public void criarPecaAleatoria() {
		int x = Constantes.DESLOCAMENTO_X_TERRITORIO + Constantes.LADO_QUADRADO;
		Color cor = Constantes.GERAR_PECAS_COLORIDAS ? Constantes.CORES[random.nextInt(Constantes.CORES.length)]
				: Color.BLACK;
		Forma forma = formas.get(random.nextInt(formas.size()));

		for (int y = 0; y < Constantes.TOTAL_COLUNAS / 2; y++) {
			x += Constantes.LADO_QUADRADO;
		}

		if (pecaProxima == null) {
			pecaProxima = new Peca(forma, cor, xPos, Constantes.DESLOCAMENTO_Y_TERRITORIO);
			cor = Constantes.GERAR_PECAS_COLORIDAS ? Constantes.CORES[random.nextInt(Constantes.CORES.length)]
					: Color.BLACK;
			forma = formas.get(random.nextInt(formas.size()));
		}

		peca = new Peca(pecaProxima.getForma(), pecaProxima.getCor(), x, Constantes.DESLOCAMENTO_Y_TERRITORIO);

		pecaProxima = new Peca(forma, cor, xPos, Constantes.DESLOCAMENTO_Y_TERRITORIO);

		repaint();
	}

	public void descerPeca() {
		filaEvento.adicionar(new Acao("DESCER") {
			public void executar() {
				processar();
			}
		});
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
				if(thread != null) {
					thread.desativar();
				}
				filaEvento.desativar();

				filaEvento.adicionar(new Acao("PERDEU", true) {
					public void executar() {
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
			if(thread != null) {
				thread.desativar();
			}
			filaEvento.desativar();

			filaEvento.adicionar(new Acao("GANHOU", true) {
				public void executar() {
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
				filaEvento.adicionar(new Acao("PROCESSAR") {
					public void executar() {
						processar();
					}
				});

				try {
					if (intervalo < 0) {
						intervalo = 0;
					}

					Thread.sleep(intervalo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}