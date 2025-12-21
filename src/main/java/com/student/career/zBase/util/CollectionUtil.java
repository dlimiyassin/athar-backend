package com.student.career.zBase.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    public static <E> List<List<E>> getListToBeSavedAndToBeDeleted(List<E> newList, List<E> oldList) {
        List<List<E>> result = new ArrayList<>();
        List<E> toBeSaved = new ArrayList<>();
        List<E> toBeDeleted = new ArrayList<>();
        if (isEmpty(oldList)) {
            toBeSaved = newList;
        } else {
            toBeSaved = newList.stream().filter(e -> !oldList.contains(e)).collect(Collectors.toList());
            toBeDeleted = oldList.stream().filter(e -> !newList.contains(e)).collect(Collectors.toList());
        }
        result.add(toBeSaved);
        result.add(toBeDeleted);
        return result;
    }


}
