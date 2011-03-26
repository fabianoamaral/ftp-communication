package br.com.ftpcommunication;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class TesteFtpConnection {
	
	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		properties.load(TesteFtpConnection.class.getResourceAsStream("/ftp.properties"));
		
		String server = properties.getProperty("server");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		
		SshConnectionHolder sshConnection = SshConnectionFactory.createSshConnection(server, user, password);
		String remoteDirectory = "/path-remote-dir";
		File localFile = new File("/path-local-dir", "fileName");
		
        sshConnection.sendFileViaSFTP(localFile, remoteDirectory);
        
        String cmd = "ls";
        String response = sshConnection.execCommandAndGetResponse(cmd);
        
        sshConnection.getFileViaSFTP(remoteDirectory, localFile.getName(), localFile);
        sshConnection.removeFileViaSFTP(localFile.getName(), remoteDirectory);
        sshConnection.disconnect();
		
	}

}
