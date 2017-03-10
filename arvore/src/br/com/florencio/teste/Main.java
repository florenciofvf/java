package br.com.florencio.teste;

import java.util.Random;

import br.com.florencio.pattern.Composite;

public class Main {
	private static Random random = new Random();
	
	public static void main(String[] args) {
		Composite raiz = new Composite();
		
		Composite filho1 = new Composite();
		raiz.add(filho1);
		
		Composite neto1 = new Composite();
		neto1.add(new Composite());
		Composite neto2 = new Composite();
		filho1.add(neto1);
		filho1.add(neto2);
		
		Composite filho2 = new Composite();
		raiz.add(filho2);
		filho2.add(new Composite());
		filho2.add(new Composite());

		Composite filho3 = new Composite();
		raiz.add(filho3);
		
		Composite neto3 = new Composite();
		Composite neto4 = new Composite();
		filho3.add(neto3);
		filho3.add(neto4);
		
		Composite bisNeto = new Composite();

		neto3.add(bisNeto);
		neto3.add(new Composite());
		neto3.add(new Composite());
		neto3.add(new Composite());
		neto3.add(new Composite());

		neto4.add(new Composite());
		
		neto2.add(new Composite());
		neto2.add(new Composite());
		neto2.add(new Composite());
		neto2.add(new Composite());
		
		bisNeto.add(new Composite());
		bisNeto.add(new Composite());
		
		raiz.organizar();
		new Formulario(raiz);
	}
	
	static void gerarFilhos(Composite c) {
		int total = random.nextInt(5);
		
		for(int i=0; i<total; i++) {
			c.add(new Composite());
		}
	}
}