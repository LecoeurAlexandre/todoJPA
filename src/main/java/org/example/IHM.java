package org.example;

import org.example.models.Todo;
import org.example.models.TodoInfos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
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
        System.out.println("Veuillez saisir le titre de la tâche à créer");
        String title = sc.nextLine();
        System.out.println("Veuillez saisir la description");
        String description = sc.nextLine();
        System.out.println("Veuillez choisir la date d'échéance");
        String date = sc.nextLine();
        LocalDate dueDate = LocalDate.parse(date);
        System.out.println("Veuillez saisir la priorité");
        Byte priority = sc.nextByte();
        sc.nextLine();

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Todo todo = new Todo(title);
        todo.setTodoInfos(new TodoInfos(description, dueDate, priority));
        em.persist(todo);
        em.getTransaction().commit();
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
                System.out.println(task.getId() + ". " + task.getName() + " (" + (task.isCompleted() ? "Terminée" : "En cours") + ")");
            }
        }
    }
    public void taskIsDone() {
        System.out.println("Entrez l'ID de la tâche à supprimer : ");
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
}
