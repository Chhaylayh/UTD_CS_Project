import java.io.*;
import java.util.*;

class Job {

    private String name;
    private int startTime;
    private int duration;

    public Job(String name, int startTime, int duration) {
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        } else {
            this.duration = 0;
        }
    }
}

class ScheduleAlgorithm {

    private ArrayList<Job> jobs;
    private ArrayList<boolean[]> matrix;

    public ScheduleAlgorithm() {
        jobs = new ArrayList<>();
        matrix = new ArrayList<>();
    }

    public void readJobs(String fileName) throws FileNotFoundException {

        List<Job> list = new ArrayList<>();
        Scanner input = new Scanner(new File(fileName));
        int startTime = 0;

        while (input.hasNextLine()) {
            String[] line = input.nextLine().split("\t");
            String name = line[0];
            int duration = Integer.parseInt(line[2]);
            Job job = new Job(name, startTime, duration);
            list.add(job);
            startTime += duration;
        }

        input.close();
        jobs = new ArrayList<>(list);
    }

    public void FCFS() {

        for (Job job : jobs) {

            System.out.printf("%s ", job.getName());

            for (int i = 0; i < job.getStartTime(); i++) {
                System.out.print(" ");
            }

            for (int i = 0; i < job.getDuration(); i++) {
                System.out.print("X");
            }

            System.out.println();
        }
    }

    public void RR(int quantum) {

        Queue<Job> queue = new LinkedList<>();
        int currentTime = 0;
        int currentJobIndex = 0;
        Job cpu = null;

        for (Job job : jobs) {
            System.out.printf("%s ", job.getName());
        }

        System.out.println();

        while (true) {

            boolean allJobsCompleted = true;

            for (int i = 0; i < jobs.size(); i++) {

                Job job = jobs.get(i);

                if (job.getStartTime() <= currentTime && job.getDuration() > 0) {

                    allJobsCompleted = false;

                    int executionTime = Math.min(quantum, job.getDuration());

                    for (int j = 0; j < executionTime; j++) {
                        System.out.print("X   ");
                    }

                    job.setDuration(job.getDuration() - executionTime);

                    if (job.getDuration() <= 0) {
                        currentJobIndex = (currentJobIndex + 1) % jobs.size();
                    }
                    
                } else {
                    System.out.print(" ");
                }
            }

            System.out.println();
            currentTime++;

            // Break the loop if all jobs are completed
            if (allJobsCompleted) {
                break;
            }
        }
    }
}

public class Project3 {

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Error: Input File not specified.");
            return;
        }

        // ArrayList<Job> jobs = new ArrayList<>();
        ScheduleAlgorithm schedule = new ScheduleAlgorithm();
        schedule.readJobs("jobs.txt");

        System.out.println("\nFCFS\n");
        schedule.FCFS();

        System.out.println();

        System.out.println("\nRR\n");
        schedule.RR(1);

    }
}
