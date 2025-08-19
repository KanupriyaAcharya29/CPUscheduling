/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cpuscheduling;
import java.util.*;

class Process {
    int id, arrivalTime, burstTime, priority, completionTime, waitingTime, turnaroundTime, remainingTime;
    
    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
}

/**
 *
 * @author ASUS
 */
public class CPUscheduling {
    static Scanner sc = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         while (true) {
            System.out.println("\nCPU Scheduling Algorithms");
            System.out.println("1. FCFS");
            System.out.println("2. SJF (Non-preemptive)");
            System.out.println("3. Priority Scheduling (Non-preemptive)");
            System.out.println("4. Round Robin");
            System.out.println("5. Exit");
            System.out.print("Choose algorithm: ");
            int choice = sc.nextInt();

            if (choice == 5) break;

            System.out.print("Enter number of processes: ");
            int n = sc.nextInt();
            List<Process> processes = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                System.out.println("\nProcess " + (i + 1));
                System.out.print("Arrival Time: ");
                int at = sc.nextInt();
                System.out.print("Burst Time: ");
                int bt = sc.nextInt();
                int pr = 0;
                if (choice == 3) {
                    System.out.print("Priority (lower number = higher priority): ");
                    pr = sc.nextInt();
                }
                processes.add(new Process(i + 1, at, bt, pr));
            }

            switch (choice) {
                case 1 -> fcfs(processes);
                case 2 -> sjf(processes);
                case 3 -> priority(processes);
                case 4 -> {
                    System.out.print("Enter time quantum: ");
                    int tq = sc.nextInt();
                    roundRobin(processes, tq);
                }
            }
        }
    }

    static void fcfs(List<Process> list) {
        list.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int time = 0;
        for (Process p : list) {
            if (time < p.arrivalTime) time = p.arrivalTime;
            p.waitingTime = time - p.arrivalTime;
            time += p.burstTime;
            p.completionTime = time;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
        }
        printResults(list, "First Come First Serve");
    }

    static void sjf(List<Process> list) {
        List<Process> result = new ArrayList<>();
        List<Process> queue = new ArrayList<>(list);
        int time = 0;

        while (!queue.isEmpty()) {
            List<Process> available = new ArrayList<>();
            for (Process p : queue) {
                if (p.arrivalTime <= time) available.add(p);
            }

            if (available.isEmpty()) {
                time++;
                continue;
            }

            available.sort(Comparator.comparingInt(p -> p.burstTime));
            Process curr = available.get(0);
            queue.remove(curr);
            if (time < curr.arrivalTime) time = curr.arrivalTime;

            curr.waitingTime = time - curr.arrivalTime;
            time += curr.burstTime;
            curr.completionTime = time;
            curr.turnaroundTime = curr.completionTime - curr.arrivalTime;
            result.add(curr);
        }
        printResults(result, "Shortest Job First");
    }

    static void priority(List<Process> list) {
        List<Process> result = new ArrayList<>();
        List<Process> queue = new ArrayList<>(list);
        int time = 0;

        while (!queue.isEmpty()) {
            List<Process> available = new ArrayList<>();
            for (Process p : queue) {
                if (p.arrivalTime <= time) available.add(p);
            }

            if (available.isEmpty()) {
                time++;
                continue;
            }

            available.sort(Comparator.comparingInt(p -> p.priority));
            Process curr = available.get(0);
            queue.remove(curr);
            if (time < curr.arrivalTime) time = curr.arrivalTime;

            curr.waitingTime = time - curr.arrivalTime;
            time += curr.burstTime;
            curr.completionTime = time;
            curr.turnaroundTime = curr.completionTime - curr.arrivalTime;
            result.add(curr);
        }
        printResults(result, "Priority Scheduling");
    }

    static void roundRobin(List<Process> list, int tq) {
        Queue<Process> queue = new LinkedList<>();
        List<Process> all = new ArrayList<>(list);
        all.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int time = 0, index = 0;
        List<Process> completed = new ArrayList<>();

        while (!queue.isEmpty() || index < all.size()) {
            while (index < all.size() && all.get(index).arrivalTime <= time) {
                queue.add(all.get(index++));
            }

            if (queue.isEmpty()) {
                time++;
                continue;
            }

            Process curr = queue.poll();
            if (curr.remainingTime <= tq) {
                time += curr.remainingTime;
                curr.remainingTime = 0;
                curr.completionTime = time;
                curr.turnaroundTime = curr.completionTime - curr.arrivalTime;
                curr.waitingTime = curr.turnaroundTime - curr.burstTime;
                completed.add(curr);
            } else {
                time += tq;
                curr.remainingTime -= tq;
                while (index < all.size() && all.get(index).arrivalTime <= time) {
                    queue.add(all.get(index++));
                }
                queue.add(curr);
            }
        }

        printResults(completed, "Round Robin (Time Quantum = " + tq + ")");
    }

    static void printResults(List<Process> list, String title) {
        System.out.println("\n--- " + title + " ---");
        System.out.printf("%-10s%-15s%-12s%-12s%-15s%-15s\n", 
            "Process", "Arrival Time", "Burst Time", "Priority", "Waiting Time", "Turnaround");
        double totalWT = 0, totalTAT = 0;
        for (Process p : list) {
            System.out.printf("%-10s%-15d%-12d%-12d%-15d%-15d\n", 
                "P" + p.id, p.arrivalTime, p.burstTime, p.priority, p.waitingTime, p.turnaroundTime);
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }
        System.out.printf("Avg Waiting Time: %.2f\n", totalWT / list.size());
        System.out.printf("Avg Turnaround Time: %.2f\n", totalTAT / list.size());
        // TODO code application logic here
    }
    
}
