package br.com.turma_java.cliente;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) throws Exception {
		String servidor = JOptionPane.showInputDialog("Servidor");

		if (servidor == null || servidor.trim().length() == 0) {
			return;
		}

		String apelido = JOptionPane.showInputDialog("Apelido");

		apelido = normalizar(apelido);

		Form form = new Form();
		form.conectar(servidor, 4567, apelido);
		form.setVisible(true);
	}

	private static String normalizar(String s) {
		if (s == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (char c : s.toCharArray()) {
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_') {
				sb.append(c);
			}
		}

		return sb.toString();
	}
}