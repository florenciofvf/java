package br.com.florencio.teste;

import br.com.florencio.pattern.Composite;

public class Main2 {
	
	public static void main(String[] args) {
		Composite raiz = new Composite();
				
		raiz.gerarFilhos();
		
		raiz.organizar();
		new Formulario(raiz);
	}
	
}