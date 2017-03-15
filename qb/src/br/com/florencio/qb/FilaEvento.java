package br.com.florencio.qb;

import java.util.ArrayList;
import java.util.List;

public class FilaEvento {
	private final List<Acao> acoes;
	private final THREAD thread;

	public FilaEvento() {
		acoes = new ArrayList<Acao>();
		thread = new THREAD();
		thread.start();
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
					if (acao.especial || acao.grupo == Territorio.grupoVigente) {
						acao.executar();
					} else {
						System.out.println("DESCARTANDO: " + acao.grupo);
					}
				} catch (Exception e) {
					Territorio.grupoVigente++;
					System.out.println("GRUPO VIGENTE=" + Territorio.grupoVigente);
					e.printStackTrace();
				}
			}
		}
	}
}