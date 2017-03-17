package br.com.florencio.qb.forma;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.florencio.qb.Celula;
import br.com.florencio.qb.Constantes;
import br.com.florencio.qb.Forma;
import br.com.florencio.qb.Peca;

public class MiniUPonta implements Forma {

	@Override
	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y) {
		List<Celula> celulas = new ArrayList<>();

		celulas.add(new Celula(cor, x, y));
		celulas.add(new Celula(cor, x, y));
		celulas.add(new Celula(cor, x, y));
		celulas.add(new Celula(cor, x, y));
		celulas.add(new Celula(cor, x, y));
		celulas.add(new Celula(cor, x, y));

		girar(celulas, Constantes.GRAU_0, peca);

		return celulas;
	}

	@Override
	public void girar(Peca peca, byte sentido) {
		List<Celula> celulas = peca.getCelulas();

		if (Constantes.SENTIDO_HORARIO == sentido) {
			if (Constantes.GRAU_0 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_270, peca);
			} else if (Constantes.GRAU_270 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_180, peca);
			} else if (Constantes.GRAU_180 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_90, peca);
			} else if (Constantes.GRAU_90 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_0, peca);
			}
		} else if (Constantes.SENTIDO_ANTIHORARIO == sentido) {
			if (Constantes.GRAU_0 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_90, peca);
			} else if (Constantes.GRAU_90 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_180, peca);
			} else if (Constantes.GRAU_180 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_270, peca);
			} else if (Constantes.GRAU_270 == peca.getGrau()) {
				girar(celulas, Constantes.GRAU_0, peca);
			}
		}
	}

	private void girar(List<Celula> celulas, short grau, Peca peca) {
		Celula c0 = celulas.get(0);
		Celula c1 = celulas.get(1);
		Celula c2 = celulas.get(2);
		Celula c3 = celulas.get(3);
		Celula c4 = celulas.get(4);
		Celula c5 = celulas.get(5);

		c2.copiar(celulas);
		peca.setGrau(grau);

		if (Constantes.GRAU_0 == grau) {
			c0.leste();
			c0.sul();
			c1.leste();
			c3.oeste();
			c4.oeste();
			c4.sul();
			c5.norte();
		} else if (Constantes.GRAU_90 == grau) {
			c0.norte();
			c0.leste();
			c1.norte();
			c3.sul();
			c4.sul();
			c4.leste();
			c5.oeste();
		} else if (Constantes.GRAU_180 == grau) {
			c0.oeste();
			c0.norte();
			c1.oeste();
			c3.leste();
			c4.leste();
			c4.norte();
			c5.sul();
		} else if (Constantes.GRAU_270 == grau) {
			c0.sul();
			c0.oeste();
			c1.sul();
			c3.norte();
			c4.norte();
			c4.oeste();
			c5.leste();
		}
	}
}