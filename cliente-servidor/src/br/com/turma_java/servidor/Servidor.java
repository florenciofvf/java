package br.com.turma_java.servidor;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.turma_java.Mensagem;

public class Servidor {
	public static final boolean PRINT_STACK_TRACE = Boolean.FALSE;
	private final List<Tarefa> tarefas = new ArrayList<>();
	private final Random random = new Random();
	private ServerSocket serverSocket;
	private String nomeVencedor;
	private final boolean borda;
	private final int celulas;
	private Tarefa proxima;
	private int proximoID;
	boolean emAndamento;

	public Servidor(int celulas, boolean borda) {
		this.celulas = celulas;
		this.borda = borda;
	}

	public void iniciar(int porta) throws Exception {
		this.serverSocket = new ServerSocket(porta);
		System.out.println("Servidor iniciado na porta: " + porta);

		while (true) {
			Socket socket = serverSocket.accept();

			if (emAndamento) {
				System.out.println("Rejeitando cliente: " + socket);
				continue;
			}

			ObjectInput in = new ObjectInputStream(socket.getInputStream());
			String apelido = (String) in.readObject();

			try {
				Tarefa tarefa = criarTarefa(socket, apelido);
				System.out.println("Cliente conectado: " + tarefa.getCliente());
				tarefas.add(tarefa);
				tarefa.start();
			} catch (Exception e) {
				Tarefa.excecao("Rejeitado: " + socket, e);
			}
		}
	}

	private Tarefa criarTarefa(Socket socket, String apelido) throws Exception {
		Tarefa tarefa = new Tarefa(this, apelido + (++proximoID), socket);
		tarefa.setCelulaEspecial(random.nextInt(celulas * celulas));
		return tarefa;
	}

	void reiniciarTarefas() {
		for (Tarefa tarefa : tarefas) {
			tarefa.abatido = false;
			tarefa.setCelulaEspecial(random.nextInt(celulas * celulas));
		}
		emAndamento = false;
	}

	synchronized void enviar(Mensagem m) {
		if (encaminhar(m)) {
			notificar(m);

			if (m.isClick()) {
				configProximoJogador(null);
				notificar(new Mensagem(Mensagem.PROXIMO, proxima.getCliente()));
			}

			if (m.isAbatido()) {
				Util.marcarAbatido(tarefas, m);
				String cliente = Util.getClienteVencedor(tarefas);

				if (cliente != null) {
					nomeVencedor = cliente;
					notificar(new Mensagem(Mensagem.VENCEDOR, cliente));
				} else {
					if (m.getCliente().equals(proxima.getCliente())) {
						configProximoJogador(null);
						notificar(new Mensagem(Mensagem.PROXIMO, proxima.getCliente()));
					}
				}
			}

			if (m.isExcluido()) {
				if (m.getCliente().equals(proxima.getCliente())) {
					configProximoJogador(proxima);
					if (proxima != null) {
						notificar(new Mensagem(Mensagem.PROXIMO, proxima.getCliente()));
					} else {
						emAndamento = false;
					}
				}
			}

			if (m.isMontarTela()) {
				notificar(new Mensagem(Mensagem.PROXIMO, proxima.getCliente()));
			}
		}
	}

	private boolean encaminhar(Mensagem m) {
		if (m.isClick()) {
			return emAndamento && m.getCliente().equals(proxima.getCliente());
		}

		if (m.isMontarTela()) {
			return true;
		}

		if (m.isExcluido()) {
			return true;
		}

		if (m.isAbatido()) {
			return true;
		}

		if (m.isAlvo()) {
			return true;
		}

		return false;
	}

	private void configProximoJogador(Tarefa excluida) {
		if (tarefas.isEmpty()) {
			proxima = null;
			return;
		}

		if (tarefas.size() == 1) {
			proxima = tarefas.get(0);
			return;
		}

		Circular circular = new Circular();

		for (Tarefa tarefa : tarefas) {
			circular.add(tarefa);
		}

		if (excluida != null) {
			circular.add(excluida);
		}

		proxima = circular.get(proxima);
	}

	private void notificar(Mensagem mensagem) {
		for (Tarefa tarefa : tarefas) {
			if (tarefa.isValida()) {
				try {
					tarefa.enviar(mensagem);
				} catch (Exception e) {
					tarefa.setValida(false);
					Tarefa.excecao("Servidor.notificar(m): INVALIDANDO A TAREFA: " + tarefa.getCliente(), e);
					tarefa.desconectar();
				}
			}
		}

		Util.excluirInvalidas(tarefas);
	}

	void notificarMontagemTela() {
		Tarefa vencedor = Util.getTarefaValida(tarefas, nomeVencedor);
		proxima = vencedor != null ? vencedor : tarefas.get(0);
		Mensagem m = new Mensagem(Mensagem.MONTAR_TELA, null);
		m.setMetaInfo(Util.montarMetaInfoNomeEspecial(tarefas));
		m.setTotalClientes(tarefas.size());
		m.setCelulas(celulas);
		m.setBorda(borda);
		enviar(m);
	}
}

class Circular {
	NO cabeca;
	NO cauda;
	int qtd;

	public void add(Tarefa t) {
		NO no = new NO(t);
		qtd++;

		if (cabeca == null) {
			cabeca = no;
		}

		if (cauda != null) {
			cauda.proximo = no;
		}

		cauda = no;
		cauda.proximo = cabeca;
	}

	public Tarefa get(Tarefa atual) {
		NO n = cabeca;

		int i = 0;
		while (n != null) {
			if (atual == n.t) {
				break;
			}
			n = n.proximo;
			i++;
			if (i > qtd) {
				throw new IllegalStateException();
			}
		}

		i = 0;
		NO p = n.proximo;
		while (p.t.abatido || !p.t.isValida()) {
			p = p.proximo;
			i++;
			if (i > qtd) {
				throw new IllegalStateException();
			}
		}

		return p.t;
	}
}

class NO {
	NO proximo;
	Tarefa t;

	NO(Tarefa tarefa) {
		t = tarefa;
	}
}