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

package org.jboss.invocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Util for centralizing the logic for handling primitive types
 * during classloading. Use the {@link #loadClass(String, ClassLoader)}
 * to centralize the logic of checking for primitive types to ensure that
 * the {@link ClassLoader#loadClass(String)} method is not invoked for primitives.
 *
 * @author Jaikiran Pai
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class PrimitiveClassLoadingUtil {
    private static final Map<String, Class<?>> primitives;

    static {
        Map<String, Class<?>> m = new HashMap<String, Class<?>>(9);

        m.put(void.class.getName(), void.class);
        m.put(byte.class.getName(), byte.class);
        m.put(short.class.getName(), short.class);
        m.put(int.class.getName(), int.class);
        m.put(long.class.getName(), long.class);
        m.put(char.class.getName(), char.class);
        m.put(boolean.class.getName(), boolean.class);
        m.put(float.class.getName(), float.class);
        m.put(double.class.getName(), double.class);

        primitives = Collections.unmodifiableMap(m);
    }
    
    /**
     * First checks if <code>name</code> is a primitive type. If yes, then returns
     * the corresponding {@link Class} for that primitive. If it's not a primitive
     * then the {@link Class#forName(String, boolean, ClassLoader)} method is invoked, passing
     * it the <code>name</code>, false and the <code>cl</code> classloader
     * <p/>
     * Note that we intentionally use Class.forName(name,boolean,cl)
     * to handle issues with loading array types in Java 6 http://bugs.sun.com/view_bug.do?bug_id=6434149
     *
     * @param name The class that has to be loaded
     * @param cl   The {@link ClassLoader} to use, if <code>name</code> is *not* a primitive
     * @return Returns the {@link Class} corresponding to <code>name</code>
     * @throws ClassNotFoundException If the class for <code>name</code> could not be found
     * @see ClassLoader#loadClass(String)
     */
    public static Class<?> loadClass(String name, ClassLoader cl) throws ClassNotFoundException {
        Class<?> cls = primitives.get(name);
        if (cls != null)
            return cls;
        // Now that we know its not a primitive, lets just allow
        // the passed classloader to handle the request.
        // Note that we are intentionally using Class.forName(name,boolean,cl)
        // to handle issues with loading array types in Java 6 http://bugs.sun.com/view_bug.do?bug_id=6434149
        return Class.forName(name, false, cl);
    }
}
