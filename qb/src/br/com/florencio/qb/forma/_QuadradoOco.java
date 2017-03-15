package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class _QuadradoOco implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		x -= Constantes.LADO_QUADRADO;
		
		Celula c0 = new Celula(cor, x, y);
		celulas.add(c0);

		Celula c1 = new Celula(cor, x, y);
		c1.leste();
		celulas.add(c1);

		Celula c2 = new Celula(cor, x, y);
		c2.leste();
		c2.leste();
		celulas.add(c2);

		Celula c3 = new Celula(cor, x, y);
		c3.sul();
		celulas.add(c3);

		Celula c4 = new Celula(cor, x, y);
		c4.sul();
		c4.leste();
		c4.leste();
		celulas.add(c4);

		Celula c5 = new Celula(cor, x, y);
		c5.sul();
		c5.sul();
		celulas.add(c5);

		Celula c6 = new Celula(cor, x, y);
		c6.sul();
		c6.sul();
		c6.leste();
		celulas.add(c6);

		Celula c7 = new Celula(cor, x, y);
		c7.sul();
		c7.sul();
		c7.leste();
		c7.leste();
		celulas.add(c7);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
	}
}