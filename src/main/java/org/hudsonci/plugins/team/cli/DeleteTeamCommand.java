/*
 * Copyright (c) 2013 Hudson.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Hudson - initial API and implementation and/or initial documentation
 */

package org.hudsonci.plugins.team.cli;

import hudson.Extension;
import hudson.cli.CLICommand;
import hudson.model.Hudson;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import java.util.Set;
import org.eclipse.hudson.security.team.Team;
import org.eclipse.hudson.security.team.TeamManager;
import org.eclipse.hudson.security.team.TeamManager.TeamNotFoundException;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Delete a team from the command line. User must be sys admin.
 * @author Bob Foster
 */
@Extension
public class DeleteTeamCommand extends CLICommand {

    @Override
    public String getShortDescription() {
        return "Delete a team";
    }
    @Argument(metaVar = "TEAM", usage = "Team to delete", required=true)
    public String team;
    @Option(name = "-j", aliases = {"--jobs"}, usage = "Delete jobs (by default jobs are made public)")
    public boolean jobs;

    protected int run() throws Exception {
        Hudson h = Hudson.getInstance();
        
        if (!h.isTeamManagementEnabled()) {
            stderr.println("Team management is not enabled");
            return -1;
        }
        
        TeamManager teamManager = h.getTeamManager();
        
        if (!teamManager.isCurrentUserSysAdmin()) {
            stderr.println("User not authorized to create team");
            return -1;
        }
        
        Team targetTeam;
        try {
            targetTeam = teamManager.findTeam(team);
        } catch (TeamNotFoundException e) {
            stderr.println("Team "+team+" not found");
            return -1;
        }
        
        if (jobs) {
            Set<String> jobNames = targetTeam.getJobNames();
            for (String job : jobNames) {
                TopLevelItem item = h.getItem(job);
                if (item != null && item instanceof Job) {
                    item.delete();
                } else {
                    stderr.println("Team "+team+" job "+job+" not found");
                }
            }
        }
        
        teamManager.deleteTeam(team);
        
        return 0;
    }
}
