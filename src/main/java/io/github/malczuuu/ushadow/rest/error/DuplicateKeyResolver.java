package io.github.malczuuu.ushadow.rest.error;

import io.github.malczuuu.problem4j.core.Problem;
import io.github.malczuuu.problem4j.core.ProblemBuilder;
import io.github.malczuuu.problem4j.core.ProblemStatus;
import io.github.malczuuu.problem4j.spring.web.context.ProblemContext;
import io.github.malczuuu.problem4j.spring.web.resolver.AbstractProblemResolver;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class DuplicateKeyResolver extends AbstractProblemResolver {

  public DuplicateKeyResolver() {
    super(DuplicateKeyException.class);
  }

  @Override
  public ProblemBuilder resolveBuilder(
      ProblemContext context, Exception ex, HttpHeaders headers, HttpStatusCode status) {
    DuplicateKeyException e = (DuplicateKeyException) ex;
    String message = e.getMessage();

    String collection = null;
    String index = null;

    Pattern pattern =
        Pattern.compile("duplicate key error collection: \\w+\\.(\\w+) index: (\\w+)");
    Matcher matcher = pattern.matcher(message);
    if (matcher.find()) {
      collection = matcher.group(1);
      index = matcher.group(2);
    }

    ProblemBuilder builder =
        Problem.builder().status(ProblemStatus.CONFLICT).detail("duplicate resource");

    if (collection != null) {
      builder =
          switch (collection) {
            case "things" -> builder.extension("resource", "thing");
            default -> builder;
          };
    }
    if (index != null) {
      builder =
          switch (index) {
            case "uid_unique" -> builder.extension("field", "uid");
            default -> builder;
          };
    }

    return builder;
  }
}
