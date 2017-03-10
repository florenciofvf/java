package br.com.florencio.teste;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import br.com.florencio.pattern.Composite;

public class Painel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Composite raiz;
	
	public Painel(Composite raiz) {
		this.raiz = raiz;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		raiz.desenhar((Graphics2D) g);
	}
}