package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class J implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		Celula c0 = new Celula(cor, x, y);
		celulas.add(c0);

		Celula c1 = new Celula(cor, x, y);
		c1.sul();
		celulas.add(c1);

		Celula c2 = new Celula(cor, x, y);
		c2.sul();
		c2.sul();
		celulas.add(c2);

		Celula c3 = new Celula(cor, x, y);
		c3.sul();
		c3.sul();
		c3.oeste();
		celulas.add(c3);

		peca.setEstado(Constantes.VERTICAL);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
		List<Celula> celulas = peca.getCelulas();

		Celula c0 = celulas.get(0);
		Celula c1 = celulas.get(1);
		Celula c2 = celulas.get(2);
		Celula c3 = celulas.get(3);

		if (Constantes.HORIZONTAL == peca.getEstado()) {

			c1.oeste();
			c1.sul();
			peca.setEstado(Constantes.VERTICAL);

		} else if (Constantes.VERTICAL == peca.getEstado()) {

			c1.norte();
			c1.leste();
			peca.setEstado(Constantes.HORIZONTAL);

		}

	}
}