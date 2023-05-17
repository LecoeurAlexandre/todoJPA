package org.example;

import org.example.models.Person;
import org.example.models.Todo;
import org.example.models.TodoInfos;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IHM {
    private Scanner sc = new Scanner(System.in);
    private String choice;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("todolist");
    public void start() {
        System.out.println("Bienvenue");
        do {
            menu();
            choice = sc.nextLine();
            switch (choice) {
                case "1":
                    addTaskAction();
                    break;
                case "2":
                    displayTasks();
                    break;
                case "3":
                    taskIsDone();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "5":
                    addUser();
                    break;
                case "6":
                    displayTasksByUser();
                    break;
                case "7":
                    deleteUser();
                    break;
            }
        }while (!choice.equals("0"));

    }
    public void menu() {
        System.out.println("1- Ajouter une tâche");
        System.out.println("2- Afficher toutes les tâches");
        System.out.println("3- Marquer une tâche comme terminée");
        System.out.println("4- Supprimer une tâche");
        System.out.println("5- Ajouter un nouvel utilisateur");
        System.out.println("6- Afficher toutes les tâches d'un utilisateur");
        System.out.println("7- Supprimer un utilisateur et les tâches associées");
        System.out.println("0- Quitter l'application");
    }
    public void addTaskAction() {
        System.out.println("Ajouter une tâche");
        System.out.println("Veuillez saisir le titre de la tâche à créer");
        String title = sc.nextLine();
        System.out.println("Veuillez saisir la description");
        String description = sc.nextLine();
        System.out.println("Veuillez choisir la date d'échéance (format jj.mm.aaaa)");
        String date = sc.nextLine();
        LocalDate dueDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        System.out.println("Veuillez saisir la priorité (1 à 3)");
        Byte priority = sc.nextByte();
        sc.nextLine();
        System.out.println("Veuillez renseigner l'id de l'utilisateur");
        Long userId = sc.nextLong();
        sc.nextLine();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Person person = em.find(Person.class, userId);
            if(person  != null){
                Todo todo = new Todo(title);
                todo.setTodoInfos(new TodoInfos(description, dueDate, priority));
                todo.setPerson(person);
                System.out.println(todo);
                em.persist(todo);
                transaction.commit();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        em.close();
    }
    public void displayTasks(){
        System.out.println("Afficher toutes les tâches");
        EntityManager em = emf.createEntityManager();
        List<Todo> allTodos = em.createQuery("SELECT t FROM Todo t", Todo.class).getResultList();
        em.close();
        System.out.println(allTodos);
        if (allTodos.isEmpty()) {
            System.out.println("Aucune tâche trouvée.");
        } else {
            System.out.println("=== Liste des tâches ===");
            for (Todo task : allTodos) {
                System.out.println(task.getId() + ". " + task.getName() + " (" + (task.isCompleted() ? "Terminée" : "En cours") + ")"+ ". Description : " +task.getTodoInfos().getDescription()+ ". Priorité : " +task.getTodoInfos().getDueDate()+ ". Priorité : " +task.getTodoInfos().getPriority()+ ". Utilisateur : " +task.getPerson().getName());
            }
        }
    }
    public void taskIsDone() {
        System.out.println("Entrez l'ID de la tâche accomplie : ");
        Long taskId  = sc.nextLong();
        sc.nextLine();
        System.out.println("Marquer une tâche comme terminée");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Todo todo = em.find(Todo.class, taskId);
            if(todo  != null){
                todo.setCompleted(true);
                transaction.commit();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteTask() {
        System.out.println("Supprimer une tâche");
        System.out.println("Quel est l'id de la tâche à supprimer ?");
        Long idChoice = sc.nextLong();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Todo todo = em.find(Todo.class, idChoice);
        em.remove(todo);
        em.getTransaction().commit();
        em.close();
    }

    public void addUser() {
        System.out.println("Ajouter un utilisateur");
        System.out.println("Veuillez saisir le nom de l'utilisateur");
        String name = sc.nextLine();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person person = new Person(name);
        em.persist(person);
        em.getTransaction().commit();
        em.close();

    }
    public void displayTasksByUser(){
        System.out.println("Afficher les tâches d'un utilisateur");
        System.out.println("Veuiller saisir l'id de l'utilisateur");
        Long userId = sc.nextLong();
        sc.nextLine();
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT t FROM Todo t WHERE t.person.id = :id");
            query.setParameter("id",userId);
            List<Todo> allTodosByUser = query.getResultList();
            if (allTodosByUser.isEmpty()) {
                System.out.println("Aucune tâche trouvée.");
            } else {
                System.out.println("=== Liste des tâches ===");
                System.out.println("Liste des tâches de "+allTodosByUser.get(0).getPerson().getName());
                for (Todo task : allTodosByUser) {
                    System.out.println(task.getId() + ". " + task.getName() + " (" + (task.isCompleted() ? "Terminée" : "En cours") + ")"+ ". Description : " +task.getTodoInfos().getDescription()+ ". Priorité : " +task.getTodoInfos().getDueDate()+ ". Priorité : " +task.getTodoInfos().getPriority());
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        em.close();

    }
    public void deleteUser() {
        System.out.println("Supprimer un utilisateur");
        System.out.println("Quel est l'id de l'utilisateur à supprimer ?");
        Long idChoice = sc.nextLong();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Person person = em.find(Person.class, idChoice);
        em.remove(person);
        em.getTransaction().commit();
        em.close();
    }
}
