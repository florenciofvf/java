package br.com.florencio.qb;

import java.awt.Color;
import java.util.List;

public interface Forma {

	public List<Celula> criarCelulas(Peca peca, Color cor, int x, int y);

	public void girar(Peca peca, byte sentido);

}