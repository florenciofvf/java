package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class QuadradoOco implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		Celula c0 = new Celula(cor, x, y);
		c0.oeste();

		Celula c1 = new Celula(cor, x, y);

		Celula c2 = new Celula(cor, x, y);
		c2.leste();

		Celula c3 = new Celula(cor, x, y);
		c3.sul();
		c3.oeste();

		Celula c4 = new Celula(cor, x, y);
		c4.sul();
		c4.leste();

		Celula c5 = new Celula(cor, x, y);
		c5.sul();
		c5.sul();
		c5.oeste();

		Celula c6 = new Celula(cor, x, y);
		c6.sul();
		c6.sul();

		Celula c7 = new Celula(cor, x, y);
		c7.sul();
		c7.sul();
		c7.leste();

		celulas.add(c0);
		celulas.add(c1);
		celulas.add(c2);
		celulas.add(c3);
		celulas.add(c4);
		celulas.add(c5);
		celulas.add(c6);
		celulas.add(c7);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
	}
}