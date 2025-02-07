package org.koishi.launcher.h2co3.core.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtil extends Thread {

    private final Callback callback;
    private Process process;
    private BufferedReader output;

    public ShellUtil(String home, Callback callback) {
        this.callback = callback;
        ProcessBuilder pb = new ProcessBuilder("sh");
        pb.directory(new File(home));
        pb.redirectErrorStream(true);
        try {
            process = pb.start();
            output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            append("export HOME=" + home + "&&cd\n");
        } catch (IOException e) {
            callback.output(e.getMessage());
        }
    }

    public void append(String command) {
        if (process != null) {
            try {
                process.getOutputStream().write((command + "\n").getBytes());
                process.getOutputStream().flush();
            } catch (IOException e) {
                callback.output(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try (BufferedReader reader = output) {
            String line;
            while ((line = reader.readLine()) != null) {
                callback.output(line);
            }
        } catch (IOException e) {
            callback.output(e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
                try {
                    process.getInputStream().close();
                    process.getErrorStream().close();
                    process.getOutputStream().close();
                } catch (IOException e) {
                    callback.output(e.getMessage());
                }
            }
        }
    }

    public interface Callback {
        void output(String output);
    }
}