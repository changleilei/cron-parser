package net.redhogs.cronparser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * @author vitctor wang
 * @since 02 SEP 2017 18:28:13
 */
public class CronExpressionDescriptorZHTest {
    private static final Locale CHINESE = new Locale("zh");
    private static final Options options = new Options();

    static {
        options.setTwentyFourHourTime(true);
        /** Chinese, Japanese, Korean and other East Asian languages have no spaces between words */
        options.setNeedSpaceBetweenWords(false);
//        options.setZeroBasedDayOfWeek(false);
        options.setLocale(Locale.CHINA);

    }

    @Test
    public void testEverySecond() throws Exception {
        Assert.assertEquals("每秒钟", CronExpressionDescriptor.getDescription("* * * * * *", options, CHINESE));
        Assert.assertEquals("每秒钟", CronExpressionDescriptor.getDescription("* * * * * *", options, CHINESE));
    }

    @Test
    public void testEvery45Seconds() throws Exception {
        Assert.assertEquals("每隔45秒", CronExpressionDescriptor.getDescription("*/45 * * * * *", options, CHINESE));
        Assert.assertEquals("每隔45秒", CronExpressionDescriptor.getDescription("*/45 * * * * *", options, CHINESE));
    }

    @Test
    public void testMinuteSpan() throws Exception {
        Assert.assertEquals("每天中午11:00和中午11:10之间每分钟", CronExpressionDescriptor.getDescription("0-10 11 * * *", options, CHINESE));
        Assert.assertEquals("每天凌晨1:00每分钟", CronExpressionDescriptor.getDescription("* 1 * * *", options, CHINESE));
        Assert.assertEquals("每天凌晨12:00每分钟", CronExpressionDescriptor.getDescription("* 0 * * *", options, CHINESE));
    }

    @Test
    public void testEveryMinute() throws Exception {
        Assert.assertEquals("每天每小时每分钟", CronExpressionDescriptor.getDescription("* * * * *",options, CHINESE));
        Assert.assertEquals("每天每小时每隔1分钟", CronExpressionDescriptor.getDescription("*/1 * * * *", options,CHINESE));
        Assert.assertEquals("每天每小时每隔1分钟", CronExpressionDescriptor.getDescription("0 0/1 * * * ?", options,CHINESE));
    }

    @Test
    public void testEveryXMinutes() throws Exception {
        Assert.assertEquals("每天每小时每隔5分钟", CronExpressionDescriptor.getDescription("*/5 * * * *", options, CHINESE));
        Assert.assertEquals("每天每小时每隔5分钟", CronExpressionDescriptor.getDescription("0 */5 * * * *", options, CHINESE));
        Assert.assertEquals("每天每小时每隔10分钟", CronExpressionDescriptor.getDescription("0 0/10 * * * ?", options, CHINESE));
    }

    @Test
    public void testEveryHour() throws Exception {
        Assert.assertEquals("每天每小时", CronExpressionDescriptor.getDescription("0 0 * * * ?", options, CHINESE));
        Assert.assertEquals("每天每隔1小时", CronExpressionDescriptor.getDescription("0 0 0/1 * * ?", options, CHINESE));
        Assert.assertEquals("每天每小时", CronExpressionDescriptor.getDescription("0 * * * *", options, CHINESE));
    }

    @Test
    public void testDailyAtTime() throws Exception {
        Assert.assertEquals("每天中午11:30", CronExpressionDescriptor.getDescription("30 11 * * *", options, CHINESE));
        Assert.assertEquals("每天中午11:00", CronExpressionDescriptor.getDescription("0 11 * * *", options, CHINESE));
    }

    @Test
    public void testTimeOfDayCertainDaysOfWeek() throws Exception {
        Assert.assertEquals("星期一到星期五晚上11:00", CronExpressionDescriptor.getDescription("0 23 ? * MON-FRI", options, CHINESE));
        Assert.assertEquals("星期一到星期五晚上11:00", CronExpressionDescriptor.getDescription("0 23 ? * MON-FRI", options, CHINESE));
        Assert.assertEquals("星期一到星期五中午11:30", CronExpressionDescriptor.getDescription("30 11 * * 1-5", options, CHINESE));
    }

    @Test
    public void testOneMonthOnly() throws Exception {
        Assert.assertEquals("三月每天每小时每分钟", CronExpressionDescriptor.getDescription("* * * 3 *", CHINESE));
    }

    @Test
    public void testTwoMonthsOnly() throws Exception {
        Assert.assertEquals("三月和六月每天每小时每分钟", CronExpressionDescriptor.getDescription("* * * 3,6 *", options, CHINESE));
    }

    @Test
    public void testTwoTimesEachAfternoon() throws Exception {
        Assert.assertEquals("每天下午2:30和下午4:30", CronExpressionDescriptor.getDescription("30 14,16 * * *", options, CHINESE));
    }

    @Test
    public void testThreeTimesDaily() throws Exception {
        Assert.assertEquals("每天凌晨6:30下午2:30和下午4:30", CronExpressionDescriptor.getDescription("30 6,14,16 * * *", options, CHINESE));
    }

    @Test
    public void testOnceAWeek() throws Exception {
        Assert.assertEquals("星期日上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 0", options, CHINESE));
        Assert.assertEquals("星期日上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 7", options, CHINESE));
        Assert.assertEquals("星期一上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 1", options, CHINESE));
        Assert.assertEquals("星期六上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 6", options, CHINESE));
    }

    @Test
    public void testOnceAWeekNonZeroBased() throws Exception {
        Options options = new Options();
        options.setZeroBasedDayOfWeek(false);
        options.setNeedSpaceBetweenWords(false);
        options.setTwentyFourHourTime(true);
        Assert.assertEquals("星期日上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 1", options, CHINESE));
        Assert.assertEquals("星期一上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 2", options, CHINESE));
        Assert.assertEquals("星期六上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 7", options, CHINESE));
    }

    @Test
    public void testTwiceAWeek() throws Exception {
        Assert.assertEquals("星期一和星期二上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 1,2", options, CHINESE));
        Assert.assertEquals("星期日和星期六上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 0,6", options, CHINESE));
        Assert.assertEquals("星期六和星期日上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 6,7", options, CHINESE));
    }

    @Test
    public void testTwiceAWeekNonZeroBased() throws Exception {
        Options options = new Options();
        options.setZeroBasedDayOfWeek(false);
        options.setTwentyFourHourTime(true);
        options.setNeedSpaceBetweenWords(false);
        Assert.assertEquals("星期日和星期一上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 1,2", options, CHINESE));
        Assert.assertEquals("星期五和星期六上午9:46", CronExpressionDescriptor.getDescription("46 9 * * 6,7", options, CHINESE));
    }

    @Test
    public void testDayOfMonth() throws Exception {
        Assert.assertEquals("每月15日中午12:23", CronExpressionDescriptor.getDescription("23 12 15 * *", options, CHINESE));
    }

    @Test
    public void testMonthName() throws Exception {
        Assert.assertEquals("一月每天中午12:23", CronExpressionDescriptor.getDescription("23 12 * JAN *", options, CHINESE));
    }

    @Test
    public void testDayOfMonthWithQuestionMark() throws Exception {
        Assert.assertEquals("一月每天中午12:23", CronExpressionDescriptor.getDescription("23 12 ? JAN *", options, CHINESE));
    }

    @Test
    public void testMonthNameRange2() throws Exception {
        Assert.assertEquals("一月到二月每天中午12:23", CronExpressionDescriptor.getDescription("23 12 * JAN-FEB *", options, CHINESE));
    }

    @Test
    public void testMonthNameRange3() throws Exception {
        Assert.assertEquals("一月到三月每天中午12:23", CronExpressionDescriptor.getDescription("23 12 * JAN-MAR *", options, CHINESE));
    }

    @Test
    public void testMonthNameRanges() throws Exception {
        Assert.assertEquals("一月到三月和五月到六月每天凌晨3:00", CronExpressionDescriptor.getDescription("0 0 3 * 1-3,5-6 *", options, CHINESE));
    }

    @Test
    public void testDayOfWeekName() throws Exception {
        Assert.assertEquals("星期日中午12:23", CronExpressionDescriptor.getDescription("23 12 * * SUN", options, CHINESE));
    }

    @Test
    public void testDayOfWeekRange() throws Exception {
        Assert.assertEquals("星期一到星期五下午3:00每隔5分钟", CronExpressionDescriptor.getDescription("*/5 15 * * MON-FRI", options, CHINESE));
        Assert.assertEquals("星期日到星期六下午3:00每隔5分钟", CronExpressionDescriptor.getDescription("*/5 15 * * 0-6", options, CHINESE));
        Assert.assertEquals("星期六到星期日下午3:00每隔5分钟", CronExpressionDescriptor.getDescription("*/5 15 * * 6-7", options, CHINESE));
    }

    @Test
    public void testDayOfWeekRanges() throws Exception {
        Assert.assertEquals("星期日、星期二到星期四和星期六凌晨3:00", CronExpressionDescriptor.getDescription("0 0 3 * * 0,2-4,6", options, CHINESE));
    }

    @Test
    public void testDayOfWeekOnceInMonth() throws Exception {
        Assert.assertEquals("每月第3个星期一每小时每分钟", CronExpressionDescriptor.getDescription("* * * * MON#3", CHINESE));
        Assert.assertEquals("每月第3个星期日每小时每分钟", CronExpressionDescriptor.getDescription("* * * * 0#3", CHINESE));
    }

    @Test
    public void testLastDayOfTheWeekOfTheMonth() throws Exception {
        Assert.assertEquals("每月最后1个星期四每小时每分钟", CronExpressionDescriptor.getDescription("* * * * 4L", CHINESE));
        Assert.assertEquals("每月最后1个星期日每小时每分钟", CronExpressionDescriptor.getDescription("* * * * 0L", CHINESE));
    }

    @Test
    public void testLastDayOfTheMonth() throws Exception {
        Assert.assertEquals("一月每月最后1天每小时每隔5分钟", CronExpressionDescriptor.getDescription("*/5 * L JAN *", options, CHINESE));
    }

    @Test
    public void testTimeOfDayWithSeconds() throws Exception {
        Assert.assertEquals("下午2:02:30", CronExpressionDescriptor.getDescription("30 02 14 * * *", options, CHINESE));
    }

    @Test
    public void testSecondInternvals() throws Exception {
        Assert.assertEquals("过整分钟后5到10秒", CronExpressionDescriptor.getDescription("5-10 * * * * *", CHINESE));
    }

    @Test
    public void testSecondMinutesHoursIntervals() throws Exception {
        Assert.assertEquals("上午10:00和中午12:00之间过整点后30到35分过整分钟后5到10秒",
                CronExpressionDescriptor.getDescription("5-10 30-35 10-12 * * *", options, CHINESE));
    }

    @Test
    public void testEvery5MinutesAt30Seconds() throws Exception {
        Assert.assertEquals("每隔5分钟过整分后第30秒", CronExpressionDescriptor.getDescription("30 */5 * * * *", options, CHINESE));
    }

    @Test
    public void testMinutesPastTheHourRange() throws Exception {
        Assert.assertEquals("星期三和星期五上午10:00和中午1:00之间过整点后30分钟",
                CronExpressionDescriptor.getDescription("0 30 10-13 ? * WED,FRI", options, CHINESE));
    }

    @Test
    public void testSecondsPastTheMinuteInterval() throws Exception {
        Assert.assertEquals("每隔5分钟过整分后第10秒", CronExpressionDescriptor.getDescription("10 0/5 * * * ?", options, CHINESE));
    }

    @Test
    public void testBetweenWithInterval() throws Exception {
        Assert.assertEquals("一月到六月每月11日和26日之间的每天凌晨1:00、上午9:00和晚上10:00过整点后02到59分每隔3分钟",
                CronExpressionDescriptor.getDescription("2-59/3 1,9,22 11-26 1-6 ?", options, CHINESE));
    }

    @Test
    public void testRecurringFirstOfMonth() throws Exception {
        Assert.assertEquals("每天隔1天凌晨6:00", CronExpressionDescriptor.getDescription("0 0 6 1/1 * ?", options, CHINESE));
    }

    @Test
    public void testMinutesPastTheHour() throws Exception {
        Assert.assertEquals("每天每隔1小时过整点后05分钟", CronExpressionDescriptor.getDescription("0 5 0/1 * * ?", options, CHINESE));
    }

    /**
     * @since https://github.com/RedHogs/cron-parser/issues/2
     */
    @Test
    public void testEveryPastTheHour() throws Exception {
        Assert.assertEquals("每天每小时过整点后00、05、10、15、20、25、30、35、40、45、50和55分钟", CronExpressionDescriptor.getDescription("0 0,5,10,15,20,25,30,35,40,45,50,55 * ? * *", options, CHINESE));
    }

    /**
     * @since https://github.com/RedHogs/cron-parser/issues/10
     */
    @Test
    public void testEveryXMinutePastTheHourWithInterval() throws Exception {
        Assert.assertEquals("星期一到星期五下午5:00过整点后00到30分每隔2分钟", CronExpressionDescriptor.getDescription("0 0-30/2 17 ? * MON-FRI", options, CHINESE));
    }

    /**
     * @since https://github.com/RedHogs/cron-parser/issues/13
     */
    @Test
    public void testOneYearOnlyWithSeconds() throws Exception {
        Assert.assertEquals("2013年每秒钟", CronExpressionDescriptor.getDescription("* * * * * * 2013", CHINESE));
    }

    @Test
    public void testOneYearOnlyWithoutSeconds() throws Exception {
        Assert.assertEquals("2013年每天每小时每分钟", CronExpressionDescriptor.getDescription("* * * * * 2013", CHINESE));
    }

    @Test
    public void testTwoYearsOnly() throws Exception {
        Assert.assertEquals("2013和2014年每天每小时每分钟", CronExpressionDescriptor.getDescription("* * * * * 2013,2014", options, CHINESE));
    }

    @Test
    public void testYearRange2() throws Exception {
        Assert.assertEquals("2013到2014一月到二月每天中午12:23", CronExpressionDescriptor.getDescription("23 12 * JAN-FEB * 2013-2014", options, CHINESE));
    }

    @Test
    public void testYearRange3() throws Exception {
        Assert.assertEquals("2013到2015一月到三月每天中午12:23", CronExpressionDescriptor.getDescription("23 12 * JAN-MAR * 2013-2015", options, CHINESE));
    }

    @Test
    public void testIssue26() throws Exception {
        Assert.assertEquals("每天每小时过整点后05和10分钟", CronExpressionDescriptor.getDescription("5,10 * * * *", options, CHINESE));
        Assert.assertEquals("每天凌晨12:00过整点后05和10分钟", CronExpressionDescriptor.getDescription("5,10 0 * * *", options, CHINESE));
        Assert.assertEquals("每月2日每小时过整点后05和10分钟", CronExpressionDescriptor.getDescription("5,10 * 2 * *", options, CHINESE));
        Assert.assertEquals("每月2日每小时从第5分钟开始每隔10分钟", CronExpressionDescriptor.getDescription("5/10 * 2 * *", options, CHINESE));

        Assert.assertEquals("过整分后第5和6秒", CronExpressionDescriptor.getDescription("5,6 0 * * * *", options, CHINESE));
        Assert.assertEquals("凌晨1:00过整分后第5和6秒", CronExpressionDescriptor.getDescription("5,6 0 1 * * *", options, CHINESE));
        Assert.assertEquals("每月2日过整分后第5和6秒", CronExpressionDescriptor.getDescription("5,6 0 * 2 * *", options, CHINESE));
    }

    @Test
    public void testCustom() throws Exception{
        options.setLocale(CHINESE);
        System.out.println(CronExpressionDescriptor.getDescription("* 3/5 0/1 * * ? *", options, CHINESE));
    }

}
