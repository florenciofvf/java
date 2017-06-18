package br.com.turma_java.servidor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.turma_java.Mensagem;

public class Util {

	public static Tarefa getVencedor(List<Tarefa> tarefas, String nome) {
		Iterator<Tarefa> iterator = tarefas.iterator();

		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();

			if (tarefa.isValida() && tarefa.getCliente().equals(nome)) {
				return tarefa;
			}
		}

		return null;
	}

	public static void excluirInvalidas(List<Tarefa> tarefas) {
		Iterator<Tarefa> iterator = tarefas.iterator();

		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();

			if (!tarefa.isValida()) {
				iterator.remove();
			}
		}
	}

	public static String montarMetaInfoNomeEspecial(List<Tarefa> tarefas) {
		Iterator<Tarefa> iterator = tarefas.iterator();
		StringBuilder builder = new StringBuilder();

		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();

			builder.append(tarefa.getCliente() + "-" + tarefa.getCelulaEspecial() + ",");
		}

		return builder.toString();
	}

	public static void marcarAbatido(List<Tarefa> tarefas, Mensagem m) {
		Iterator<Tarefa> iterator = tarefas.iterator();

		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();

			if (tarefa.getCliente().equals(m.getCliente())) {
				tarefa.abatido = true;
			}
		}
	}

	public static String getClienteVencedor(List<Tarefa> tarefas) {
		List<String> clientes = new ArrayList<>();
		List<String> abatidos = new ArrayList<>();

		Iterator<Tarefa> iterator = tarefas.iterator();

		while (iterator.hasNext()) {
			Tarefa tarefa = iterator.next();

			if (tarefa.abatido) {
				abatidos.add(tarefa.getCliente());
			} else {
				clientes.add(tarefa.getCliente());
			}
		}

		if (abatidos.isEmpty() || clientes.isEmpty() || clientes.size() > 1) {
			return null;
		}

		return clientes.get(0);
	}
}