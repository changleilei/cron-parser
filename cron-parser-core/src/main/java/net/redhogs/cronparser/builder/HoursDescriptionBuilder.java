package net.redhogs.cronparser.builder;

import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.DateAndTimeUtils;
import net.redhogs.cronparser.I18nMessages;
import net.redhogs.cronparser.Options;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:18:21
 */
public class HoursDescriptionBuilder extends AbstractDescriptionBuilder {

    private final Options options;

    public HoursDescriptionBuilder(Options options) {
        this.options = options;
    }

    @Override
    protected String getSingleItemDescription(String expression) {
        String des = DateAndTimeUtils.formatTime(expression, "0", options);
        if (options.getLocale().equals(Locale.CHINESE)||options.getLocale().equals(Locale.CHINA)){
            des = CronExpressionDescriptor.chineseTypeDescription(des, expression);
        }
        return des;
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        if (options.getLocale().equals(Locale.CHINA)||options.getLocale().equals(Locale.CHINESE)){
            return MessageFormat.format(I18nMessages.get("interval_description_format"), expression) + I18nMessages.get("hour");
        }
        return MessageFormat.format(I18nMessages.get("every_x")+ getSpace(options) +
                plural(expression, I18nMessages.get("hour"), I18nMessages.get("hours")), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
        return I18nMessages.get("between_x_and_y");
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return I18nMessages.get("at_x");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

    @Override
    protected String getBeginDescriptionFormat(String expression) {
        return ", "+MessageFormat.format(I18nMessages.get("begin_description_format"), expression)+I18nMessages.get("hour")+I18nMessages.get("begin");
    }

}
