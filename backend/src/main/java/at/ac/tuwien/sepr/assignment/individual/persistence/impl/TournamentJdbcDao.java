package at.ac.tuwien.sepr.assignment.individual.persistence.impl;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseSelectionDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.entity.TournamentParticipant;
import at.ac.tuwien.sepr.assignment.individual.entity.TournamentStandings;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
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
import java.util.HashMap;
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
      + " (tournament_id, horse_id, round_reached, entry_number)"
      + " VALUES (?, ?, ?, ?)";
  private static final String SQL_SELECT_SEARCH_TOURNAMENT = "SELECT"
      + " t.id as \"id\", t.name as \"name\", t.start_date as \"start_date\", t.end_date as \"end_date\""
      + " FROM " + TOURNAMENT_TABLE_NAME + " t"
      + " WHERE (:startDate IS NULL OR :startDate <= t.start_date)"
      + " AND (:endDate IS NULL OR :endDate >= t.end_date)";
  private static final String SQL_GET_POINTS_FOR_PARTICIPANT = "SELECT "
      + "SUM(points) as total_points "
      + "FROM ("
      + "SELECT "
      + "horse_id, "
      + "CASE "
      + "WHEN round_reached = 2 THEN 1 "
      + "WHEN round_reached = 3 THEN 3 "
      + "WHEN round_reached = 4 THEN 5 "
      + "ELSE 0 "
      + "END as points "
      + "FROM ("
      + "SELECT "
      + "participation.horse_id, "
      + "participation.round_reached "
      + "FROM participation JOIN tournament ON participation.tournament_id = tournament.id "
      + "WHERE tournament.start_date >= DATE_TRUNC('year', CURRENT_DATE - INTERVAL '1' YEAR) AND participation.horse_id = ?"
      + ") as tournament_rounds"
      + ") as horse_points";
  private static final String SQL_SELECT_TOURNAMENT_PARTICIPANTS = "SELECT"
      + " p.horse_id as horse_id,"
      + " h.name as name,"
      + " h.date_of_birth as date_of_birth,"
      + " p.round_reached as round_reached,"
      + " p.entry_number as entry_number"
      + " FROM " + PARTICIPATION_TABLE_NAME + " p"
      + " JOIN horse h ON p.horse_id = h.id"
      + " WHERE p.tournament_id = ?";
  private static final String SQL_SELECT_TOURNAMENT_BY_ID = "SELECT"
      + " t.id as id, t.name as name, t.start_date as start_date, t.end_date as end_date"
      + " FROM " + TOURNAMENT_TABLE_NAME + " t"
      + " WHERE t.id = ?";
  private static final String SQL_UPDATE_PARTICIPANT = "UPDATE "
      + PARTICIPATION_TABLE_NAME
      + " SET round_reached = ?, entry_number = ?"
      + " WHERE horse_id = ? AND tournament_id = ?";

  public TournamentJdbcDao(
      NamedParameterJdbcTemplate jdbcNamed,
      JdbcTemplate jdbcTemplate) {
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
  public Tournament create(TournamentCreateDto tournament) throws NotFoundException {
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
        ps.setInt(3, 0);
        ps.setInt(4, -1);
        return ps;
      }, keyHolder1);

      long nextParticipantId = keyHolder1.getKey().longValue();
      newTournamentParticipants.add(new TournamentParticipant()
          .setId(nextParticipantId)
          .setName(participant.name())
          .setDateOfBirth(participant.dateOfBirth())
          .setRoundReached(0)
          .setEntryNumber(-1));
    }

    return new Tournament()
        .setId(newTournamentId)
        .setName(tournament.name())
        .setStartDate(tournament.startDate())
        .setEndDate(tournament.endDate())
        .setParticipants(newTournamentParticipants);
  }

  @Override
  public TournamentStandings generateFirstRound(long id) throws NotFoundException, FatalException {
    Tournament tournament = getById(id);
    List<TournamentParticipant> participants = tournament.getParticipants();
    List<TournamentParticipant> sortedParticipants = new ArrayList<>(participants);
    HashMap<TournamentParticipant, Long> points = new HashMap<>();
    for (TournamentParticipant participant : participants) {
      Long tournamentParticipantPoints = jdbcTemplate.queryForObject(SQL_GET_POINTS_FOR_PARTICIPANT, new Object[]{participant.getId()}, Long.class);
      points.put(participant, (tournamentParticipantPoints != null ? tournamentParticipantPoints : 0));
    }
    sortedParticipants.sort((p1, p2) -> {
      int pointsComparison = points.get(p2).compareTo(points.get(p1));
      if (pointsComparison != 0) {
        return pointsComparison;
      } else {
        return p1.getName().compareTo(p2.getName());
      }
    });
    List<TournamentParticipant> finalOrder = new ArrayList<>();
    int size = sortedParticipants.size();
    for (int i = 0; i < size / 2; i++) {
      finalOrder.add(sortedParticipants.get(i));
      finalOrder.add(sortedParticipants.get(size - i - 1));
    }
    for (TournamentParticipant participant : participants) {
      participant.setEntryNumber(finalOrder.indexOf(participant));
      participant.setRoundReached(1);
    }
    return new TournamentStandings(tournament);
  }

  @Override
  public Tournament getById(long id) throws NotFoundException, FatalException {
    LOG.trace("getById({})", id);
    List<Tournament> tournaments;
    tournaments = jdbcTemplate.query(SQL_SELECT_TOURNAMENT_BY_ID, this::mapRowTournament, id);
    List<TournamentParticipant> participants = new ArrayList<>(jdbcTemplate.query(SQL_SELECT_TOURNAMENT_PARTICIPANTS, this::mapRowParticipant, id));
    if (tournaments.isEmpty()) {
      throw new NotFoundException("No tournament with ID %d found".formatted(id));
    }
    if (tournaments.size() > 1) {
      throw new FatalException("Too many tournaments with ID %d found".formatted(id));
    }
    return tournaments.get(0).setParticipants(participants);
  }

  @Override
  public TournamentStandings setStandings(TournamentStandingsDto standingsToSet, long id) throws NotFoundException {
    Tournament tournament = getById(id);
    if (tournament == null) {
      throw new NotFoundException("Tournament with given id doesn't exist in the system.");
    }
    List<TournamentParticipant> participants = new ArrayList<>();
    for (TournamentParticipantDto participant : standingsToSet.getParticipants()) {
      jdbcTemplate.update(SQL_UPDATE_PARTICIPANT, participant.getRoundReached(), participant.getEntryNumber(), participant.getHorseId(), id);
      participants.add(new TournamentParticipant()
          .setId(participant.getHorseId())
          .setName(participant.getName())
          .setDateOfBirth(participant.getDateOfBirth())
          .setRoundReached(participant.getRoundReached())
          .setEntryNumber(standingsToSet.getParticipants().indexOf(participant)));
    }
    tournament.setParticipants(participants);
    return new TournamentStandings(tournament);
  }

  private Tournament mapRowTournament(ResultSet result, int rownum) throws SQLException {
    return new Tournament()
        .setId(result.getLong("id"))
        .setName(result.getString("name"))
        .setStartDate(result.getDate("start_date").toLocalDate())
        .setEndDate(result.getDate("end_date").toLocalDate());
  }

  private TournamentParticipant mapRowParticipant(ResultSet result, int rownum) throws SQLException {
    TournamentParticipant participant = new TournamentParticipant()
        .setId(result.getLong("horse_id"))
        .setName(result.getString("name"))
        .setDateOfBirth(result.getDate("date_of_birth").toLocalDate())
        .setRoundReached(result.getInt("round_reached"));
    int entryNumber = result.getInt("entry_number");
    if (result.wasNull()) {
      participant.setEntryNumber(-1);
    } else {
      participant.setEntryNumber(entryNumber);
    }
    return participant;
  }


}
