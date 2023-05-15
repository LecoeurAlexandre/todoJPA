package org.example;

import org.example.models.Todo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
            }
        }while (!choice.equals("0"));

    }
    public void menu() {
        System.out.println("1- Ajouter une tâche");
        System.out.println("2- Afficher toutes les tâches");
        System.out.println("3- Marquer une tâche comme terminée");
        System.out.println("4- Supprimer une tâche");
        System.out.println("0- Quitter l'application");
    }
    public void addTaskAction() {
        System.out.println("Ajouter une tâche");
        System.out.println("Veuillez saisir la tâche à créer");
        choice = sc.nextLine();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Todo todo = new Todo(choice);
        em.persist(todo);
        em.getTransaction().commit();
        em.close();
    }
    public void displayTasks(){
        System.out.println("Afficher toutes les tâches");
    }
    public void taskIsDone() {
        System.out.println("Marquer une tâche comme terminée");
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
}
