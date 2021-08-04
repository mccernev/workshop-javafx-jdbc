package model.exceptions;

import java.util.HashMap;
import java.util.Map;

// Ela vai heradar de RuntimeException:

public class ValidationException extends RuntimeException{
// Vamos colocar o número serial padrão, que é o 1. 
	
	private static final long serialVersionUID = 1L;

	//Como essa exceção é uma exceção para validar um formulário
	// Eu vou fazer essa exceção carregar as mensagens de erro
	// do meu formulário, caso elas existam.
	// Um formulario pode ter vários campos.
	// Cada campo pode ter uma msg específica de erro para ele.
	// Para poder carregar esses erros na minha exceção, eu vou 
	// declarar aqui um atirbuto do tipo: Map.
	// Map é uUma coleção de pares chave/valor.
	// O primeiro tipo é da chave e o segundo é do valor.
	// Estamos fazendo isso para poder guardar os erros de cada campo do formulário.
	
	
	private Map<String, String> errors = new HashMap<>();
	
	// Vou forçar a instanciação da exceção com uma string:
	
	public ValidationException(String msg) {
		super(msg);
	}

	public Map<String, String> getErrors(){
		return errors;
	}
	
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}