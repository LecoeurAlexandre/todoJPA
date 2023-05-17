package org.example.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private boolean completed = false;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="todoInfos_id", referencedColumnName = "id") //referencedColumnName pas obligatoire, utilisé pour pointer sur une autre colonne que l'id. name renseigne le nom de la colonne de la clé étrangère de la table Todo
    private TodoInfos todoInfos;
    @ManyToOne
    @JoinColumn(name="person_id")
    private Person person;

    @ManyToMany
    @JoinTable(name="todo_category", joinColumns = @JoinColumn(name= "todo_id"), inverseJoinColumns = @JoinColumn(name= "category_id"))
    private List<Category> categories = new ArrayList<>();

    public Todo() {
    }
    public Todo(String name) {
        this.name = name;
    }
    public void addCategory(Category category) {
        categories.add(category);
        category.getTodos().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public TodoInfos getTodoInfos() {
        return todoInfos;
    }

    public void setTodoInfos(TodoInfos todoInfos) {
        this.todoInfos = todoInfos;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                '}';
    }
}
