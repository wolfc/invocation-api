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

package org.jboss.invocation.classloader;

import org.jboss.invocation.Invocation;
import org.junit.Test;

import java.net.URLClassLoader;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class CloneInvocationTestCase {
    @Test
    public void testClassLoader() throws Exception {
        URLClassLoader master = (URLClassLoader) ClassLoader.getSystemClassLoader();
        RedefiningClassLoader classLoader1 = new RedefiningClassLoader(master, IsolatedBean.class);
        Class<?> cls1 = Class.forName(IsolatedBean.class.getName(), false, classLoader1);
        assertEquals(classLoader1, cls1.getClassLoader()); // sanity
        RedefiningClassLoader classLoader2 = new RedefiningClassLoader(master, IsolatedBean.class);
        Class<?> cls2 = Class.forName(IsolatedBean.class.getName(), false, classLoader2);
        assertEquals(classLoader2, cls2.getClassLoader()); // sanity
        
        Invocation invocation1 = new Invocation(cls1.getMethod("workMagic"), null);
        Invocation invocation2 = invocation1.cloneTo(classLoader2);

        assertEquals(cls2, invocation2.getDeclaringClass());
        assertEquals(cls2, invocation2.getMethod().getDeclaringClass());
    }
}
