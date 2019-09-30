package cz.jaroslavmedek.time_shifter;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimeShifterTest {

  private static TimeShifter timeShifter;

  @BeforeClass
  public static void initTimeShifter(){
    timeShifter = new TimeShifter();
  }

  @Test
  public void testToday(){
    String today = timeShifter.insertDate("<today # MM/dd/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().toString("MM/dd/yyyy"), today);
  }

  @Test
  public void testNow(){
    String now = timeShifter.insertDate("<now>", DateTime.now());
    Assert.assertEquals(DateTime.now().toString("yyyy-MM-dd"), now);
  }

  @Test
  public void testEndOfMonth() {
    String eom = timeShifter.insertDate("<endOfMonth # MM/dd/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().dayOfMonth().withMaximumValue().toString("MM/dd/yyyy"), eom);
  }

  @Test
  public void testEndOfMonthPlus5days() {
    String eom5days = timeShifter.insertDate("<endOfMonth + 5 days # dd/MM/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().dayOfMonth().withMaximumValue().plusDays(5).toString("dd/MM/yyyy"), eom5days);
  }

  @Test
  public void testTodayPlus14days() {
    String today14days = timeShifter.insertDate("<today + 14 days # MM/dd/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(14).toString("MM/dd/yyyy"), today14days);
  }

  @Test
  public void testNextMonthPlus5days() {
    String nextmonth5days = timeShifter.insertDate("<nextMonth - 5 days #MM/dd>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusMonths(1).minusDays(5).toString("MM/dd"), nextmonth5days);
  }

  @Test
  public void testTomorrowPlus14daysPlus2monthsPlus10years() {
    String tomorrow14days2months10years = timeShifter.insertDate("<tomorrow +14days+2months+10years>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(1).plusDays(14).plusMonths(2).plusYears(10).toString("yyyy-MM-dd"), tomorrow14days2months10years);
  }

  @Test
  public void testTomorrowPlus14daysMinus2monthsPlus10years() {
    String tomorrow14daysM2months10years = timeShifter.insertDate("<tomorrow +14days-2months+10years>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(1).plusDays(14).minusMonths(2).plusYears(10).toString("yyyy-MM-dd"), tomorrow14daysM2months10years);
  }

  @Test
  public void testTomorrowPlus14daysMinus2monthsPlus10yearsFormatted() {
    String tomorrow14daysM2months10yearsF = timeShifter.insertDate("<tomorrow +14days-2months+10years # dd/MM/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(1).plusDays(14).minusMonths(2).plusYears(10).toString("dd/MM/yyyy"), tomorrow14daysM2months10yearsF);
  }

  @Test
  public void testEndOfMonthPlus1month() {
    String eom1month = timeShifter.insertDate("<endOfMonth + 1 month # dd/MM/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().dayOfMonth().withMaximumValue().plusMonths(1).dayOfMonth().withMaximumValue().toString("dd/MM/yyyy"), eom1month);
  }

  @Test
  public void testTodayPlus5workdays() {
    String today5wd = timeShifter.insertDate("<today + 5 workDays # dd-MM>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(7).toString("dd-MM"), today5wd);
  }

  @Test
  public void testYearPlus2years(){
    String year2years = timeShifter.insertDate("<year + 2 years # yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusYears(2).toString("yyyy"), year2years);
  }
}
