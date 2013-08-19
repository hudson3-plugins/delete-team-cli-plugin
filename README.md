delete-team-cli-plugin
======================

Adds delete-team and delete-job CLI commands.

delete-team
===========
Delete a single team. By default, all jobs in the team are made public. Use the -j
switch to delete the team jobs instead.

Help:
    Delete a team
      TEAM                : Team to delete
      -j (--jobs)         : Delete jobs (by default jobs are made public)


delete-job
==========
Delete one or more jobs whose names match a regex pattern. The pattern is implicitly
bounded by ^ and $, and must match the entire job name. For example, "A" will delete
only job A, but ".*A.*" will delete all jobs with a capital A in the name.

The optional TEAM argument restricts deleting to a single team. E.g., to delete
all jobs in in team MyTeam, <code>delete-job ".*" MyTeam</code>. This is more
reliable than deleting based on the team prefix in the job name, as '.' (dot) is
<i>not</i> a reserved character in job names. The may be a job <code>A.Job</code>
that is not in team A.

Help:
    Delete one or more jobs
      JOB                 : Job(s) to delete
    T EAM                : Only within team (optional)
