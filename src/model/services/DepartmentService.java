package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

// Precisamos criar uma dependência desse serviço
// lá no controlador da tela DepartmentList
// Isto será feito lá no DepartmentListController.


public class DepartmentService {
	// Vamos criar um método que retorne todos os departments:
	public List<Department> findAll() {
		// Por enquanto não vamos buscar os dados no banco de dados
		// Vamos MOCKAR os dados
		// MOCK trabalhar com dados de mentirinha.
		// Para isto vamos criar uma lista temporária de Departments:
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Electronics"));
		return list;
	}

}
