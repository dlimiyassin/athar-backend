package com.student.career.zBase.criteria;

import com.student.career.zBase.util.CollectionUtil;
import com.student.career.zBase.util.DateUtil;
import com.student.career.zBase.util.NumberUtil;
import com.student.career.zBase.util.StringUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public class SearchHelper {
    public static <T> void addCriteria(String name, String value, Query query) {
        if (StringUtil.isNotEmpty(value)) {
            query.addCriteria(Criteria.where(name).is(value));
        }
    }

    public static <T> void addCriteria(String name, Integer value, Query query) {
        if (value != null) {
            query.addCriteria(Criteria.where(name).is(value));
        }
    }

    public static <T> void addCriteria(String name, Boolean value, Query query) {
        if (value != null) {
            query.addCriteria(Criteria.where(name).is(value));
        }
    }

    public static <T> void addCriteria(String name, Long value, Query query) {
        if (value != null) {
            query.addCriteria(Criteria.where(name).is(value));
        }
    }

    public static <T> void addCriteria(String name, LocalDateTime value, Query query) {
        if (value != null) {
            query.addCriteria(Criteria.where(name).is(value));
        }
    }

    public static <T> void addCriteriaLocalDate(String name, LocalDateTime value, Query query) {
        if (value != null) {
            value = DateUtil.toZonedDateTime(value);
            assert value != null;
            LocalDate localDate = value.toLocalDate();
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
            query.addCriteria(Criteria.where(name).gte(startOfDay).lte(endOfDay));
        }
    }


    public static <T> void addCriteriaMinMax(String name, BigDecimal valueMin, BigDecimal valueMax, Query query) {
        if (NumberUtil.isNotNull(valueMax) && NumberUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin).lte(valueMax));
        } else if (NumberUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin));
        } else if (NumberUtil.isNotNull(valueMax)) {
            query.addCriteria(Criteria.where(name).lte(valueMax));
        }
    }

    public static <T> void addCriteriaMinMax(String name, Integer valueMin, Integer valueMax, Query query) {
        if (NumberUtil.isNotNull(valueMax) && NumberUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin).lte(valueMax));
        } else if (NumberUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin));
        } else if (NumberUtil.isNotNull(valueMax)) {
            query.addCriteria(Criteria.where(name).lte(valueMax));
        }
    }

    public static <T> void addCriteriaFromTo(String name, LocalDateTime valueMin, LocalDateTime valueMax,
                                             Query query) {
        if (DateUtil.isNotNull(valueMax) && DateUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin).lte(valueMax));
        } else if (DateUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin));
        } else if (DateUtil.isNotNull(valueMax)) {
            query.addCriteria(Criteria.where(name).lte(valueMax));
        }
    }

    public static <T> void addCriteriaFromTo(String name, LocalDate valueMin, LocalDate valueMax,
                                             Query query) {
        if (DateUtil.isNotNull(valueMax) && DateUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin).lte(valueMax));
        } else if (DateUtil.isNotNull(valueMin)) {
            query.addCriteria(Criteria.where(name).gte(valueMin));
        } else if (DateUtil.isNotNull(valueMax)) {
            query.addCriteria(Criteria.where(name).lte(valueMax));
        }
    }


    public static <T> void addCriteriaFK(String name, Long value,
                                         Query query) {

        // Assuming that the value is always a String for simplicity
        String[] strings = name.split("\\.");
        if (NumberUtil.isNotNull(value) && strings.length == 2) {
            query.addCriteria(Criteria.where(strings[0] + "." + strings[1]).is(value));
        }
    }

    public static <T> void addCriteriaFK(String name, String value,
                                         Query query) {

        // Assuming that the value is always a String for simplicity
        String[] strings = name.split("\\.");
        if (StringUtil.isNotEmpty(value) && strings.length == 2) {
            query.addCriteria(Criteria.where(strings[0] + "." + strings[1]).is(value));
        }
    }

    public static <T, E extends Enum<E>> void addCriteria(String name, E value,
                                                          Query query) {
        if (value != null) {
            query.addCriteria(Criteria.where(name).is(value));
        }
    }

    public static <T, R> void addCriteria(String name, List<T> values, Function<T, R> mapper, Query query) {
        if (CollectionUtil.isNotEmpty(values)) {
            List<R> mappedValues = values.stream()
                    .map(mapper)
                    .toList();
            query.addCriteria(Criteria.where(name).in(mappedValues));
        }
    }

}
