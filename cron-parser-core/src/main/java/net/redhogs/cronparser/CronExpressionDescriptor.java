package net.redhogs.cronparser;

import net.redhogs.cronparser.builder.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author grhodes
 * @since 10 Dec 2012 11:36:38
 */
public class CronExpressionDescriptor {

    private static final Logger LOG = LoggerFactory.getLogger(CronExpressionDescriptor.class);
    private static final char[] specialCharacters = new char[] { '/', '-', ',', '*' };

    private CronExpressionDescriptor() {
    }

    public static String getDescription(String expression) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, new Options(), I18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(String expression, Options options) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, options, I18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(String expression, Locale locale) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, new Options(), locale);
    }

    public static String getDescription(String expression, Options options, Locale locale) throws ParseException {
        return getDescription(DescriptionTypeEnum.FULL, expression, options, locale);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression) throws ParseException {
        return getDescription(type, expression, new Options(), I18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression, Locale locale) throws ParseException {
        return getDescription(type, expression, new Options(), locale);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression, Options options) throws ParseException {
        return getDescription(type, expression, options, I18nMessages.DEFAULT_LOCALE);
    }

    public static String getDescription(DescriptionTypeEnum type, String expression, Options options, Locale locale) throws ParseException {
        I18nMessages.setCurrentLocale(locale);
        String[] expressionParts;
        String description = "";
        try {
            expressionParts = ExpressionParser.parse(expression, options);
            switch (type) {
                case FULL:
                    description = getFullDescription(expressionParts, options, locale);
                    break;
                case TIMEOFDAY:
                    description = getTimeOfDayDescription(expressionParts, options, locale);
                    break;
                case HOURS:
                    description = getHoursDescription(expressionParts, options, locale);
                    break;
                case MINUTES:
                    description = getMinutesDescription(expressionParts, options, locale);
                    break;
                case SECONDS:
                    description = getSecondsDescription(expressionParts, options, locale);
                    break;
                case DAYOFMONTH:
                    description = getDayOfMonthDescription(expressionParts, options, locale);
                    break;
                case MONTH:
                    description = getMonthDescription(expressionParts, options, locale);
                    break;
                case DAYOFWEEK:
                    description = getDayOfWeekDescription(expressionParts, options, locale);
                    break;
                case YEAR:
                    description = getYearDescription(expressionParts,options, locale);
                    break;
                default:
                    description = getSecondsDescription(expressionParts, options, locale);
                    break;
            }
        } catch (ParseException e) {
            if (!options.isThrowExceptionOnParseError()) {
                description = e.getMessage();
                LOG.debug("Exception parsing expression.", e);
            } else {
                LOG.error("Exception parsing expression.", e);
                throw e;
            }
        }
        return description;
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getYearDescription(String[] expressionParts, Options options, Locale locale) {
      return new YearDescriptionBuilder(options).getSegmentDescription(expressionParts[6], ", "+I18nMessages.get("every_year"), locale);
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getDayOfWeekDescription(String[] expressionParts, Options options, Locale locale) {
        return new DayOfWeekDescriptionBuilder(options).getSegmentDescription(expressionParts[5], ", "+I18nMessages.get("every_day"), locale);
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getMonthDescription(String[] expressionParts, Options options, Locale locale) {
        return new MonthDescriptionBuilder(options).getSegmentDescription(expressionParts[4], "", locale);
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getDayOfMonthDescription(String[] expressionParts, Options options, Locale locale) {
        String description = null;
        String exp = expressionParts[3].replace("?", "*");
        if ("L".equals(exp)) {
            description = ", "+I18nMessages.get("on_the_last_day_of_the_month");
        } else if ("WL".equals(exp) || "LW".equals(exp)) {
            description = ", "+I18nMessages.get("on_the_last_weekday_of_the_month");
        } else {
            Pattern pattern = Pattern.compile("(\\dW)|(W\\d)");
            Matcher matcher = pattern.matcher(exp);
            if (matcher.matches()) {
                int dayNumber = Integer.parseInt(matcher.group().replace("W", ""));
                String dayString = dayNumber == 1 ? I18nMessages.get("first_weekday") : MessageFormat.format(I18nMessages.get("weekday_nearest_day"), dayNumber);
                description = MessageFormat.format(", "+I18nMessages.get("on_the_of_the_month"), dayString);
            } else {
                description = new DayOfMonthDescriptionBuilder(options).getSegmentDescription(exp, ", "+I18nMessages.get("every_day"), locale);
            }
        }
        return description;
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getSecondsDescription(String[] expressionParts, Options opts, Locale locale) {
        return new SecondsDescriptionBuilder(opts).getSegmentDescription(expressionParts[0], I18nMessages.get("every_second"), locale);
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getMinutesDescription(String[] expressionParts, Options opts, Locale locale) {
        return new MinutesDescriptionBuilder(opts).getSegmentDescription(expressionParts[1], I18nMessages.get("every_minute"), locale);
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getHoursDescription(String[] expressionParts, Options opts, Locale locale) {
        return new HoursDescriptionBuilder(opts).getSegmentDescription(expressionParts[2], I18nMessages.get("every_hour"), locale);
    }

    /**
     * @param expressionParts
     * @return
     */
    private static String getTimeOfDayDescription(String[] expressionParts, Options opts, Locale locale) {
        String secondsExpression = expressionParts[0];
        String minutesExpression = expressionParts[1];
        String hoursExpression = expressionParts[2];
        StringBuilder description = new StringBuilder();
        // Handle special cases first
        boolean isChineseLocale = locale.equals(Locale.CHINA) || locale.equals(Locale.CHINESE);
        if (!StringUtils.containsAny(minutesExpression, specialCharacters) && !StringUtils.containsAny(hoursExpression, specialCharacters) && !StringUtils.containsAny(secondsExpression, specialCharacters)) {
            if (!isChineseLocale){
                description.append(I18nMessages.get("at"));
            }
            if(opts.isNeedSpaceBetweenWords()){
                description.append(" ");
            }
            description.append(DateAndTimeUtils.formatTime(hoursExpression, minutesExpression, secondsExpression, opts)); // Specific time of day (e.g. 10 14)
        } else if (minutesExpression.contains("-") && !minutesExpression.contains("/") && !StringUtils.containsAny(hoursExpression, specialCharacters)) {
            // Minute range in single hour (e.g. 0-10 11)
            String[] minuteParts = minutesExpression.split("-");
            description.append(MessageFormat.format(I18nMessages.get("every_minute_between"), DateAndTimeUtils.formatTime(hoursExpression, minuteParts[0], opts),
                    DateAndTimeUtils.formatTime(hoursExpression, minuteParts[1], opts)));
        } else if (hoursExpression.contains(",") && !StringUtils.containsAny(minutesExpression, specialCharacters)) {
            // Hours list with single minute (e.g. 30 6,14,16)
            String[] hourParts = hoursExpression.split(",");
            if (!isChineseLocale){
                description.append(I18nMessages.get("at"));
            }
            for (int i = 0; i < hourParts.length; i++) {
                if (opts.isNeedSpaceBetweenWords()) {
                    description.append(" ");
                }
                description.append(DateAndTimeUtils.formatTime(hourParts[i], minutesExpression, opts));
                if (i < hourParts.length - 2) {
                    description.append(",");
                }
                if (i == hourParts.length - 2) {
                    if (opts.isNeedSpaceBetweenWords()) {
                        description.append(" ");
                    }
                    description.append(I18nMessages.get("and"));
                }
            }
        } else {
            String secondsDescription = getSecondsDescription(expressionParts, opts, locale);
            String minutesDescription = getMinutesDescription(expressionParts, opts, locale);
            String hoursDescription = getHoursDescription(expressionParts, opts, locale);
            String result = "";
            if (isChineseLocale){
                result = MessageFormat.format(", {0}, {1}, {2}", hoursDescription, minutesDescription, secondsDescription);
                description.append(result);
            }else {
                result = MessageFormat.format("{0}, {1}, {2}", secondsDescription, minutesDescription, hoursDescription);
                description.append(result);
            }
        }
        return description.toString();
    }

    /**
     * @param options
     * @param expressionParts
     * @return
     */
    private static String getFullDescription(String[] expressionParts, Options options, Locale locale) {
        String description = "";
        String timeSegment = getTimeOfDayDescription(expressionParts, options, locale);
        String dayOfMonthDesc = getDayOfMonthDescription(expressionParts, options, locale);
        String monthDesc = getMonthDescription(expressionParts, options, locale);
        String dayOfWeekDesc = getDayOfWeekDescription(expressionParts, options, locale);
        String yearDesc = getYearDescription(expressionParts, options, locale);
        if (locale.equals(Locale.CHINESE)||locale.equals(Locale.CHINA)){
            description = MessageFormat.format("{0}{1}{2}{3}", yearDesc,(expressionParts[5].length()>1 ? "":monthDesc), ("*".equals(expressionParts[3]) ? dayOfWeekDesc : dayOfMonthDesc), timeSegment);
        }else {
            description = MessageFormat.format("{0}{1}{2}{3}", timeSegment, ("*".equals(expressionParts[3]) ? dayOfWeekDesc : dayOfMonthDesc), (expressionParts[5].length()>1 ? "":monthDesc), yearDesc);
        }
        description = transformVerbosity(description, options, locale, expressionParts).trim();
        description = transformCase(description, options);
        return description;
    }

    /**
     * @param description
     * @return
     */
    private static String transformCase(String description, Options options) {
        String descTemp = description;
        switch (options.getCasingType()) {
            case Sentence:
                descTemp = StringUtils.upperCase("" + descTemp.charAt(0)) + descTemp.substring(1);
                break;
            case Title:
                descTemp = StringUtils.capitalize(descTemp);
                break;
            default:
                descTemp = descTemp.toLowerCase();
                break;
        }
        return descTemp;
    }

    /**
     * @param description
     * @param options
     * @return
     */
    private static String transformVerbosity(String description, Options options, Locale locale, String[] expressionParts) {
        String descTemp = description;
        if (!options.isVerbose()) {
            descTemp = descTemp.replace(I18nMessages.get("every_1_minute"), I18nMessages.get("every_minute"));
            descTemp = descTemp.replace(I18nMessages.get("every_1_hour"), I18nMessages.get("every_hour"));
            descTemp = descTemp.replace(I18nMessages.get("every_1_day"), I18nMessages.get("every_day"));
            //秒位不为0，保留最后的秒
            if (expressionParts[0].length()!=0&&!"0".equals(expressionParts[0])){
                descTemp = descTemp.replace(", "+ I18nMessages.get("every_minute"), "");
                descTemp = descTemp.replace(", "+ I18nMessages.get("every_hour"), "");
                descTemp = descTemp.replace(", "+ I18nMessages.get("every_day"), "");
                descTemp = descTemp.replace(", " + I18nMessages.get("every_year"), "");
            }else if (expressionParts[1].length()!=0&&!"0".equals(expressionParts[1])){
                descTemp = descTemp.replace(", "+ I18nMessages.get("every_hour"), "");
                descTemp = descTemp.replace(", "+ I18nMessages.get("every_day"), "");

                descTemp = descTemp.replace(", " + I18nMessages.get("every_year"), "");
            }else {
                descTemp = descTemp.replace(", "+ I18nMessages.get("every_day"), "");

                descTemp = descTemp.replace(", " + I18nMessages.get("every_year"), "");
            }

        }
        if (locale.equals(Locale.CHINA)|| locale.equals(Locale.CHINESE)){
            descTemp = descTemp.replaceAll("[, ]", "");
        }
        return descTemp;
    }

}
