package br.com.turma_java.cliente;

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
	private final Socket socket;
	private final Form form;
	private boolean valida;

	public Tarefa(Form formulario, Socket socket) throws Exception {
		this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
		this.objectInput = new ObjectInputStream(socket.getInputStream());
		this.form = formulario;
		this.socket = socket;
		this.valida = true;
	}

	public void enviar(Mensagem mensagem) throws Exception {
		objectOutput.writeObject(mensagem);
	}

	@Override
	public void run() {
		while (valida) {
			try {
				Mensagem m = (Mensagem) objectInput.readObject();
				form.receber(m);
			} catch (Exception e) {
				valida = false;
				excecao("TarefaCliente.run(): LEITURA DA MENSAGEM. INVALIDANDO A TAREFA: " + form.cliente, e);
				desconectar();
				System.exit(0);
			}
		}
	}

	void desconectar() {
		try {
			socket.close();
		} catch (IOException ex) {
			excecao("TarefaCliente.desconectar(): " + socket, null);
		}
	}

	public static void excecao(String s, Exception e) {
		System.out.println(s);

		if (e != null && Cliente.PRINT_STACK_TRACE) {
			e.printStackTrace();
		}
	}
}