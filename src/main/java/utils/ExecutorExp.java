package utils;

import it.sauronsoftware.cron4j.*;

import java.time.LocalTime;

/**
 * @description:
 * @author: youyinnn
 * @date: 2017/8/21
 */
public class ExecutorExp {

    public static void main(String[] args) {

        MyTask2 t1 = new MyTask2("one");
        MyTask2 t2 = new MyTask2("two");

        TaskCollector taskCollector = () ->{
            System.out.println("调度器索引");
            TaskTable taskTable = new TaskTable();
            taskTable.add(new SchedulingPattern("* * * * *"), t1);
            taskTable.add(new SchedulingPattern("* * * * *"), t2);
            return taskTable;
        };


        Scheduler scheduler = new Scheduler();
        scheduler.addTaskCollector(taskCollector);
        scheduler.addSchedulerListener(new ScListener());

        TaskCollector taskCollector1 = () ->{
            TaskTable taskTable = new TaskTable();
            taskTable.add(new SchedulingPattern("* * * * *"), new Task() {
                @Override
                public void execute(TaskExecutionContext context) throws RuntimeException {
                    System.out.println("*************（被检测调度器状态区）****************");

                    System.out.println("被测调度器的情况：");
                    TaskExecutor[] executingTasks = scheduler.getExecutingTasks();
                    System.out.println("调度器中含有执行器的数量："+executingTasks.length);
                    for (TaskExecutor executingTask : executingTasks) {
                        System.out.println("+++++++++++（执行器信息区）+++++++++++");

                        // 通过guid我们可以发现 每个任务一旦执行成功就会销毁
                        // 然后猜测是在调度器索引收集器的时候又会创建新任务做同样的事情
                        System.out.println("该执行器的GUID：" + executingTask.getGuid());

                        // 以下两个状态我们只能是在执行器正在执行任务的时候获取
                        System.out.println("该任务当前的状态信息：" + executingTask.getStatusMessage());
                        System.out.println("该任务当前的完成度信息：" + executingTask.getCompleteness());

                        System.out.println("+++++++++++（执行器信息区）+++++++++++");
                    }

                    if (MyTask2.getCount() == 3){
                        System.out.println("已经执行3次了 该停止调度器了");
                        scheduler.stop();

                        System.out.println("调度器1也该停止了");
                        context.getScheduler().stop();
                    }
                    System.out.println("*************（被检测调度器状态区）****************");
                }
            });

            return taskTable;
        };
        Scheduler scheduler1 = new Scheduler();
        scheduler1.addTaskCollector(taskCollector1);

        scheduler.start();
        scheduler1.start();
    }

}

class ScListener implements SchedulerListener{

    @Override
    public void taskLaunching(TaskExecutor executor) {
        System.out.println("执行器开始运行");
    }

    @Override
    public void taskSucceeded(TaskExecutor executor) {
        System.out.println("任务被执行成功");
    }

    @Override
    public void taskFailed(TaskExecutor executor, Throwable exception) {
        System.out.println("任务被执行失败");
    }
}

class MyTask2 extends Task{

    private String num;
    private static int count = 0;

    static int getCount() {
        return count;
    }

    MyTask2(String num) {
        this.num = num;
    }

    @Override
    public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
        System.out.println("-------------------（任务执行区）---------------------");


        LocalTime now = LocalTime.now();
        System.out.println("This is Task "+num+" ! [ " + now.getHour()
                + " : " + now.getMinute() + " ] and count:"+(++count));

        taskExecutionContext.setCompleteness(count * 0.2);
        taskExecutionContext.setStatusMessage("The count is :"+count);

        System.out.println("-------------------（任务执行区）---------------------");

        // 为了获取执行器状态信息 增长了执行器执行任务的时间
        //try {
        //    Thread.sleep(3000L * 60L);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    @Override
    public boolean supportsCompletenessTracking() {
        return true;
    }

    @Override
    public boolean supportsStatusTracking() {
        return true;
    }

    @Override
    public boolean canBePaused() {
        return true;
    }

    @Override
    public boolean canBeStopped() {
        return true;
    }
}