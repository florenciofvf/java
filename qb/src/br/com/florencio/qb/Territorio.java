package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import br.com.florencio.qb.forma.MiniBarra;
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
	private int y;

	public Territorio(Visao visao) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		celulas = new ArrayList<>();
		formas = new ArrayList<>();
		inicializarFormas();
		setBackground(Color.WHITE);
		this.ouvinte = visao;
		this.visao = visao;
	}

	private void inicializarFormas() {
		//formas.add(new QuadradoOco());
		formas.add(new MiniBarra());
		formas.add(new Ponto());
		// formas.add(new L());
		// formas.add(new J());
		// formas.add(new MiniL());
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
		intervalo = Constantes.INTERVALO_MOVIMENTO;
		
		celulas.clear();

		List<Celula> colunaEsquerd = new ArrayList<>();
		List<Celula> colunaDireita = new ArrayList<>();
		List<Celula> camadaInferio = new ArrayList<>();
		
		int x = Constantes.DESLOCAMENTO_X_TERRITORIO;
		y = Constantes.DESLOCAMENTO_Y_TERRITORIO;
		
		for(int i=0; i<Constantes.TOTAL_CAMADAS; i++) {
			colunaEsquerd.add(new Celula(Color.BLACK, x, y));
			colunaDireita.add(new Celula(Color.BLACK, x, y));
			y += Constantes.LADO_QUADRADO;
		}
		
		for(Celula c : colunaDireita) {
			for(int i=0; i<=Constantes.TOTAL_COLUNAS; i++) {
				c.leste();
			}
		}
				
		y -= Constantes.LADO_QUADRADO;
		
		for(int i=1; i<=Constantes.TOTAL_COLUNAS; i++) {
			Celula c = new Celula(Color.BLACK, x, y);
			camadaInferio.add(c);
			
			for(int j=0; j<i; j++) {
				c.leste();
			}			
		}
		
		celulas.addAll(colunaEsquerd);
		celulas.addAll(colunaDireita);
		celulas.addAll(camadaInferio);
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

	private synchronized void processar() {
		if(peca == null) {
			return;
		}
		
		if(peca.podeDeslocar(celulas, Constantes.DIRECAO_SUL)) {
			peca.deslocar(Constantes.DIRECAO_SUL);
			loopRecortar();
		} else {
			throw new IllegalStateException();
		}
		
		repaint();
	}
	
	private void loopRecortar() {
		if(!peca.podeDeslocar(celulas, Constantes.DIRECAO_SUL)) {
			for(Celula c : peca.getCelulas()) {
				celulas.add(c.clonar());
			}
			loop();
			criarPecaAleatoria();
		}
	}

	private void loop() {
		int pos = Constantes.TOTAL_CAMADAS * 2 + Constantes.TOTAL_COLUNAS;
		List<Celula> marcados = new ArrayList<>();
		
		int y = this.y;
		
		while(y >= Constantes.DESLOCAMENTO_Y_TERRITORIO) {
			marcados.clear();
			
			for(int i=pos; i<celulas.size(); i++) {
				Celula c = celulas.get(i);
				
				if(c.y == y) {
					marcados.add(c);
				}
			}
			
			if(marcados.size() > Constantes.TOTAL_COLUNAS) {
				throw new IllegalStateException();
			}
			
			if(marcados.size() == Constantes.TOTAL_COLUNAS) {
				celulas.removeAll(marcados);
				
				List<Celula> celulasAcima = new ArrayList<>();
				int yAcima = y - Constantes.LADO_QUADRADO;
			
				for(int i=pos; i<celulas.size(); i++) {
					Celula c = celulas.get(i);
					
					if(c.y <= yAcima) {
						celulasAcima.add(c);
					}
				}
				
				for(Celula c : celulasAcima) {
					c.sul();
				}

				y += Constantes.LADO_QUADRADO;
			}
						
			y -= Constantes.LADO_QUADRADO;
		}
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