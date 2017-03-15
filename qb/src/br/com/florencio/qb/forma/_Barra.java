package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class _Barra implements Forma {

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

		peca.setOrientacao(Constantes.HORIZONTAL);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
		List<Celula> celulas = peca.getCelulas();

		Celula c1 = celulas.get(1);
		Celula c2 = celulas.get(2);

		if (Constantes.HORIZONTAL == peca.getOrientacao()) {

			c1.oeste();
			c1.sul();

			c2.oeste();
			c2.oeste();
			c2.sul();
			c2.sul();
			
			peca.setOrientacao(Constantes.VERTICAL);

		} else if (Constantes.VERTICAL == peca.getOrientacao()) {

			c1.norte();
			c1.leste();

			c2.norte();
			c2.norte();
			c2.leste();
			c2.leste();
			
			peca.setOrientacao(Constantes.HORIZONTAL);

		}
	}
}