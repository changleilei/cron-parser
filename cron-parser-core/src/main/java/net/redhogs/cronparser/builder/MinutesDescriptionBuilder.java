package net.redhogs.cronparser.builder;

import net.redhogs.cronparser.DateAndTimeUtils;
import net.redhogs.cronparser.I18nMessages;
import net.redhogs.cronparser.Options;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:11:11
 */
public class MinutesDescriptionBuilder extends AbstractDescriptionBuilder {
    private final Options options;

    public MinutesDescriptionBuilder(Options options) {
        this.options = options;
    }
    @Override
    protected String getSingleItemDescription(String expression) {
        return DateAndTimeUtils.formatMinutes(expression);
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        if (options.getLocale().equals(Locale.CHINA)||options.getLocale().equals(Locale.CHINESE)){
            return MessageFormat.format(I18nMessages.get("interval_description_format"), expression) + I18nMessages.get("minute");
        }
        return MessageFormat.format(I18nMessages.get("every_x") + getSpace(options) + minPlural(expression), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        return I18nMessages.get("minutes_through_past_the_hour");
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return "0".equals(expression) ? "" : I18nMessages.get("at_x") + getSpace(options) + minPlural(expression) +
                getSpace(options) + I18nMessages.get("past_the_hour");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

    @Override
    protected String getBeginDescriptionFormat(String expression) {
        return ", "+MessageFormat.format(I18nMessages.get("begin_description_format"), expression)+I18nMessages.get("minute")+I18nMessages.get("begin");
    }

    private String minPlural(String expression) {
        return plural(expression, I18nMessages.get("minute"), I18nMessages.get("minutes"));
    }

}
