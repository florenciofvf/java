package br.com.florencio.qb.forma;

import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class Ponto implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		Celula c0 = new Celula(x, y);
		celulas.add(c0);
		
		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
	}
}