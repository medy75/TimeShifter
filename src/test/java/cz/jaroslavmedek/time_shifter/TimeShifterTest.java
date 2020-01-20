package cz.jaroslavmedek.time_shifter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

public class TimeShifterTest {

  private DateTimeFormatter getFormatter(String format){
    return DateTimeFormatter.ofPattern(format);
  }

  @Test
  public void testToday(){
    String today = TimeShifter.insertDate("<today # MM/dd/yyyy>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().format(getFormatter("MM/dd/yyyy")), today);
  }

  @Test
  public void testNow(){
    String now = TimeShifter.insertDate("<now>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().format(getFormatter("yyyy-MM-dd")), now);
  }

  @Test
  public void testTextAtEndYear(){
    String year = TimeShifter.insertDate("Upcoming year will be <now + 1 year # yyyy>", LocalDateTime.now());
    Assert.assertEquals("Upcoming year will be " + LocalDateTime.now().plusYears(1).format(getFormatter("yyyy")), year);
  }

  @Test
  public void testTextInMiddleYear(){
    String middle = TimeShifter.insertDate("Loan was activated on <today - 10 days # dd/MM/yyyy> and first repayment will be <today + 4 days # dd/MM/yyyy>.", LocalDateTime.now());
    Assert.assertEquals("Loan was activated on " + LocalDateTime.now().minusDays(10).format(getFormatter("dd/MM/yyyy")) + " and first repayment will be " + LocalDateTime.now().plusDays(4).format(getFormatter("dd/MM/yyyy")) + ".", middle);
  }

  @Test
  public void testEndOfMonth() {
    String eom = TimeShifter.insertDate("<endOfMonth # MM/dd/yyyy>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).format(getFormatter("MM/dd/yyyy")), eom);
  }

  @Test
  public void testEndOfMonthPlus5days() {
    String eom5days = TimeShifter.insertDate("<endOfMonth + 5 days # dd/MM/yyyy>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).plusDays(5).format(getFormatter("dd/MM/yyyy")), eom5days);
  }

  @Test
  public void testTodayPlus14days() {
    String today14days = TimeShifter.insertDate("<today + 14 days # MM/dd/yyyy>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().plusDays(14).format(getFormatter("MM/dd/yyyy")), today14days);
  }

  @Test
  public void testNextMonthPlus5days() {
    String nextmonth5days = TimeShifter.insertDate("<nextMonth - 5 days #MM/dd>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().plusMonths(1).minusDays(5).format(getFormatter("MM/dd")), nextmonth5days);
  }

  @Test
  public void testTomorrowPlus14daysPlus2monthsPlus10years() {
    String tomorrow14days2months10years = TimeShifter.insertDate("<tomorrow +14days+2months+10years>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().plusDays(1).plusDays(14).plusMonths(2).plusYears(10).format(getFormatter("yyyy-MM-dd")), tomorrow14days2months10years);
  }

  @Test
  public void testTomorrowPlus14daysMinus2monthsPlus10years() {
    String tomorrow14daysM2months10years = TimeShifter.insertDate("<tomorrow +14days-2months+10years>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().plusDays(1).plusDays(14).minusMonths(2).plusYears(10).format(getFormatter("yyyy-MM-dd")), tomorrow14daysM2months10years);
  }

  @Test
  public void testTomorrowPlus14daysMinus2monthsPlus10yearsFormatted() {
    String tomorrow14daysM2months10yearsF = TimeShifter.insertDate("<tomorrow +14days-2months+10years # dd/MM/yyyy>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().plusDays(1).plusDays(14).minusMonths(2).plusYears(10).format(getFormatter("dd/MM/yyyy")), tomorrow14daysM2months10yearsF);
  }

  @Test
  public void testEndOfMonthPlus1month() {
    String eom1month = TimeShifter.insertDate("<endOfMonth + 1 month # dd/MM/yyyy>", LocalDateTime.now());
    LocalDateTime semi = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).plusMonths(1);
    Assert.assertEquals(semi.withDayOfMonth(semi.toLocalDate().lengthOfMonth()).format(getFormatter("dd/MM/yyyy")), eom1month);
  }

  @Test
  public void testTodayPlus5workdays() {
    LocalDateTime startDate = LocalDateTime.parse("2020/01/01 12:00:00", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    String today5wd = TimeShifter.insertDate("<today + 5 workDays # dd-MM>", startDate);
    Assert.assertEquals(startDate.plusDays(7).format(getFormatter("dd-MM")), today5wd);
  }

  @Test
  public void testYearPlus2years(){
    String year2years = TimeShifter.insertDate("<year + 2 years # yyyy>", LocalDateTime.now());
    Assert.assertEquals(LocalDateTime.now().plusYears(2).format(getFormatter("yyyy")), year2years);
  }

  @Test
  public void testYearPlus3yearsWithDefaultDate(){
    String year3years = TimeShifter.insertDate("<year + 3 years # yyyy>");
    Assert.assertEquals(LocalDateTime.now().plusYears(3).format(getFormatter("yyyy")), year3years);
  }

//  @Test
//  public void testTodayPlus1hour(){
//    LocalDateTime now = LocalDateTime.now();
//    String today1hour = TimeShifter.insertDate("<now + 1 hour # yy-MM-dd-hh>", now);
//    Assert.assertEquals(now.plusHours(1).toString("yy-MM-dd-hh"), today1hour);
//  }
//
//  @Test
//  public void testTodayMinus3hours(){
//    LocalDateTime now = LocalDateTime.now();
//    String todayM3hours = TimeShifter.insertDate("<now - 3 hours # yy-MM-dd-hh>", now);
//    Assert.assertEquals(now.minusHours(3).toString("yy-MM-dd-hh"), todayM3hours);
//  }
//
//  @Test
//  public void testTodayPlus1second(){
//    LocalDateTime now = LocalDateTime.now();
//    String today1second = TimeShifter.insertDate("<today + 1 seconds # dd-hh:mm:ss>", now);
//    Assert.assertEquals(now.plusSeconds(1).toString("dd-hh:mm:ss"), today1second);
//  }
//
//  @Test
//  public void testTodayPlus10seconds(){
//    LocalDateTime now = LocalDateTime.now();
//    String today10seconds = TimeShifter.insertDate("<today + 10 seconds # dd-hh:mm:ss>", now);
//    Assert.assertEquals(now.plusSeconds(10).toString("dd-hh:mm:ss"), today10seconds);
//  }
}
