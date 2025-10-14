package io.github.malczuuu.ushadow.rest.error;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemBuilder;
import io.github.malczuuu.problem4j.core.ProblemStatus;
import io.github.malczuuu.problem4j.spring.web.context.ProblemContext;
import io.github.malczuuu.problem4j.spring.web.resolver.AbstractProblemResolver;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockingFailureResolver extends AbstractProblemResolver {

  public OptimisticLockingFailureResolver() {
    super(OptimisticLockingFailureException.class);
  }

  @Override
  public ProblemBuilder resolveBuilder(
      ProblemContext context, Exception ex, HttpHeaders headers, HttpStatusCode status) {
    return Problem.builder().status(ProblemStatus.CONFLICT).detail("version mismatch");
  }
}
