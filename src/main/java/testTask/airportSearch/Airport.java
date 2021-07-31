package testTask.airportSearch;


import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Comparator;


public class Airport implements Comparable<Airport>{

    private static int filterField = 0;

    public String id;
    public String name;
    public String city;
    public String country;
    public String iataAirportCode;
    public String icaoAirportCode;
    public Double latitude;
    public Double longitude;
    public Integer height;
    public Double timeZone;
    public Dst dst;
    public String timeZoneName;
    public Type type;
    public Source source;


    public static Comparator<? super Airport> setFilterField(int newFilterField){
        filterField = newFilterField;
        return null;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", iataAirportCode='" + iataAirportCode + '\'' +
                ", icaoAirportCode='" + icaoAirportCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", timeZone=" + timeZone +
                ", dst='" + dst + '\'' +
                ", timeZoneName='" + timeZoneName + '\'' +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public String get(){
        switch (filterField){
            case 0:
                return this.id.toLowerCase();
            case 1:
                return this.name.toLowerCase();
            case 2:
                return this.city.toLowerCase();
            case 3:
                return this.country.toLowerCase();
            case 4:
                return this.iataAirportCode.toLowerCase();
            case 5:
                return this.icaoAirportCode.toLowerCase();
            case 6:
                return this.latitude.toString().toLowerCase();
            case 7:
                return this.longitude.toString().toLowerCase();
            case 8:
                return this.height.toString().toLowerCase();
            case 9:
                return this.timeZone.toString().toLowerCase();
            case 10:
                return this.dst.toString().toLowerCase();
            case 11:
                return this.timeZoneName.toLowerCase();
            case 12:
                return this.type.toString().toLowerCase();
            case 13:
                return this.source.toString().toLowerCase();
            default:
                throw new NotImplementedException();
        }
    }

    @Override
    public int compareTo(Airport o) {
        switch (filterField){
            case 0:
                return this.id.compareTo(o.id);
            case 1:
                return this.name.compareTo(o.name);
            case 2:
                return this.city.compareTo(o.city);
            case 3:
                return this.country.compareTo(o.country);
            case 4:
                return this.iataAirportCode.compareTo(o.iataAirportCode);
            case 5:
                return this.icaoAirportCode.compareTo(o.icaoAirportCode);
            case 6:
                return this.latitude.compareTo(o.latitude);
            case 7:
                return this.longitude.compareTo(o.longitude);
            case 8:
                return this.height.compareTo(o.height);
            case 9:
                return this.timeZone.compareTo(o.timeZone);
            case 10:
                return this.dst.compareTo(o.dst);
            case 11:
                return this.timeZoneName.compareTo(o.timeZoneName);
            case 12:
                return this.type.compareTo(o.type);
            case 13:
                return this.source.compareTo(o.source);
            default:
                throw new NotImplementedException();
        }
    }
}
