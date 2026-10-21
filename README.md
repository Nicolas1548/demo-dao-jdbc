# 🗄️ Demo DAO JDBC — Persistência de Dados com Java e MySQL

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-Data_Persistence-blue?style=for-the-badge)

Projeto desenvolvido em **Java** aplicando acesso a banco de dados relacional (**MySQL**) através da API **JDBC (Java Database Connectivity)**, estruturado com os padrões de projeto **DAO (Data Access Object)** e **Factory**.

O objetivo do sistema é gerenciar o cadastro e o relacionamento entre **Departamentos (`Department`)** e **Vendedores (`Seller`)**, implementando todas as operações **CRUD** (`insert`, `update`, `deleteById`, `findById`, `findAll` e `findByDepartment`) com baixo acoplamento, segurança contra *SQL Injection* e tratamento personalizado de exceções.

---

## 🎯 Conceitos e Boas Práticas Aplicadas

- **Padrão DAO (Data Access Object):** Isolamento completo da lógica de acesso a dados (SQL/JDBC) em relação às regras de negócio e entidades de domínio.
- **Padrão Factory (`DaoFactory`):** Injeção de dependência simplificada e inversão de controle, permitindo instanciar implementações de DAO sem expor a classe concreta (`SellerDaoJDBC` / `DepartmentDaoJDBC`) para o programa principal.
- **Mapeamento Objeto-Relacional Manual:** Conversão de registros do `ResultSet` em objetos Java associados (`Seller` $\leftrightarrow$ `Department`), utilizando `INNER JOIN` e estrutura `Map<Integer, Department>` para evitar instanciação duplicada de departamentos em memória.
- **Segurança e Performance com `PreparedStatement`:** Consultas parametrizadas e recuperação automática de chaves primárias geradas pelo banco (`Statement.RETURN_GENERATED_KEYS`).
- **Exceções Personalizadas:** Tratamento robusto de erros com `DbException` e `DbIntegrityException` (prevenção de falhas de integridade referencial / *Foreign Key*).
- **Gerenciamento de Recursos:** Abertura e fechamento seguro de conexões (`Connection`), comandos (`Statement`) e resultados (`ResultSet`) centralizados na classe utilitária `DB`.

---

## 🏛️ Arquitetura e Estrutura do Projeto

```text
demo-dao-jdbc/
├── src/
│   ├── app/
│   │   ├── Main.java                 # Testes de operações CRUD do SellerDao
│   │   └── Main2.java                # Testes de operações CRUD do DepartmentDao (se aplicável)
│   ├── db/
│   │   ├── DB.java                   # Conexão com o MySQL e fechamento de recursos
│   │   ├── DbException.java          # Exceção personalizada de acesso a dados
│   │   └── DbIntegrityException.java # Exceção para violação de integridade referencial
│   └── model/
│       ├── entities/
│       │   ├── Department.java       # Entidade Departamento (id, name)
│       │   └── Seller.java           # Entidade Vendedor (id, name, email, birthDate, baseSalary, department)
│       └── dao/
│           ├── DaoFactory.java       # Fábrica de instâncias DAO
│           ├── DepartmentDao.java    # Interface de contrato para Department
│           ├── SellerDao.java        # Interface de contrato para Seller
│           └── impl/
│               ├── DepartmentDaoJDBC.java # Implementação JDBC do DepartmentDao
│               └── SellerDaoJDBC.java     # Implementação JDBC do SellerDao
└── db.properties                     # Configurações de conexão com o banco de dados
```

### Relacionamento de Domínio
Um **Departamento (`Department`)** pode possuir vários **Vendedores (`Seller`)**, enquanto cada **Vendedor** pertence a um único **Departamento** (`1:N`).

```text
+-----------------------+          1          * +-----------------------+
|      Department       |<----------------------|        Seller         |
+-----------------------+                       +-----------------------+
| - id: Integer         |                       | - id: Integer         |
| - name: String        |                       | - name: String        |
+-----------------------+                       | - email: String       |
                                                | - birthDate: Date     |
                                                | - baseSalary: Double  |
                                                | - department: Dept    |
                                                +-----------------------+
```

## ⚙️ Operações Implementadas

### `SellerDao` (`SellerDaoJDBC`)
| Método | Descrição | Operação SQL |
| :--- | :--- | :--- |
| `findById(Integer id)` | Busca um vendedor pelo ID junto aos dados do seu departamento. | `SELECT seller.*, department.Name as DepName ... INNER JOIN ... WHERE seller.Id = ?` |
| `findByDepartment(Department dep)` | Lista todos os vendedores de um determinado departamento ordenados por nome. | `SELECT ... INNER JOIN ... WHERE DepartmentId = ? ORDER BY Name` |
| `findAll()` | Lista todos os vendedores cadastrados ordenados por nome. | `SELECT ... INNER JOIN ... ORDER BY Name` |
| `insert(Seller obj)` | Insere um novo vendedor e atribui o ID gerado ao objeto. | `INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)` |
| `update(Seller obj)` | Atualiza os dados cadastrais de um vendedor existente. | `UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?` |
| `deleteById(Integer id)` | Remove um vendedor pelo seu identificador. | `DELETE FROM seller WHERE Id = ?` |

### `DepartmentDao` (`DepartmentDaoJDBC`)
| Método | Descrição | Operação SQL |
| :--- | :--- | :--- |
| `findById(Integer id)` | Busca um departamento pelo ID. | `SELECT * FROM department WHERE Id = ?` |
| `findAll()` | Lista todos os departamentos ordenados por nome. | `SELECT * FROM department ORDER BY Name` |
| `insert(Department obj)` | Insere um novo departamento e recupera o ID gerado. | `INSERT INTO department (Name) VALUES (?)` |
| `update(Department obj)` | Atualiza o nome de um departamento existente. | `UPDATE department SET Name = ? WHERE Id = ?` |
| `deleteById(Integer id)` | Remove um departamento (lança `DbIntegrityException` caso possua vendedores vinculados). | `DELETE FROM department WHERE Id = ?` |

---

## 🚀 Como Executar o Projeto

### 1. Pré-requisitos
- **Java JDK 21+** instalado
- **MySQL Server** em execução
- Driver **MySQL Connector/J** adicionado ao *classpath* do projeto

### 2. Configuração do Banco de Dados (MySQL)
Execute o script SQL abaixo no seu MySQL para criar as tabelas e popular os dados iniciais:

```sql
CREATE DATABASE IF NOT EXISTS coursejdbc;
USE coursejdbc;

CREATE TABLE department (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE seller (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) NOT NULL,
  Email varchar(100) NOT NULL,
  BirthDate datetime NOT NULL,
  BaseSalary double NOT NULL,
  DepartmentId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (DepartmentId) REFERENCES department (id)
);

INSERT INTO department (Name) VALUES 
  ('Computers'),
  ('Electronics'),
  ('Fashion'),
  ('Books');

INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES 
  ('Bob Brown','bob@gmail.com','1998-04-21 00:00:00',1000,1),
  ('Maria Green','maria@gmail.com','1979-12-31 00:00:00',3500,2),
  ('Alex Grey','alex@gmail.com','1988-01-15 00:00:00',2200,1),
  ('Martha Red','martha@gmail.com','1993-11-30 00:00:00',3000,4),
  ('Donald Blue','donald@gmail.com','2000-01-09 00:00:00',4000,3),
  ('Alex Pink','bob@gmail.com','1997-03-04 00:00:00',3000,2);
```

### 3. Configurar o arquivo `db.properties`
Na raiz do projeto, configure suas credenciais locais do MySQL no arquivo `db.properties`:

```properties
user=seu_usuario_mysql
password=sua_senha_mysql
dburl=jdbc:mysql://localhost:3306/coursejdbc
useSSL=false
```

### 4. Executar a Aplicação
Execute a classe `src/app/Main.java` na sua IDE (IntelliJ IDEA, Eclipse ou VS Code) para testar todas as operações do DAO no console.

---

## 👨‍💻 Autor

Desenvolvido por **Nicolas Gabriel**  
🎓 Estudante de Sistemas de Informação — Unileste  
🔗 [LinkedIn](https://www.linkedin.com/in/nicolas-gabriel-ab175a28b/) · [GitHub](https://github.com/Nicolas1548)
