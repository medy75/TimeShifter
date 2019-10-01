package cz.jaroslavmedek.time_shifter;

import org.joda.time.DateTime;

public interface ITimeShifter {
  String insertDate(String textWithPlaceholder);
  String insertDate(String textWithPlaceholder, DateTime dateTime);
}
