package net.redhogs.cronparser.builder;

import net.redhogs.cronparser.I18nMessages;
import net.redhogs.cronparser.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author grhodes
 * @since 10 Dec 2012 14:00:49
 */
public abstract class AbstractDescriptionBuilder {

    protected final char[] SpecialCharsMinusStar = new char[] { '/', '-', ',' };

    public String getSegmentDescription(String expression, String allDescription, Locale locale) {
        String description = "";
        boolean isChineseLocale = locale.equals(Locale.CHINA)||locale.equals(Locale.CHINESE);
        if (StringUtils.isEmpty(expression)) {
            description = "";
        } else if ("*".equals(expression)) {
            description = allDescription;
        } else if (!StringUtils.containsAny(expression, SpecialCharsMinusStar)) {
            String temp = getDescriptionFormat(expression);
            if (isChineseLocale){
                String[] terms = temp.split("，");
                if (terms.length==2){
                    temp = terms[1]+terms[0];
                }else {
                    temp = terms[0];
                }
            }
            description = MessageFormat.format(temp, getSingleItemDescription(expression));
        } else if (expression.contains("/")) {
            String[] segments = expression.split("/");
            if (isChineseLocale){
                if (segments[0].contains("-")) {
                    String betweenSegmentOfInterval = segments[0];
                    String[] betweenSegments = betweenSegmentOfInterval.split("-");
                    description = MessageFormat.format(getBetweenDescriptionFormat(betweenSegmentOfInterval, false), getSingleItemDescription(betweenSegments[0]), getSingleItemDescription(betweenSegments[1]));
                    description += MessageFormat.format(getIntervalDescriptionFormat(segments[1]), getSingleItemDescription(segments[1]));
                }else {
                    if ("*".equals(segments[0])){
                        description += ", " + MessageFormat.format(getIntervalDescriptionFormat(segments[1]), getSingleItemDescription(segments[1]));
                    }else {
                        description += ", " + getBeginDescriptionFormat(segments[0]) +MessageFormat.format(getIntervalDescriptionFormat(segments[1]), getSingleItemDescription(segments[1]));
                    }
                }
            }else {
                description = MessageFormat.format(getIntervalDescriptionFormat(segments[1]), getSingleItemDescription(segments[1]));
                // interval contains 'between' piece (e.g. 2-59/3)
                if (segments[0].contains("-")) {
                    String betweenSegmentOfInterval = segments[0];
                    String[] betweenSegments = betweenSegmentOfInterval.split("-");
                    description += ", " + MessageFormat.format(getBetweenDescriptionFormat(betweenSegmentOfInterval, false), getSingleItemDescription(betweenSegments[0]), getSingleItemDescription(betweenSegments[1]));
                }
            }

        } else if (expression.contains(",")) {
            String[] segments = expression.split(",");
            StringBuilder descriptionContent = new StringBuilder();
            for (int i = 0; i < segments.length; i++) {
                if ((i > 0) && (segments.length > 2)) {
                    if (i < (segments.length - 1)) {
                        if(isChineseLocale){
                            descriptionContent.append("、");
                        }else {
                            descriptionContent.append(", ");
                        }
                    }
                }
                if ((i > 0) && (segments.length > 1) && ((i == (segments.length - 1)) || (segments.length == 2))) {
                    if (needSpaceBetweenWords()) {
                        descriptionContent.append(" ");
                    }
                    descriptionContent.append(I18nMessages.get("and"));
                    if (needSpaceBetweenWords()) {
                        descriptionContent.append(" ");
                    }
                }
                if (segments[i].contains("-")) {
                    String[] betweenSegments = segments[i].split("-");
                    descriptionContent.append(MessageFormat.format(getBetweenDescriptionFormat(expression, true), getSingleItemDescription(betweenSegments[0]), getSingleItemDescription(betweenSegments[1])));
                } else {
                    descriptionContent.append(getSingleItemDescription(segments[i]));
                }
            }
            String temp = getDescriptionFormat(expression);
            String tempContentDes = descriptionContent.toString();
            if (isChineseLocale){
                String[] terms = temp.split("，");
                if (terms.length==2){
                    temp = terms[1]+terms[0];
                }else {
                    temp = terms[0];
                }
            }
            description = MessageFormat.format(temp, tempContentDes);
        } else if (expression.contains("-")) {
            String[] segments = expression.split("-");
            description = MessageFormat.format(getBetweenDescriptionFormat(expression, false), getSingleItemDescription(segments[0]), getSingleItemDescription(segments[1]));
        }
        return description;
    }

    /**
     * @param expression
     * @return
     */
    protected abstract String getBetweenDescriptionFormat(String expression, boolean omitSeparator);

    /**
     * @param expression
     * @return
     */
    protected abstract String getIntervalDescriptionFormat(String expression);

    /**
     * @param expression
     * @return
     */
    protected abstract String getSingleItemDescription(String expression);

    /**
     * @param expression
     * @return
     */
    protected abstract String getDescriptionFormat(String expression);

    /**
     * @param
     * @return
     */
    protected abstract Boolean needSpaceBetweenWords();

    /**
     * @param num
     * @param singular
     * @param plural
     * @return
     * @deprecated Use plural(String, String, String) instead
     */
    @Deprecated
    protected String plural(int num, String singular, String plural) {
        return plural(String.valueOf(num), singular, plural);
    }

    protected abstract String getBeginDescriptionFormat(String expression);
    /**
     * @param expression
     * @param singular
     * @param plural
     * @return
     * @since https://github.com/RedHogs/cron-parser/issues/2
     */
    protected String plural(String expression, String singular, String plural) {
        if (NumberUtils.isNumber(expression) && (Integer.parseInt(expression) > 1)) {
            return plural;
        } else if (StringUtils.contains(expression, ",")) {
            return plural;
        }
        return singular;
    }

    /**
     * @since https://github.com/grahamar/cron-parser/issues/48
     * @param options
     * @return
     */
    protected String getSpace(Options options){
        return options.isNeedSpaceBetweenWords() ? " " : "";
    }
}
