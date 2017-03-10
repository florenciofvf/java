package br.com.florencio.teste;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import br.com.florencio.pattern.Composite;

public class Formulario extends JFrame {
	private static final long serialVersionUID = 1L;
	private Painel painel;

	public Formulario(Composite raiz) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		painel = new Painel(raiz);
		montarLayout();
		setSize(800, 600);
		setVisible(true);
	}

	private void montarLayout() {
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, painel);
	}
}