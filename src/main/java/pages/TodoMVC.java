package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static helpers.Helpers.doubleClick;
import static pages.TodoMVC.TaskType.ACTIVE;
/**
 * Created by 64 on 31.03.2016.
 */
public class TodoMVC {

    public static ElementsCollection tasks = $$("#todo-list>li");
    public static SelenideElement newTask = $("#new-todo");

    public enum TaskType {
        ACTIVE, COMPLETED
    }

    public enum Filter {

        ALL(""),
        ACTIVE("active"),
        COMPLETED("completed");

        private String url;

        Filter(String url) {
            this.url = url;
        }

        public String getURL() {
            return "http://todomvc4tasj.herokuapp.com/#/" + url;
        }

    }

    public static class Task {
        TaskType taskType;
        String taskText;


        public Task(TaskType taskType, String taskText) {
            this.taskType = taskType;
            this.taskText = taskText;
        }
    }

    public static Task aTask(TaskType taskType, String taskText) {
        Task task = new Task(taskType, taskText);
        return task;
    }

    public static void given(Filter filter, Task... tasks) {

        if (!url().equals(filter.getURL())) {
            open(filter.getURL());
        }

        String result = "";

        for (Task task : tasks) {
            result = result + "{\\\"completed\\\":" + (task.taskType == ACTIVE ? "false" : "true") +
                    ", \\\"title\\\":\\\"" + task.taskText + "\\\"},";

        }
        if (tasks.length > 0) {
            result = result.substring(0, result.length() - 1);
        }

        String JS = "localStorage.setItem(\"todos-troopjs\", \"[" + result + "]\")";

        executeJavaScript(JS);
        executeJavaScript("location.reload()");

    }

    public static Task[] getTaskArray(TaskType taskType, String... taskTexts) {

        Task[] tasks = new Task[taskTexts.length];
        for (int i = 0; i < taskTexts.length; i++) {
            tasks[i] = aTask(taskType, taskTexts[i]);
        }
        return (tasks);
    }

    public static void givenAtAll(Task... tasks) {
        given(Filter.ALL, tasks);
    }

    public static void givenAtActive(Task... tasks) {
        given(Filter.ACTIVE, tasks);
    }

    public static void givenAtCompleted(Task... tasks) {
        given(Filter.COMPLETED, tasks);
    }

    public static void givenAtAll(TaskType taskType, String... taskTexts) {
        given(Filter.ALL, getTaskArray(taskType, taskTexts));

    }

    public static void givenAtActive(TaskType taskType, String... taskTexts) {
        given(Filter.ACTIVE, getTaskArray(taskType, taskTexts));

    }

    public static void givenAtCompleted(TaskType taskType, String... taskTexts) {
        given(Filter.COMPLETED, getTaskArray(taskType, taskTexts));

    }

    @Step
    public static void assertItemsLeft(Integer count) {
        $("#todo-count>strong").shouldHave(exactText(String.valueOf(count)));
    }

    @Step
    public static void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public static SelenideElement startEdit(String oldTaskText, String newTaskText) {
        doubleClick(tasks.find(exactText(oldTaskText)).find("label"));

        return tasks.find(cssClass("editing")).find(".edit").val(newTaskText);
    }

    @Step
    public static void filterAll() {
        $(By.linkText("All")).click();
    }

    @Step
    public static void filterActive() {
        $(By.linkText("Active")).click();
    }

    @Step
    public static void filterCompleted() {
        $(By.linkText("Completed")).click();
    }

    @Step
    public static void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    @Step
    public static void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldNotBe(visible);
    }

    @Step
    public static void delete(String taskText) {
        tasks.find(exactText(taskText)).hover();
        tasks.find(exactText(taskText)).$(".destroy").click();
    }

    @Step
    public static void add(String... taskTexts) {
        for (String text : taskTexts) {
            newTask.setValue(text).pressEnter();
        }
    }

    @Step
    public static void assertTasks(String... taskTexts) {
        tasks.shouldHave(exactTexts(taskTexts));
    }

    @Step
    public static void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    @Step
    public static void assertVisibleTasks(String... taskTexts) {
        tasks.filter(visible).shouldHave(exactTexts(taskTexts));
    }

    @Step
    public static void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

}