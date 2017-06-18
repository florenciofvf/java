package br.com.turma_java.servidor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

import br.com.turma_java.Mensagem;

public class Tarefa extends Thread {
	private final ObjectOutput objectOutput;
	private final ObjectInput objectInput;
	private final Servidor servidor;
	private final String cliente;
	private final Socket socket;
	private int celulaEspecial;
	private boolean valida;
	boolean abatido;

	public Tarefa(Servidor servidor, String cliente, Socket socket) throws Exception {
		this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
		this.objectInput = new ObjectInputStream(socket.getInputStream());
		this.servidor = servidor;
		this.cliente = cliente;
		this.socket = socket;
		this.valida = true;
	}

	public void setCelulaEspecial(int celulaEspecial) {
		this.celulaEspecial = celulaEspecial;
	}

	public boolean isValida() {
		return valida;
	}

	public void setValida(boolean valida) {
		this.valida = valida;
	}

	public String getCliente() {
		return cliente;
	}

	public int getCelulaEspecial() {
		return celulaEspecial;
	}

	public void enviar(Mensagem mensagem) throws Exception {
		objectOutput.writeObject(mensagem);
	}

	@Override
	public void run() {
		try {
			enviar(new Mensagem(Mensagem.CONECTADO, cliente));
		} catch (Exception e) {
			valida = false;
			excecao("TarefaServidor.run(): ENVIO MENSAGEM CONECTADO: " + cliente, e);
			desconectar();
			return;
		}

		servidor.notificarMontagemTela();

		while (valida) {
			try {
				Mensagem m = (Mensagem) objectInput.readObject();
				if (m.isReiniciar()) {
					servidor.reiniciarTarefas();
					servidor.notificarMontagemTela();
				} else {
					servidor.enviar(m);
				}
			} catch (Exception e) {
				valida = false;
				excecao("TarefaServidor.run(): LEITURA DA MENSAGEM. INVALIDANDO A TAREFA: " + cliente, e);
				Mensagem m = new Mensagem(Mensagem.EXCLUIDO);
				m.setMetaInfo("[" + cliente + "]");
				servidor.enviar(m);
			}
		}
	}

	void desconectar() {
		try {
			socket.close();
		} catch (IOException ex) {
			excecao("TarefaServidor.desconectar(): " + socket, null);
		}
	}

	public static void excecao(String s, Exception e) {
		System.out.println(s);

		if (e != null && Servidor.PRINT_STACK_TRACE) {
			e.printStackTrace();
		}
	}
}