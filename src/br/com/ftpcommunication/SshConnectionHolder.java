package br.com.ftpcommunication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.connection.ChannelInputStream;
import com.sshtools.j2ssh.connection.ChannelOutputStream;
import com.sshtools.j2ssh.session.SessionChannelClient;

public class SshConnectionHolder {
	
	private SshClient sshClient;

    protected SshConnectionHolder(SshClient sshClient) {
        if (sshClient == null) {
            throw new IllegalStateException();
        }
        this.sshClient = sshClient;
    }

    public void disconnect() {
        if (sshClient.isConnected()) {
            sshClient.disconnect();
        }
        sshClient = null;
    }

    public boolean isConnected() {
        return sshClient.isConnected();
    }

    public void removeFileViaSFTP(String fileName, String folder) throws IOException {
        if (this.sshClient == null || !this.sshClient.isConnected()) {
            throw new IllegalStateException();
        }
        SftpClient sftpClient = sshClient.openSftpClient();
        sftpClient.cd("");
        sftpClient.cd(folder);
        sftpClient.rm(fileName);
    }

    public void sendFileViaSFTP(File file, String folder) throws IOException {
        if (this.sshClient == null || !this.sshClient.isConnected()) {
            throw new IllegalStateException();
        }
        SftpClient sftpClient = sshClient.openSftpClient();
        sftpClient.cd("");
        sftpClient.cd(folder);
        FileInputStream fis = new FileInputStream(file);
        sftpClient.put(fis, file.getName());
        fis.close();
    }

    public File getFileViaSFTP(String folder, String fileName, File output) throws IOException {
        if (this.sshClient == null || !this.sshClient.isConnected()) {
            throw new IllegalStateException();
        }
        SftpClient sftpClient = sshClient.openSftpClient();
        FileOutputStream fos = new FileOutputStream(output);
        sftpClient.cd("");
        sftpClient.cd(folder);
        sftpClient.get(fileName, fos);
        fos.close();
        return output;
    }

    public void execCommand(String cmd) throws IOException {
        if (this.sshClient == null || !this.sshClient.isConnected()) {
            throw new IllegalStateException();
        }
        SessionChannelClient session = this.sshClient.openSessionChannel();
        if (session.startShell()) {
            ChannelOutputStream output = session.getOutputStream();
            cmd = cmd + "\n";
            output.write(cmd.getBytes());
            output.close();
        }
        if (session.isOpen()) {
            session.close();
        }
    }

    public String execCommandAndGetResponse(String cmd) throws IOException {
        if (this.sshClient == null || !this.sshClient.isConnected()) {
            throw new IllegalStateException();
        }
        String response = "";
        SessionChannelClient session = this.sshClient.openSessionChannel();
        if (session.startShell()) {
            ChannelOutputStream output = session.getOutputStream();
            ChannelInputStream input = session.getInputStream();
            cmd = cmd + "\n";
            output.write(cmd.getBytes());
            byte buffer[] = new byte[255];
            int read;
            while ((read = input.read(buffer)) > 0) {
                response = new String(buffer, 0, read);
                break;
            }
            response = response.trim();
            input.close();
            output.close();
        }
        if (session.isOpen()) {
            session.close();
        }
        return response;
    }

}
