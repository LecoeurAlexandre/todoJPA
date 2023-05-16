package org.example.models;

import javax.persistence.*;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private boolean completed = false;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="todoInfos_id", referencedColumnName = "id")
    private TodoInfos todoInfos;

    public Todo() {
    }
    public Todo(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                '}';
    }
}
