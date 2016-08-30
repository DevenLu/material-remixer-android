/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class RangeRemixTest {

  @Mock
  RemixCallback<Integer> singleIncrementsCallback;
  @Mock
  RemixCallback<Integer> increments5Callback;

  RangeRemix singleIncrements;
  RangeRemix increments5;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    singleIncrements = new RangeRemix("name", "key", 15, 0, 20, 1, singleIncrementsCallback, 0);
    increments5 = new RangeRemix("name", "key", 15, 0, 20, 5, increments5Callback, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptDefaultValueGreaterThanMax() {
    new RangeRemix("name", "key", 15, 0, 10, 1, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptDefaultValueLessThanMin() {
    new RangeRemix("name", "key", 15, 20, 30, 1, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidRanges() {
    new RangeRemix("name", "key", 15, 50, 20, 1, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptNegativeStepping() {
    new RangeRemix("name", "key", 15, 50, 20, -1, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidStepping() {
    // Stepping is invalid because maxValue 52 cannot be reached from 15 in steps of 5
    new RangeRemix("name", "key", 15, 0, 52, 5, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorDoesNotAcceptInvalidSteppingToDefaultValue() {
    // Stepping is invalid because defaultValue 22 cannot be reached from 15 in steps of 5
    new RangeRemix("name", "key", 22, 0, 50, 5, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueLessThanMin() {
    singleIncrements.setValue(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsValueGreaterThanMax() {
    singleIncrements.setValue(100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValueRejectsInvalidValueForStepping() {
    increments5.setValue(12);
  }

  @Test
  public void callbackIsCalledOnConstructor() {
    Mockito.verify(singleIncrementsCallback, Mockito.times(1)).onValueSet(singleIncrements);
  }

  @Test
  public void callbackIsCalledAfterValueSet() {
    singleIncrements.setValue(18);
    Mockito.verify(singleIncrementsCallback, Mockito.times(2)).onValueSet(singleIncrements);
    increments5.setValue(5);
    Mockito.verify(increments5Callback, Mockito.times(2)).onValueSet(increments5);
  }

  @Test
  public void doesNotCrashOnNullCallback() {
    RangeRemix remix = new RangeRemix("name", "key", 15, 0, 20, 1, null, 0);
    remix.setValue(18);
  }
}
