package hudson.plugins.batch_task;

import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.util.List;

/**
 * Shows all the tasks.
 *
 * @author Kohsuke Kawaguchi
 */
public class BatchTaskAction implements Action {
    public final AbstractProject<?,?> project;
    public final BatchTaskProperty property;

    public BatchTaskAction(AbstractProject<?, ?> project, BatchTaskProperty property) {
        this.project = project;
        this.property = property;
    }

    public List<BatchTask> getTasks() {
        return property.getTasks();
    }

    public String getIconFileName() {
        List<BatchTask> tasks = property.getTasks();
        if (tasks.isEmpty()) return null;
        // Show animated ball icon if a task is running now
        Run run = project.getLastBuild();
        BatchRunAction bra = run != null ? run.getAction(BatchRunAction.class) : null;
        if (bra != null)
            for (BatchRun br : bra.records)
                if (br.isRunning())
                    return br.getBuildStatusUrl();
        return "gear2.gif";
    }

    public String getDisplayName() {
        return Messages.BatchTaskAction_DisplayName(property.getTasks().size());
    }

    public String getUrlName() {
        return "batchTasks";
    }

    public BatchTask getDynamic(String name, StaplerRequest req, StaplerResponse rsp) {
        return getTask(name);
    }

    public BatchTask getTask(String name) {
        return property.getTask(name);
    }
}
