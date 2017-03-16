package br.com.florencio.qb;

import java.util.ArrayList;
import java.util.List;

public class FilaEvento {
	private final List<Acao> acoes;
	private final THREAD thread;
	private boolean valida;

	public FilaEvento() {
		acoes = new ArrayList<Acao>();
		thread = new THREAD();
		thread.start();
		valida = true;
	}

	public synchronized void adicionar(Acao acao) {
		if (acao != null) {
			acoes.add(acao);
			notifyAll();
		}
	}

	public synchronized Acao proximo() {
		while (acoes.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				throw new IllegalAccessError();
			}
		}

		return acoes.remove(0);
	}

	class THREAD extends Thread {
		public void run() {
			while (true) {
				try {
					Acao acao = proximo();
					if (valida || acao.especial) {
						acao.executar();
					} else {
						System.out.println("DESCARTANDO: " + acao);
					}
				} catch (Exception e) {
					valida = false;
					e.printStackTrace();
				}
			}
		}
	}

	public void desativar() {
		valida = false;
	}

	public void ativar() {
		valida = true;
	}
}