package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class MiniBarra implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		Celula c0 = new Celula(cor, x, y);
		celulas.add(c0);

		Celula c1 = new Celula(cor, x, y);
		c1.leste();
		celulas.add(c1);

		peca.setOrientacao(Constantes.HORIZONTAL);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
		List<Celula> celulas = peca.getCelulas();

		Celula c1 = celulas.get(1);

		if (Constantes.HORIZONTAL == peca.getOrientacao()) {

			c1.oeste();
			c1.sul();
			peca.setOrientacao(Constantes.VERTICAL);

		} else if (Constantes.VERTICAL == peca.getOrientacao()) {

			c1.norte();
			c1.leste();
			peca.setOrientacao(Constantes.HORIZONTAL);

		}
	}
}