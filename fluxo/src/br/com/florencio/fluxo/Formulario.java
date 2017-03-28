package br.com.florencio.fluxo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.sun.glass.events.KeyEvent;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private JMenuItem menuItemSalvarArquivo = new JMenuItem("Salvar arquivo");
	private JMenuItem menuItemAbrirArquivo = new JMenuItem("Abrir arquivo");
	private JTextField textFieldArquivo = new JTextField();
	private JTextField textFieldSQL = new JTextField();
	private JMenu menuArquivo = new JMenu("Arquivo");
	private JMenuBar menuBarra = new JMenuBar();
	private Painel painel;

	public Formulario(Instancia raiz) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		painel = new Painel(raiz);
		montarMenu();
		montarLayout();
		registrarEventos();
		setSize(800, 600);
		setVisible(true);
	}

	private void montarMenu() {
		setJMenuBar(menuBarra);
		menuBarra.add(menuArquivo);
		menuArquivo.add(menuItemSalvarArquivo);
		menuArquivo.add(menuItemAbrirArquivo);
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, textFieldArquivo);
		add(BorderLayout.CENTER, new JScrollPane(painel));
		add(BorderLayout.SOUTH, textFieldSQL);
	}

	private void registrarEventos() {
		textFieldArquivo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
					abrirArquivo();
				} else if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
					painel.salvarArquivo(textFieldArquivo.getText());
				}
			}
		});

		textFieldArquivo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painel.setArquivo(textFieldArquivo.getText());
			}
		});

		textFieldSQL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painel.processarSQL(textFieldSQL.getText());
			}
		});

		menuItemAbrirArquivo.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
		menuItemAbrirArquivo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirArquivo();
			}
		});

		menuItemSalvarArquivo.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		menuItemSalvarArquivo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				painel.salvarArquivo(null);
			}
		});
	}

	protected void abrirArquivo() {
		JFileChooser fileChooser = new JFileChooser(painel.getArquivo());
		int opcao = fileChooser.showOpenDialog(Formulario.this);

		if (opcao == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			if (file != null) {
				textFieldArquivo.setText(Arquivo.semSufixo(file.getAbsolutePath()));
				painel.setArquivo(file.getAbsolutePath());
			}
		}
	}
}