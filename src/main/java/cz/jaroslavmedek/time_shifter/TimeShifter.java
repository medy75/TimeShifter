package cz.jaroslavmedek.time_shifter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;

public class TimeShifter {

  private static final Logger LOGGER = LogManager.getLogger(TimeShifter.class);

  private static final int DAYS_IN_WEEK = 7;
  private static final String DATE_WITH_DELIMITER = "(?=[\\+-])";
  private static final String FORMAT_DELIMITER = "#";
  private static final String DATE_PATTERN = "<(today|now|week|month|year|tomorrow|yesterday|nextWeek|endOfMonth|nextMonth|startOfMonth|nextYear)((\\s?[\\+-]\\s?\\d+\\s?\\w+\\s?)*)(\\s?[#-]\\s?.*)?>";

  private String dateFormat = "yyyy-MM-dd";

  public static void main(String[] args) {
    TimeShifter tf = new TimeShifter();
    System.out.println(tf.insertDate("<today>"));
  }


  public String insertDate(String inputText) {
    return insertDate(inputText, DateTime.now());
  }

  public String insertDate(String inputText, DateTime actualDate) {
    Pattern formatPattern = Pattern.compile(DATE_PATTERN);
    Matcher formatMatcher = formatPattern.matcher(inputText);
    if (formatMatcher.find()) {
      return replaceDateStamp(inputText, actualDate);
    } else {
      return inputText;
    }
  }

  private String replaceDateStamp(String inputText, DateTime actualDate) {
    LOGGER.debug("Replace date (actual: {}) in string: {}", actualDate.toString(), inputText);
    String withoutParentheses = inputText.substring(inputText.indexOf('<') + 1, inputText.indexOf('>'));
    String replaceable = inputText.substring(inputText.indexOf('<'), inputText.indexOf('>') + 1);

    String[] splitByFormatter = withoutParentheses.split(FORMAT_DELIMITER);
    String[] dateValues = splitByFormatter[0].split(DATE_WITH_DELIMITER);

    DateTime modifiedDate;
    if (isEndOfMonth(dateValues) && !isCountInDays(dateValues)) {
      modifiedDate = calcDate(dateValues, actualDate).dayOfMonth().withMaximumValue();
    } else {
      modifiedDate = calcDate(dateValues, actualDate);
    }
    LOGGER.debug("Modified date (without format): {}", modifiedDate.toString());
    return inputText.replace(replaceable, modifiedDate.toString(getFormatter(splitByFormatter)));
  }

  private boolean isEndOfMonth(String[] dateValues) {
    return "endofmonth".equalsIgnoreCase(dateValues[0].trim());
  }

  private boolean isCountInDays(String[] dateValues) {
    for (int i = 1; i < dateValues.length; i++) {
      if (dateValues[i].trim().contains("day")) {
        return true;
      }
    }
    return false;
  }

  private DateTime calcDate(String[] dateValuesParam, DateTime actualDateParam) {
    DateTime actualDate = actualDateParam;
    actualDate = replaceFirstVal(dateValuesParam[0], actualDate);
    String[] dateValues = Arrays.copyOfRange(dateValuesParam, 1, dateValuesParam.length);
    for (String value : dateValues) {
      value = value.trim();
      int count = getCount(value);
      String periodName = getPeriodName(value);
      if (value.startsWith("-")) {
        actualDate = actualDate.minus(getPeriod(count, periodName, actualDate));
      } else {
        actualDate = actualDate.plus(getPeriod(count, periodName, actualDate));
      }
    }
    return actualDate;
  }

  private int getCount(String value) {
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(value);
    if (matcher.find()) {
      return Integer.parseInt(matcher.group(0));
    }
    return 0;
  }

  private String getPeriodName(String value) {
    Pattern pattern = Pattern.compile("[a-zA-Z]+");
    Matcher matcher = pattern.matcher(value);
    if (matcher.find()) {
      return matcher.group(0);
    }
    return "day";
  }

  private String getFormatter(String[] splittedValues) {
    return splittedValues.length > 1 ? splittedValues[1].trim() : dateFormat;
  }

  private DateTime replaceFirstVal(String value, DateTime actualDate) {
    DateTime date;
    switch (value.trim().toLowerCase()) {
      case "yesterday":
        date = actualDate.minusDays(1);
        break;
      case "tomorrow":
        date = actualDate.plusDays(1);
        break;
      case "nextweek":
        date = actualDate.plusDays(DAYS_IN_WEEK);
        break;
      case "endofmonth":
        date = actualDate.dayOfMonth().withMaximumValue();
        break;
      case "nextmonth":
        date = actualDate.plusMonths(1);
        break;
      case "startofmonth":
        date = actualDate.withDayOfMonth(1);
        break;
      case "nextyear":
        date = actualDate.plusYears(1);
        break;
      default:
        date = actualDate;
    }
    return date;
  }

  private Period getPeriod(int value, String period, DateTime actualDate) {
    Period p;
    switch (period.toLowerCase()) {
      case "day":
      case "days":
        p = Period.days(value);
        break;
      case "week":
      case "weeks":
        p = Period.days(value * DAYS_IN_WEEK);
        break;
      case "month":
      case "months":
        p = Period.months(value);
        break;
      case "year":
      case "years":
        p = Period.years(value);
        break;
      case "workday":
      case "workdays":
        p = calcWorkDays(value, actualDate);
        break;
      default:
        p = Period.days(value);
    }
    return p;
  }

  private Period calcWorkDays(int valueParam, DateTime actualDateParam) {
    int value = valueParam;
    LOGGER.debug("Calc working days - wanted working days: {} from date: {}", value, actualDateParam.toString());
    DateTime actualDate = actualDateParam;
    int days = 0;
    for (int i = 1; i <= value; i++) {
      days++;
      actualDate = actualDate.plusDays(1);
      if (actualDate.dayOfWeek().get() == 6 || actualDate.dayOfWeek().get() == 7) {
        value++;
      }
    }
    LOGGER.debug("Number of calculated days (period): {}", days);
    return Period.days(days);
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }
}