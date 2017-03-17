package br.com.florencio.qb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

public class Catalogo extends JPanel {
	private static final long serialVersionUID = 1L;
	private final Random random = new Random();
	private final List<Produto> produtos;

	public Catalogo() {
		produtos = new ArrayList<>();
		for (Forma f : Territorio.formas) {
			produtos.add(new Produto(f));
		}
		montarLayout();
	}

	private void montarLayout() {
		setLayout(new GridLayout(0, 3));
		for (Produto p : produtos) {
			add(p);
		}
	}

	public void criarPecas() {
		for (Produto p : produtos) {
			p.criarPeca();
		}
	}

	public List<Forma> getFormas() {
		List<Forma> formas = new ArrayList<>();
		for (Produto p : produtos) {
			if (p.selecionado) {
				formas.add(p.forma);
			}
		}
		return formas;
	}

	class Produto extends JPanel {
		private static final long serialVersionUID = 1L;
		boolean selecionado = true;
		Forma forma;
		Peca peca;

		public Produto(Forma forma) {
			this.forma = forma;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selecionado = !selecionado;
					peca.girar(new ArrayList<>(), Constantes.SENTIDO_HORARIO);
					repaint();
				}
			});
		}

		void criarPeca() {
			int x = getWidth() / 2;
			int y = getHeight() / 2;

			Color cor = Constantes.GERAR_PECAS_COLORIDAS ? Constantes.CORES[random.nextInt(Constantes.CORES.length)]
					: Color.BLACK;

			peca = new Peca(forma, cor, x, y);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);

			Graphics2D g2 = (Graphics2D) g;

			if (Constantes.DESENHAR_PECA_CIRCULAR) {
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}

			if (selecionado) {
				Color c = g.getColor();
				g.setColor(Color.BLUE);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				g.setColor(c);
			}
			
			if (peca != null) {
				peca.desenhar(g2);
			}
		}
	}
}
