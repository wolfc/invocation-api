/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.invocation.performance;

import org.jboss.invocation.ObjectInvocationDispatcher;
import org.jboss.invocation.ProxyInvocationHandler;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Just blast away at different pieces of code.
 *
 * This test should not be run during a regular build.
 * 
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class PerformanceTestCase {
   public static interface Calculator {
      int add(int a, int b);
   }
   
   public static class CalculatorBean implements Calculator {
      public int add(int a, int b) {
         return a + b;
      }
   }

   @Test
   public void testProxyInvocationHandler() {
      Object target = new CalculatorBean();
      ObjectInvocationDispatcher dispatcher = new ObjectInvocationDispatcher(target);
      ProxyInvocationHandler handler = new ProxyInvocationHandler(dispatcher);
      Calculator proxy = (Calculator) Proxy.newProxyInstance(Calculator.class.getClassLoader(), new Class<?>[]{Calculator.class}, handler);
      long numInvocations = 0;
      long end = System.currentTimeMillis() + 10000;
      while(System.currentTimeMillis() < end) {
         proxy.add(1, 2);
         numInvocations++;
      }
      System.out.println("Number of invocations " + numInvocations);
   }
}
