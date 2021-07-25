package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {

	void insert(Department obj);
	// essa operação será a responsável por inserir no banco de dados
	// esse objeto que eu enviar como parâmetro de entrada.
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	// Essa operação vai pegar esse id,
	// Consultar no banco de dados o objeto com esse id
	// Se existir, vai retornar o objeto
	// Se não existir vai retornar nulo.
	List<Department> findAll();
	// Como vai retornar os departments
	// ela retorna uma lista de departments 
}
