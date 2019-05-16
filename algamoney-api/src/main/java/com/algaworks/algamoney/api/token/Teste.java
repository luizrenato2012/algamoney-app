package com.algaworks.algamoney.api.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Teste {
	
	void executa() {
		List<Map<Integer,Integer>> listaClientes = this.getListaClientes();
		
		List listaChaves = listaClientes.stream()
			.map(mapCliente -> mapCliente.keySet().toArray()[0])
			.collect(Collectors.toList());
	
		
			
	}
	
	private List<Map<Integer,Integer>> getListaClientes() {
		List<Map<Integer,Integer>> lista = new ArrayList<>();
		Map<Integer,Integer> mapCliente1 = new HashMap<>();
		mapCliente1.put(1, 1001);
		mapCliente1.put(1, 1002);
		mapCliente1.put(1, 1003);
		lista.add(mapCliente1);
		
		Map<Integer,Integer> mapCliente2 = new HashMap<>();
		mapCliente2.put(2, 1001);
		mapCliente2.put(2, 1003);
		mapCliente2.put(2, 1004);
		lista.add(mapCliente2);
		
		Map<Integer,Integer> mapCliente3 = new HashMap<>();
		mapCliente3.put(3, 1001);
		mapCliente3.put(3, 1002);
		mapCliente3.put(3, 1003);
		mapCliente3.put(3, 1004);
		lista.add(mapCliente3);
		
		Map<Integer,Integer> mapCliente4 = new HashMap<>();
		mapCliente4.put(4, 1002);
		mapCliente4.put(4, 1003);
		mapCliente4.put(4, 1004);
		lista.add(mapCliente4);
		
		return lista;
	}
	
	static class ClienteProduto {
		int cliente;
		
		private Map<Integer, Boolean> mapProdutos;
		
		public ClienteProduto(int cliente) {
			super();
			this.cliente = cliente;
			init();
		}

		private void init() {
			mapProdutos = new LinkedHashMap<>();
			mapProdutos.put(1001, false);
			mapProdutos.put(1002, false);
			mapProdutos.put(1003, false);
			mapProdutos.put(1004, false);
			mapProdutos.put(1005, false);
		}
		
		public void adicionaProduto(int produto) {
			mapProdutos.put(produto, true);
		}
		
	}
	
	public static void main(String[] args) {
		new Teste().executa();
	}

}
