package ru.tsystems.tsproject.sbb.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;

import java.util.List;

/**
 * Created by apple on 11.11.14.
 */
public interface RouteEntryRepository extends JpaRepository<RouteEntry, Integer> {
    @Query("select r1 from Route route " +
            "join route.routeEntries r1 " +
            "join route.routeEntries r2 " +
            "where r1.station.name = :stationFrom and r2.station.name = :stationTo " +
            "and r1.route.id = r2.route.id and r1.seqNumber < r2.seqNumber")
    List<RouteEntry> findByStations(@Param("stationFrom") String stationFrom, @Param("stationTo") String stationTo);
    List<RouteEntry> findByStation_Name(String stationName);
    List<RouteEntry> findByStation_Id(int stationId);
    RouteEntry findByStation_NameAndRoute_Id(String stationName, int routeId);
    List<RouteEntry> findByRoute_Id(int routeId);

    @Query("select r from RouteEntry r where r.route.id = :routeId and r.seqNumber = " +
           "(select max(re.seqNumber) from RouteEntry re where re.route.id = :routeId)")
    RouteEntry findLastByRoute_Id(@Param("routeId") int routeId);
}
