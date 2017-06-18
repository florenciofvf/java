package br.com.turma_java.servidor;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import br.com.turma_java.Mensagem;

public class Servidor {
	public static final boolean PRINT_STACK_TRACE = Boolean.FALSE;
	private final List<Tarefa> tarefas = new ArrayList<>();
	private final Random random = new Random();
	private ServerSocket serverSocket;
	private String nomeVencedor;
	private boolean emAndamento;
	private final int celulas;
	private Tarefa proxima;
	private int proximoID;

	public Servidor(int celulas) {
		this.celulas = celulas;
	}

	public void iniciar(int porta) throws Exception {
		serverSocket = new ServerSocket(porta);
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
				emAndamento = true;
				configProximoJogador();
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
						configProximoJogador();
						notificar(new Mensagem(Mensagem.PROXIMO, proxima.getCliente()));
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
			return m.getCliente().equals(proxima.getCliente());
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

	private void configProximoJogador() {
		Tarefa t = null;

		Iterator<Tarefa> iterator = tarefas.iterator();
		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();
			if (tarefa == proxima) {
				break;
			}
		}

		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();
			if (!tarefa.abatido) {
				t = tarefa;
				break;
			}
		}

		proxima = t == null ? tarefas.get(0) : t;
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
		Tarefa vencedor = Util.getVencedor(tarefas, nomeVencedor);
		proxima = vencedor != null ? vencedor : tarefas.get(0);
		Mensagem m = new Mensagem(Mensagem.MONTAR_TELA);
		m.setMetaInfo(Util.montarMetaInfoNomeEspecial(tarefas));
		m.setTotalClientes(tarefas.size());
		m.setCelulas(celulas);
		enviar(m);
	}
}