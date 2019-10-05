package cz.jaroslavmedek.time_shifter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

public class TimeShifterTest {

  @Test
  public void testToday(){
    String today = TimeShifter.insertDate("<today # MM/dd/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().toString("MM/dd/yyyy"), today);
  }

  @Test
  public void testNow(){
    String now = TimeShifter.insertDate("<now>", DateTime.now());
    Assert.assertEquals(DateTime.now().toString("yyyy-MM-dd"), now);
  }

  @Test
  public void testTextAtEndYear(){
    String year = TimeShifter.insertDate("Upcoming year will be <now + 1 year # yyyy>", DateTime.now());
    Assert.assertEquals("Upcoming year will be " + DateTime.now().plusYears(1).toString("yyyy"), year);
  }

  @Test
  public void testTextInMiddleYear(){
    String middle = TimeShifter.insertDate("Loan was activated on <today - 10 days # dd/MM/yyyy> and first repayment will be <today + 4 days # dd/MM/yyyy>.", DateTime.now());
    Assert.assertEquals("Loan was activated on " + DateTime.now().minusDays(10).toString("dd/MM/yyyy")+ " and first repayment will be " + DateTime.now().plusDays(4).toString("dd/MM/yyyy") + ".", middle);
  }

  @Test
  public void testEndOfMonth() {
    String eom = TimeShifter.insertDate("<endOfMonth # MM/dd/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().dayOfMonth().withMaximumValue().toString("MM/dd/yyyy"), eom);
  }

  @Test
  public void testEndOfMonthPlus5days() {
    String eom5days = TimeShifter.insertDate("<endOfMonth + 5 days # dd/MM/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().dayOfMonth().withMaximumValue().plusDays(5).toString("dd/MM/yyyy"), eom5days);
  }

  @Test
  public void testTodayPlus14days() {
    String today14days = TimeShifter.insertDate("<today + 14 days # MM/dd/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(14).toString("MM/dd/yyyy"), today14days);
  }

  @Test
  public void testNextMonthPlus5days() {
    String nextmonth5days = TimeShifter.insertDate("<nextMonth - 5 days #MM/dd>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusMonths(1).minusDays(5).toString("MM/dd"), nextmonth5days);
  }

  @Test
  public void testTomorrowPlus14daysPlus2monthsPlus10years() {
    String tomorrow14days2months10years = TimeShifter.insertDate("<tomorrow +14days+2months+10years>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(1).plusDays(14).plusMonths(2).plusYears(10).toString("yyyy-MM-dd"), tomorrow14days2months10years);
  }

  @Test
  public void testTomorrowPlus14daysMinus2monthsPlus10years() {
    String tomorrow14daysM2months10years = TimeShifter.insertDate("<tomorrow +14days-2months+10years>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(1).plusDays(14).minusMonths(2).plusYears(10).toString("yyyy-MM-dd"), tomorrow14daysM2months10years);
  }

  @Test
  public void testTomorrowPlus14daysMinus2monthsPlus10yearsFormatted() {
    String tomorrow14daysM2months10yearsF = TimeShifter.insertDate("<tomorrow +14days-2months+10years # dd/MM/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusDays(1).plusDays(14).minusMonths(2).plusYears(10).toString("dd/MM/yyyy"), tomorrow14daysM2months10yearsF);
  }

  @Test
  public void testEndOfMonthPlus1month() {
    String eom1month = TimeShifter.insertDate("<endOfMonth + 1 month # dd/MM/yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().dayOfMonth().withMaximumValue().plusMonths(1).dayOfMonth().withMaximumValue().toString("dd/MM/yyyy"), eom1month);
  }

  @Test
  public void testTodayPlus5workdays() {
    DateTime startDate = DateTime.parse("01/01/2020", DateTimeFormat.forPattern("dd/MM/yyyy"));
    String today5wd = TimeShifter.insertDate("<today + 5 workDays # dd-MM>", startDate);
    Assert.assertEquals(startDate.plusDays(7).toString("dd-MM"), today5wd);
  }

  @Test
  public void testYearPlus2years(){
    String year2years = TimeShifter.insertDate("<year + 2 years # yyyy>", DateTime.now());
    Assert.assertEquals(DateTime.now().plusYears(2).toString("yyyy"), year2years);
  }

  @Test
  public void testYearPlus3yearsWithDefaultDate(){
    String year3years = TimeShifter.insertDate("<year + 3 years # yyyy>");
    Assert.assertEquals(DateTime.now().plusYears(3).toString("yyyy"), year3years);
  }

  @Test
  public void testTodayPlus1hour(){
    DateTime now = DateTime.now();
    String today1hour = TimeShifter.insertDate("<now + 1 hour # yy-MM-dd-hh>", now);
    Assert.assertEquals(now.plusHours(1).toString("yy-MM-dd-hh"), today1hour);
  }

  @Test
  public void testTodayMinus3hours(){
    DateTime now = DateTime.now();
    String todayM3hours = TimeShifter.insertDate("<now - 3 hours # yy-MM-dd-hh>", now);
    Assert.assertEquals(now.minusHours(3).toString("yy-MM-dd-hh"), todayM3hours);
  }

  @Test
  public void testTodayPlus1second(){
    DateTime now = DateTime.now();
    String today1second = TimeShifter.insertDate("<today + 1 seconds # dd-hh:mm:ss>", now);
    Assert.assertEquals(now.plusSeconds(1).toString("dd-hh:mm:ss"), today1second);
  }

  @Test
  public void testTodayPlus10seconds(){
    DateTime now = DateTime.now();
    String today10seconds = TimeShifter.insertDate("<today + 10 seconds # dd-hh:mm:ss>", now);
    Assert.assertEquals(now.plusSeconds(10).toString("dd-hh:mm:ss"), today10seconds);
  }
}
