package com.boj.route;

import com.boj.annotation.IsAdmin;
import com.boj.base.BojErrorType;
import com.boj.problem.ProblemManager;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.inject.Inject;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Created by biran on 10/17/16.
 */
@Singleton
public class CreateOrUpdateProblemRoute implements Route {

  private final ProblemManager problemManager;
  private final Provider<Boolean> isAdmin;


  @Inject
  public CreateOrUpdateProblemRoute(ProblemManager problemManager,
                                    @IsAdmin Provider<Boolean> isAdmin) {
    this.problemManager = problemManager;
    this.isAdmin = isAdmin;
  }

  @Override
  public Object handle(Request req, Response resp) throws Exception {
    if (!isAdmin.get()) {
      throw BojErrorType.NO_PERMISSION.exception();
    }
    String title = req.queryParams("title");
    String desc = req.queryParams("description");
    String template = req.queryParams("template");
    String test = req.queryParams("test");
    String problemId = req.queryParams("id");
    if (Strings.isNullOrEmpty(problemId)) {
      problemManager.createProblem(title, desc, template, test);
    } else {
      int id = Integer.valueOf(problemId);
      problemManager.updateProblem(id, title, desc, template, test);
    }
    resp.redirect("/problems");
    return null;
  }
}
