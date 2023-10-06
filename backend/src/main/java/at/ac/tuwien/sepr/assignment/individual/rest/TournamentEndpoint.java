package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(path = TournamentEndpoint.BASE_PATH)
public class TournamentEndpoint {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  static final String BASE_PATH = "/tournaments";
  private final TournamentService service;

  public TournamentEndpoint(TournamentService service) {
    this.service = service;
  }

  @PostMapping
  public TournamentDetailDto create(@RequestBody TournamentDetailDto toCreate) throws ValidationException, NotFoundException {
    LOG.info("POST " + BASE_PATH + "/tournaments" + toCreate);
    LOG.debug("Body of request:\n{}", toCreate);
    return service.create(toCreate);
  }
}
