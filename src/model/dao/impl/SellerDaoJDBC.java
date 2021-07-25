package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

//Vamos informar que esta classe
//vai implementar minha interface SellerDao

// O compilador avisa que falta implementar os métodos da interface.
// Eu aceito a auto-correção
// Então o Eclipse cria uma esqueleto dos métodos:

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	// para forçar a injeção de dependência aqui dentro
	// eu vou colocar um construtor:

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	// Esse objeto conn eu vou ter ele à disposição,
	// em qualquer lugar da minha classe SellerDaoJDBC!

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"INSERT INTO seller " 
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					// Aqui estamos colocando um comando para ele retornar o
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			// st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				// Significa que ele inseriu o registro
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					// vou atribuir o id ao objeto
					// para que ele já fique populado:
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " 
					+ " WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			
			if (rows == 0){
				// significa que o id não exitia no banco.
				throw new DbException ("Id not exist"); 
			}
						
		}
		catch(SQLException e){
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		// E a conexão com o banco, eu vou ter que criar
		// um objeto conection também?
		// Não!
		// Porque o nosso Dao vai ter uma dependência com a conexão!
		// Vide o início desta classe que tem uma dependência em relação ao ojeto conn.

		try {
			st = conn.prepareStatement(

					// Aqui vamos colocar o comando SQL:

					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			// Agora vamos configurar esse campo ?:

			st.setInt(1, id);
			rs = st.executeQuery();

			// Explicação importante:

			// O nosso resultSet ele traz os dados para mim
			// no formato de tabela, ele é um objeto com linhas e colunas.

			// Só que estamos programando orientado a objetos.
			// A nossa classe Dao é responsável por pegar esses dados aqui
			// do banco de dados relacional e transforma-los em objetos associados.
			// Então na verdade eu vou querer criar um objeto, com os dados do Alex,
			// e associado a ele vai ter um outro objeto do tipo department, com os dados do
			// deparment dele.
			// Isso aqui é muito importante a gente entender. Quando a gente está
			// programando um sistema
			// orientado a objetos, mesmo eu buscando os objetos no banco de dados, na forma
			// de tabela,
			// na memória do computador, eu vou querer ter os objetos associados,
			// instanciados em memória.

			// Como que eu vou fazer isso?
			// Primeiro tenho que lembrar o seguinte:
			// Quando acabo de realizar uma consulta SQL
			// e vem o resultado no resultSet,
			// Esse resultSet ele está apontando para a posição zero.
			// A posição zero não contem objeto!
			// É só na posição 1 que contém.
			// Por isso vamos fazer um if:

			if (rs.next()) {
				// Estamos criando funções para melhorar o código
				// e possibilitar reutilização de instanciação:

				// Department dep = new Department();
				// dep.setId(rs.getInt("DepartmentId"));
				// dep.setName(rs.getString("DepName"));

				Department dep = instantiateDepartment(rs);

				// Esse if é justamente para testar se veio algum resultado.
				// Se não tiver vindo nada, esse rs.next vai dar falso.
				// Eu vou pular esse if aqui e vou retornar nulo,
				// ou seja, não existe nenhum vendedor com esse id.
				// Agora, se esse rs.next deu verdadeiro, significa que retornou
				// essa tabelinha com os dadosdo meu vendedor.
				// Então eu vou ter que navegar nesses dados aqui, para instanciar
				// os meus objetos.

				// Então com isso aqui nós instanciamos um department;
				// E setamos os valores dele.

				// Agora vamos ter que criar o objeto Seller
				// Com os dados que vieram da tabela
				// E inclusive apontando para o objeto Department.

				// Estamos criando funções para melhorar o código
				// e possibilitar reutilização de instanciação:

				Seller obj = instantiateSeller(rs, dep);
				// Seller obj = new Seller();
				// obj.setId(rs.getInt("Id"));
				// obj.setName(rs.getString("Name"));
				// obj.setEmail(rs.getString("Email"));
				// obj.setBaseSalary(rs.getDouble("BaseSalary"));
				// obj.setBirthDate(rs.getDate("BirthDate"));
				// Aqui para fazer o department
				// Você vai querer o objeto montado:
				// Por isso vamos usar o dep que montamos acima!
				// obj.setDepartment(dep);

				// Aqui criamos nosso objeto Seller com todos os
				// atributos já definidos
				// Então, ao final, vamos mandar retornar esse objeto Seller:
				return obj;
			}

			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			// vamos fechar nossos recursos:
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// Aqui alguém poderia perguntar:
			// Tem que fechar a conexão?
			// Não!
			// Deixe a conexão aberta, porque o mesmo objeto DAO
			// ele pode servir para fazer mais de uma operação.
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		// aqui não vamos tratar a exceção, porque lá em cima já estamos tratando.
		// Então, nesse método auxiliar aqui.
		// Eu simplesmente vou propagar a exceção:

		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		// aqui não vamos tratar a exceção, porque lá em cima já estamos tratando.
		// Então, nesse método auxiliar aqui.
		// Eu simplesmente vou propagar a exceção:

		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id ");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			// Para controlar a não repetição de departament
			// vamos utilizar a estrutura map
			// que é aquela estrtura de chave/valor:

			Map<Integer, Department> map = new HashMap<>();
			// Aqui criei um map vazio.
			// Vou guardar dentro dele qualquer department que eu instanciar.

			while (rs.next()) {
				// A cada vez que passar aqui, vou ter
				// que testar se o Department já existe.
				// Como faço isso, no meu map eu tento buscar
				// com o método get, um Department que tem esse id (rs.getInt(DepartmentId"):
				Department dep = map.get(rs.getInt("DepartmentId"));
				// Se não existir, esse map.get vai retornar nullo
				// Então se for nulo, aí sim eu vou instanciar o Departament.
				if (dep == null) {
					dep = instantiateDepartment(rs);
					// Agora vou salvar este department dentro do meu map
					// para que da próxima vez, eu possa fazer a verificação
					// e ver que ele já existe, para não criar outro.
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(

					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			// Para controlar a não repetição de departament
			// vamos utilizar a estrutura map
			// que é aquela estrtura de chave/valor:

			Map<Integer, Department> map = new HashMap<>();
			// Aqui criei um map vazio.
			// Vou guardar dentro dele qualquer department que eu instanciar.

			while (rs.next()) {
				// A cada vez que passar aqui, vou ter
				// que testar se o Department já existe.
				// Como faço isso, no meu map eu tento buscar
				// com o método get, um Department que tem esse id (rs.getInt(DepartmentId"):
				Department dep = map.get(rs.getInt("DepartmentId"));
				// Se não existir, esse map.get vai retornar nullo
				// Então se for nulo, aí sim eu vou instanciar o Departament.
				if (dep == null) {
					dep = instantiateDepartment(rs);
					// Agora vou salvar este department dentro do meu map
					// para que da próxima vez, eu possa fazer a verificação
					// e ver que ele já existe, para não criar outro.
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}
}
