package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class _Estrela implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		x -= Constantes.LADO_QUADRADO;
		
		Celula c0 = new Celula(cor, x, y);
		celulas.add(c0);

		Celula c1 = new Celula(cor, x, y);
		c1.sul();
		c1.oeste();
		celulas.add(c1);

		Celula c2 = new Celula(cor, x, y);
		c2.sul();
		celulas.add(c2);

		Celula c3 = new Celula(cor, x, y);
		c3.sul();
		c3.leste();
		celulas.add(c3);

		Celula c4 = new Celula(cor, x, y);
		c4.sul();
		c4.sul();
		celulas.add(c4);
		
		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
	}
}