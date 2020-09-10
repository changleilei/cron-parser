package net.redhogs.cronparser.builder;

import net.redhogs.cronparser.I18nMessages;
import net.redhogs.cronparser.Options;
import org.joda.time.DateTime;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:23:50
 */
public class MonthDescriptionBuilder extends AbstractDescriptionBuilder {
    private final Options options;

    public MonthDescriptionBuilder(Options options) {
        this.options = options;
    }
    @Override
    protected String getSingleItemDescription(String expression) {
        return new DateTime().withDayOfMonth(1).withMonthOfYear(Integer.parseInt(expression)).
                toString("MMMM", I18nMessages.getCurrentLocale());
    }

    @Override
    protected String getIntervalDescriptionFormat(String expression) {
        if (options.getLocale().equals(Locale.CHINESE)|| options.getLocale().equals(Locale.CHINA)){
            return MessageFormat.format(I18nMessages.get("interval_description_format")+"个"+ getSpace(options) +
                    plural(expression, I18nMessages.get("month"), I18nMessages.get("months")), expression);
        }
        return MessageFormat.format(", "+I18nMessages.get("every_x")+ getSpace(options) +
                plural(expression, I18nMessages.get("month"), I18nMessages.get("months")), expression);
    }

    @Override
    protected String getBetweenDescriptionFormat(String expression, boolean omitSeparator) {
    	String format = I18nMessages.get("between_description_format");
        return omitSeparator ? format : ", "+format;
    }

    @Override
    protected String getDescriptionFormat(String expression) {
        return ", "+I18nMessages.get("only_in_month");
    }

    @Override
    protected Boolean needSpaceBetweenWords() {
        return options.isNeedSpaceBetweenWords();
    }

    @Override
    protected String getBeginDescriptionFormat(String expression) {
        return ", "+MessageFormat.format(I18nMessages.get("begin_description_format"), expression)+I18nMessages.get("month")+I18nMessages.get("begin");
    }

}
