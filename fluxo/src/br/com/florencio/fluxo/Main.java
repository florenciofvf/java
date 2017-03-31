package br.com.florencio.fluxo;

import javax.swing.UIManager;

public class Main {
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		Instancia raiz = new Instancia("Raiz");
		new Formulario(raiz);
	}
}