package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class Estrela implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		y -= Constantes.LADO_QUADRADO;

		Celula c0 = new Celula(cor, x, y);

		Celula c1 = new Celula(cor, x, y);
		c1.sul();
		c1.oeste();

		Celula c2 = new Celula(cor, x, y);
		c2.sul();

		Celula c3 = new Celula(cor, x, y);
		c3.sul();
		c3.leste();

		Celula c4 = new Celula(cor, x, y);
		c4.sul();
		c4.sul();

		celulas.add(c0);
		celulas.add(c1);
		celulas.add(c2);
		celulas.add(c3);
		celulas.add(c4);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
	}
}