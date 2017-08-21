package utils;

import it.sauronsoftware.cron4j.*;

import java.time.LocalTime;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/21
 */
public class CollectorExp {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        TaskCollector c1 = new TaskCollector() {
            @Override
            public TaskTable getTasks() {
                System.out.println("过了一分钟 调度器又来索引我啦");
                TaskTable taskTable = new TaskTable();
                taskTable.add(new SchedulingPattern("* * * * *"), new MyTask("one"));
                taskTable.add(new SchedulingPattern("*/2 * * * *"), new MyTask("two"));
                return taskTable;
            }
        };

        TaskCollector c2 = () ->{
            System.out.println("过了一分钟 调度器又来索引我啦");
            TaskTable taskTable = new TaskTable();
            taskTable.add(new SchedulingPattern("* * * * *"), new MyTask("three"));
            taskTable.add(new SchedulingPattern("*/2 * * * *"), new MyTask("four"));
            return taskTable;
        };

        scheduler.addTaskCollector(c1);
        scheduler.addTaskCollector(c2);

        showController(scheduler);

        scheduler.start();

        try {
            Thread.sleep(2000L * 60L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("两分钟到 移除c1");
        scheduler.removeTaskCollector(c1);
        showController(scheduler);

    }

    static void showController(Scheduler scheduler){
        TaskCollector[] taskCollectors = scheduler.getTaskCollectors();
        System.out.println("|----当前调度器中有"+taskCollectors.length+"个收集器");
        for (int i = 0 ; i < taskCollectors.length ; ++i){
            System.out.println("|----|----当前显示第"+(i+1)+"个收集器的信息");
            TaskCollector now = taskCollectors[i];
            TaskTable tasks = now.getTasks();
            System.out.println("|----|----|----当前收集器有"+tasks.size()+"个任务");
            for (int j = 0 ; j < tasks.size() ; ++j){
                System.out.println("|----|----|----|----当前显示第"+(j+1)+"个任务信息");
                System.out.println("|----|----|----|----Task:["+tasks.getTask(j)+"] and scp:["+tasks.getSchedulingPattern(j)+"]");
            }
        }
    }

}

class MyTask extends Task{

    private String num;

    MyTask(String num) {
        this.num = num;
    }

    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        LocalTime now = LocalTime.now();
        System.out.println("This is Task "+num+" ! [ " + now.getHour() + " : " + now.getMinute() + " ]");

    }
}