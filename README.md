Initial Author
--------------
Leandro
Twitter: @leandronsp
Email: leandronsp@gmail.com

Ftp-communication
===============

A easy way for SSH and FTP communication.


Usage
-----

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
