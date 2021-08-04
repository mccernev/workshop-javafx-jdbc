package model.entities;

import java.io.Serializable;

public class Department implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;

	public Department() {
	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//Vamos gerar um hashCode and equals para que 
//os meus objetos possam ser comparados pelos conteúdos.
//e não pela referência de ponteiros.
//Por padrão vamos fazer para ele comparar somente por id.

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (id != other.id)
			return false;
		return true;
	}

//vamos gerar um código toString
//para ter facilidade para imprimir os valores 
//dos objetos quando estivermos testando.

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
// implements Serializable
// Isso aqui é para os nossos objetos poderem ser
// transformados em sequências de bites.

// Na linguagem Java a gente tem que fazer o implements serializable
// se a gente quiser que o nosso objeto seja gravado em arquivo,
// seja trafegado em rede e assim, por diante.
// Então vamos ao começo da classe, e acrescentar:
// ... implements Serializable [do pacote java.io() 
// O compilador pede para definir o número da versão.
// nós clicamos na auto-correção e ele adiciona para nós.
}
