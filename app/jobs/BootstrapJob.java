package jobs;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

//@OnApplicationStart
public class BootstrapJob extends Job {

    @Override
    public void doJob() throws Exception {
        Fixtures.deleteDatabase();
        Fixtures.loadModels("data/permissions.yml", "data/initial-data.yml");
    }
}
