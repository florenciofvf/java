package br.com.florencio.fluxo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private Painel painel;

	public Formulario(Instancia raiz) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		painel = new Painel(raiz);
		montarLayout();
		setSize(800, 600);
		setVisible(true);
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, new JScrollPane(painel));
	}
}