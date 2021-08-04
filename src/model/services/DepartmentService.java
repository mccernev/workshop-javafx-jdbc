package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

// Precisamos criar uma dependência desse serviço
// lá no controlador da tela DepartmentList
// Isto será feito lá no DepartmentListController.

public class DepartmentService {

	// Agora que o banco já está acessivel
	// Vamos declarar uma dependência e vamos usar a minha fábrica
	// para injetar a dependência aqui:

	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	// Método que retorna todos os departments:
	public List<Department> findAll() {
		// Por enquanto não vamos buscar os dados no banco de dados
		// Vamos MOCKAR os dados
		// MOCK trabalhar com dados de mentirinha.
		// Para isto vamos criar uma lista temporária de Departments:
		// List<Department> list = new ArrayList<>();
		// list.add(new Department(1, "Books"));
		// list.add(new Department(2, "Computers"));
		// list.add(new Department(3, "Electronics"));
		// return list;
		return dao.findAll();
	}

	public void saveOrUpdate(Department obj) {
		// Vamos testar se eu preciso inserir um
		// department ou atualizar um department existente:
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}

}
