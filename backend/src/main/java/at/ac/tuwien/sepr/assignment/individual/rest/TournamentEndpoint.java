package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.exception.ValidationException;
import at.ac.tuwien.sepr.assignment.individual.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;


@RestController
@RequestMapping(path = TournamentEndpoint.BASE_PATH)
public class TournamentEndpoint {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  static final String BASE_PATH = "/tournaments";
  private final TournamentService service;

  public TournamentEndpoint(TournamentService service) {
    this.service = service;
  }

  @GetMapping
  public Stream<TournamentListDto> searchTournaments(TournamentSearchDto searchParameters) {
    LOG.info("GET " + BASE_PATH);
    LOG.debug("request parameters: {}", searchParameters);
    return service.search(searchParameters);
  }

  @PostMapping
  public TournamentDetailDto create(@RequestBody TournamentCreateDto toCreate) throws ValidationException, NotFoundException {
    LOG.info("POST " + BASE_PATH + "/tournaments" + toCreate);
    LOG.debug("Body of request:\n{}", toCreate);
    return service.create(toCreate);
  }

  @GetMapping("/standings/generate/{id}")
  public TournamentStandingsDto generateFirstRound(@PathVariable Long id) throws NotFoundException, FatalException {
    LOG.info("GET " + BASE_PATH + "/standings/generate/" + id);
    LOG.debug("request parameters: {}", id);
    return service.generateFirstRound(id);
  }

  @GetMapping("/standings/{id}")
  public TournamentStandingsDto getStandings(@PathVariable Long id) throws NotFoundException, FatalException {
    LOG.info("GET " + BASE_PATH + "/standings/" + id);
    LOG.debug("request parameters: {}", id);
    return service.getStandings(id);
  }

  @PutMapping("/standings/{id}")
  public TournamentStandingsDto setStandings(@RequestBody TournamentStandingsDto standingsToSet, @PathVariable Long id)
      throws ValidationException, NotFoundException {
    LOG.info("PUT " + BASE_PATH + "/standings/" + id);
    LOG.debug("Body of request: \n{}", standingsToSet);
    return service.setStandings(standingsToSet, id);
  }


}
