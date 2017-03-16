package br.com.florencio.qb;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Visao extends JFrame implements Ouvinte {
	private static final long serialVersionUID = 1L;
	private final Territorio territorio;
	private final JTabbedPane fichario;
	private final Catalogo catalogo;

	private final JLabel[] rotulos = new JLabel[] { new JLabel(Strings.get("label_comandos")),
			new JLabel(Strings.get("label_anti_horario")), new JLabel(Strings.get("label_horario")),
			new JLabel(Strings.get("label_seta_esquerda")), new JLabel(Strings.get("label_seta_direita")),
			new JLabel(Strings.get("label_seta_abaixo")), new JLabel(Strings.get("label_pausar_reiniciar")) };

	public Visao() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		territorio = new Territorio(this);
		fichario = new JTabbedPane();
		catalogo = new Catalogo();
		montarLayout();
		registrarEvento();
	}

	private void montarLayout() {
		setLayout(new BorderLayout());

		
		JPanel panelJogo = new JPanel(new BorderLayout());
		panelJogo.add(BorderLayout.CENTER, territorio);
		JPanel panel = new JPanel(new GridLayout(0, 1));
		for (JLabel label : rotulos) {
			panel.add(label);
		}
		panelJogo.add(BorderLayout.EAST, panel);
		
		fichario.addTab(Strings.get("label_componente"), catalogo);
		fichario.addTab(Strings.get("label_jogo"), panelJogo);

		add(BorderLayout.CENTER, fichario);
	}

	private void registrarEvento() {
		fichario.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(fichario.getSelectedIndex() == 0) {
					catalogo.criarPecas();
					
				} else if(fichario.getSelectedIndex() == 1) {
					List<Forma> formas = catalogo.getFormas();
					
					if(formas.size() < Constantes.MINIMO_PECA_JOGO) {
						fichario.setSelectedIndex(0);
						JOptionPane.showMessageDialog(Visao.this, Strings.get("label_minimo_peca_jogo") + Constantes.MINIMO_PECA_JOGO);
					} else {
						Territorio.formas.clear();
						Territorio.formas.addAll(formas);
						ini();
					}
				}
			}
		});
		
		fichario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					territorio.girar(Constantes.SENTIDO_ANTIHORARIO);
					break;

				case KeyEvent.VK_S:
					territorio.girar(Constantes.SENTIDO_HORARIO);
					break;

				case KeyEvent.VK_RIGHT:
					e.consume();
					territorio.deslocar(Constantes.DIRECAO_LESTE);
					break;

				case KeyEvent.VK_LEFT:
					e.consume();
					territorio.deslocar(Constantes.DIRECAO_OESTE);
					break;

				case KeyEvent.VK_DOWN:
					territorio.descerPeca();
					break;

				case KeyEvent.VK_SPACE:
					territorio.pausarReiniciar();
					break;
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				catalogo.criarPecas();
				catalogo.repaint();
				ini();
			}
		});
	}

	public void setTitulo(String s) {
		setTitle(s);
	}

	private void ini() {
		territorio.ini();
		territorio.criarPecaAleatoria();
	}

	@Override
	public void limiteUltrapassado() {
		JOptionPane.showMessageDialog(this, Strings.get("label_perdeu"));
		ini();
	}

	@Override
	public void tamanhoCompletado() {
		JOptionPane.showMessageDialog(this, Strings.get("label_ganhou"));
		ini();
	}
}