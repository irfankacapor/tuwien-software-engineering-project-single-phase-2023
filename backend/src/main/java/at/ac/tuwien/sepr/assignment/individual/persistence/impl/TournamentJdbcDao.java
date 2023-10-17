package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseSelectionDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.entity.TournamentParticipant;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.invoke.MethodHandles;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class TournamentJdbcDao implements TournamentDao {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate jdbcNamed;
  private static final String TOURNAMENT_TABLE_NAME = "tournament";
  private static final String PARTICIPATION_TABLE_NAME = "participation";
  private static final String SQL_INSERT_INTO_TOURNAMENT = "INSERT INTO " + TOURNAMENT_TABLE_NAME
      + " (name, start_date, end_date)"
      + " VALUES (?, ?, ?)";
  private static final String SQL_INSERT_INTO_PARTICIPATION = "INSERT INTO " + PARTICIPATION_TABLE_NAME
      + " (tournament_id, horse_id, round_reached)"
      + " VALUES (?, ?, ?)";

  private static final String SQL_SELECT_SEARCH_TOURNAMENT = "SELECT"
      + " t.id as \"id\", t.name as \"name\", t.start_date as \"start_date\", t.end_date as \"end_date\""
      + " FROM " + TOURNAMENT_TABLE_NAME + " t"
      + " WHERE (:startDate IS NULL OR :startDate <= t.start_date)"
      + " AND (:endDate IS NULL OR :endDate >= t.end_date)";

  public TournamentJdbcDao(
      NamedParameterJdbcTemplate jdbcNamed,
      JdbcTemplate jdbcTemplate,
      HorseJdbcDao horseJdbcDao) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcNamed = jdbcNamed;
  }
  @Override
  public Collection<Tournament> search(TournamentSearchDto searchParameters) {
    LOG.trace("search({})", searchParameters);
    var query = SQL_SELECT_SEARCH_TOURNAMENT;
    var params = new BeanPropertySqlParameterSource(searchParameters);
    return jdbcNamed.query(query, params, this::mapRowTournament);
  }

  @Override
  public Tournament create(TournamentDetailDto tournament) throws NotFoundException {
    LOG.trace("create({})", tournament);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(SQL_INSERT_INTO_TOURNAMENT, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, tournament.name());
      ps.setDate(2, java.sql.Date.valueOf(tournament.startDate()));
      ps.setDate(3, java.sql.Date.valueOf(tournament.endDate()));
      return ps;
    }, keyHolder);

    long newTournamentId = keyHolder.getKey().longValue();
    KeyHolder keyHolder1 = new GeneratedKeyHolder();
    List<TournamentParticipant> newTournamentParticipants = new ArrayList<>();

    for (HorseSelectionDto participant : tournament.participants()) {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(SQL_INSERT_INTO_PARTICIPATION, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, newTournamentId);
        ps.setLong(2, participant.id());
        ps.setLong(3, 0);
        return ps;
      }, keyHolder1);

      long nextParticipantId = keyHolder1.getKey().longValue();
      newTournamentParticipants.add(new TournamentParticipant()
          .setId(nextParticipantId)
          .setName(participant.name())
          .setDateOfBirth(participant.dateOfBirth())
          .setRoundReached(0));
    }

    return new Tournament()
        .setId(newTournamentId)
        .setName(tournament.name())
        .setStartDate(tournament.startDate())
        .setEndDate(tournament.endDate())
        .setParticipants(newTournamentParticipants);
  }

  private Tournament mapRowTournament(ResultSet result, int rownum) throws SQLException {
    return new Tournament()
        .setId(result.getLong("id"))
        .setName(result.getString("name"))
        .setStartDate(result.getDate("start_date").toLocalDate())
        .setEndDate(result.getDate("end_date").toLocalDate());
  }


}
