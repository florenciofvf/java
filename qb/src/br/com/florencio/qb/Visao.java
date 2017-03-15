package br.com.florencio.qb;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Visao extends JFrame implements Ouvinte {
	private static final long serialVersionUID = 1L;
	private final Territorio territorio;

	private final JLabel[] rotulos = new JLabel[] { new JLabel("COMANDOS"),
			new JLabel("Tecla A"), new JLabel("Tecla S"),
			new JLabel("Seta esquerda"), new JLabel("Seta direita"),
			new JLabel("Espaço (PAUSAR/REINICIAR)") };

	public Visao() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		territorio = new Territorio(this);
		montarLayout();
		registrarEvento();
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, territorio);

		JPanel panel = new JPanel(new GridLayout(0, 1));
		for (JLabel label : rotulos) {
			panel.add(label);
		}

		add(BorderLayout.EAST, panel);
	}

	private void registrarEvento() {
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					territorio.girar(Constantes.SENTIDO_ANTIHORARIO);
					break;

				case KeyEvent.VK_S:
					territorio.girar(Constantes.SENTIDO_HORARIO);
					break;

				case KeyEvent.VK_RIGHT:
					territorio.deslocar(Constantes.DIRECAO_LESTE);
					break;

				case KeyEvent.VK_LEFT:
					territorio.deslocar(Constantes.DIRECAO_OESTE);
					break;

				case KeyEvent.VK_SPACE:
					territorio.pausarReiniciar();
					break;
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
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
		JOptionPane.showMessageDialog(this, "Perdeu!");
		ini();
	}

	@Override
	public void tamanhoCompletado() {
		JOptionPane.showMessageDialog(this, "Ganhou!");
		ini();
	}
}