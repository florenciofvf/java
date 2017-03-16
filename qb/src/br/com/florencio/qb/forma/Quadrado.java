package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class Quadrado implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		Celula c0 = new Celula(cor, x, y);
		c0.oeste();

		Celula c1 = new Celula(cor, x, y);

		Celula c2 = new Celula(cor, x, y);
		c2.oeste();
		c2.sul();

		Celula c3 = new Celula(cor, x, y);
		c3.sul();

		celulas.add(c0);
		celulas.add(c1);
		celulas.add(c2);
		celulas.add(c3);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
	}
}