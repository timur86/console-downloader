package com.github.itimur.utils;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static com.github.itimur.utils.CommonUtils.requireNonEmpty;
import static com.github.itimur.utils.CommonUtils.requirePositive;
import static com.google.common.truth.Truth.assertThat;

/**
 * @author Timur
 */
public class CommonUtilsTest {

    @Test
    public void itShouldReturnIntArgumentIfPositiveArgumentPassed() {
        assertThat(requirePositive(3, "arg")).is(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfIntArgumentIsNegative() {
        requirePositive(-1, "arg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfIntArgumentIsEqualToZero() {
        requirePositive(0, "arg");
    }

    @Test
    public void itShouldReturnCollectionArgumentIfNotNullOrEmptyArgumentPassed() {
        ImmutableList<String> immutableList = ImmutableList.of("foo", "bar");
        assertThat(requireNonEmpty(immutableList, "arg")).isSameAs(immutableList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfCollectionArgumentIsEmpty() {
        requireNonEmpty(Collections.emptyList(), "arg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfCollectionArgumentIsNull() {
        requireNonEmpty((Collection<String>) null, "arg");
    }

    @Test
    public void itShouldReturnStringArgumentIfNotNullOrEmptyArgumentPassed() {
        String string = "foo, bar";
        assertThat(requireNonEmpty(string, "arg")).isSameAs(string);
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfStringArgumentIsEmpty() {
        requireNonEmpty("", "arg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowIAE_IfStringArgumentIsNull() {
        requireNonEmpty((String) null, "arg");
    }
}
