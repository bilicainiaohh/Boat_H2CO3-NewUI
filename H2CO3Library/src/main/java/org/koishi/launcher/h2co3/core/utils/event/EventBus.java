/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.koishi.launcher.h2co3.core.utils.event;


import org.koishi.launcher.h2co3.core.utils.Logging;

import java.util.concurrent.ConcurrentHashMap;

public final class EventBus {

    public static final EventBus EVENT_BUS = new EventBus();
    private final ConcurrentHashMap<Class<?>, EventManager<?>> events = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Event> EventManager<T> channel(Class<T> clazz) {
        return (EventManager<T>) events.computeIfAbsent(clazz, ignored -> new EventManager<>());
    }

    @SuppressWarnings("unchecked")
    public Event.Result fireEvent(Event obj) {
        Logging.LOG.info(obj + " gets fired");

        return channel((Class<Event>) obj.getClass()).fireEvent(obj);
    }
}
