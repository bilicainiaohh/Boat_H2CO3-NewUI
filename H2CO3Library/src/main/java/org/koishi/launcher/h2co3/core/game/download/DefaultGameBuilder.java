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
package org.koishi.launcher.h2co3.core.game.download;

import org.koishi.launcher.h2co3.core.utils.function.ExceptionalFunction;
import org.koishi.launcher.h2co3.core.utils.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultGameBuilder extends GameBuilder {

    private final DefaultDependencyManager dependencyManager;

    public DefaultGameBuilder(DefaultDependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public DefaultDependencyManager getDependencyManager() {
        return dependencyManager;
    }

    @Override
    public Task<?> buildAsync() {
        List<String> stages = new ArrayList<>();

        Task<Version> libraryTask = Task.supplyAsync(() -> new Version(name));
        libraryTask = libraryTask.thenComposeAsync(libraryTaskHelper(gameVersion, "game", gameVersion));
        stages.add("h2co3.install.game:" + gameVersion);
        stages.add("h2co3.install.assets");

        for (Map.Entry<String, String> entry : toolVersions.entrySet()) {
            libraryTask = libraryTask.thenComposeAsync(libraryTaskHelper(gameVersion, entry.getKey(), entry.getValue()));
            stages.add(String.format("h2co3.install.%s:%s", entry.getKey(), entry.getValue()));
        }

        for (RemoteVersion remoteVersion : remoteVersions) {
            libraryTask = libraryTask.thenComposeAsync(version -> dependencyManager.installLibraryAsync(version, remoteVersion));
            stages.add(String.format("h2co3.install.%s:%s", remoteVersion.getLibraryId(), remoteVersion.getSelfVersion()));
        }

        return libraryTask.thenComposeAsync(dependencyManager.getGameRepository()::saveAsync).whenComplete(exception -> {
            if (exception != null) {
                System.out.println(name + ": " + exception.getMessage());
                dependencyManager.getGameRepository().removeVersionFromDisk(name);
            }
        }).withStagesHint(stages);
    }

    private ExceptionalFunction<Version, Task<Version>, ?> libraryTaskHelper(String gameVersion, String libraryId, String libraryVersion) {
        System.out.println(gameVersion + " " + libraryId + " " + libraryVersion);
        return version -> dependencyManager.installLibraryAsync(gameVersion, version, libraryId, libraryVersion);
    }
}
