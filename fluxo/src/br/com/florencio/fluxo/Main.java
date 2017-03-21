package br.com.florencio.fluxo;

public class Main {
	public static void main(String[] args) throws Exception {
		Instancia raiz = new Instancia("Raiz");
		raiz.organizar();
		new Formulario(raiz);
	}
}