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
package org.koishi.launcher.h2co3.core.game.mod.server;

import com.google.gson.JsonParseException;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.koishi.launcher.h2co3.core.game.download.DefaultDependencyManager;
import org.koishi.launcher.h2co3.core.game.mod.MismatchedModpackTypeException;
import org.koishi.launcher.h2co3.core.game.mod.Modpack;
import org.koishi.launcher.h2co3.core.game.mod.ModpackProvider;
import org.koishi.launcher.h2co3.core.game.mod.ModpackUpdateTask;
import org.koishi.launcher.h2co3.core.utils.gson.JsonUtils;
import org.koishi.launcher.h2co3.core.utils.io.CompressingUtils;
import org.koishi.launcher.h2co3.core.utils.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public final class ServerModpackProvider implements ModpackProvider {
    public static final ServerModpackProvider INSTANCE = new ServerModpackProvider();

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public Task<?> createCompletionTask(DefaultDependencyManager dependencyManager, String version) {
        return new ServerModpackCompletionTask(dependencyManager, version);
    }

    @Override
    public Task<?> createUpdateTask(DefaultDependencyManager dependencyManager, String name, File zipFile, Modpack modpack) throws MismatchedModpackTypeException {
        if (!(modpack.getManifest() instanceof ServerModpackManifest))
            throw new MismatchedModpackTypeException(getName(), modpack.getManifest().getProvider().getName());

        return new ModpackUpdateTask(dependencyManager.getGameRepository(), name, new ServerModpackLocalInstallTask(dependencyManager, zipFile, modpack, (ServerModpackManifest) modpack.getManifest(), name));
    }

    @Override
    public Modpack readManifest(ZipFile zip, Path file, Charset encoding) throws IOException, JsonParseException {
        String json = CompressingUtils.readTextZipEntry(zip, "server-manifest.json");
        ServerModpackManifest manifest = JsonUtils.fromNonNullJson(json, ServerModpackManifest.class);
        return manifest.toModpack(encoding);
    }
}
